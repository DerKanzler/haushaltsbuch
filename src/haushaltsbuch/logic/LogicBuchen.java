package haushaltsbuch.logic;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Vector;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.bean.Konto;
import haushaltsbuch.bean.Kontostand;
import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.bean.Kostenartsaldo;
import haushaltsbuch.bean.util.SaveBuchung;
import haushaltsbuch.bean.util.SearchBuchung;
import haushaltsbuch.dao.BuchungDAO;
import haushaltsbuch.dao.KontoDAO;
import haushaltsbuch.dao.KontostandDAO;
import haushaltsbuch.dao.KostenartDAO;
import haushaltsbuch.dao.KostenartsaldoDAO;
import haushaltsbuch.dao.VJBuchungDAO;
import haushaltsbuch.db.DB;
import haushaltsbuch.util.Tools;

public class LogicBuchen {

    private static LogicBuchen logic;

    private LogicBuchen() {}

    public static LogicBuchen instance() {
        if (logic == null) {
            logic = new LogicBuchen();
        }
        return logic;
    }

    public Vector<Buchung> getMonthBookings() {
        try {
            SearchBuchung sb = new SearchBuchung();
            sb.setFromDate(Tools.getLastMonth());
            return BuchungDAO.instance().search(sb);
        } catch (Exception e) {
            return new Vector<>();
        }
    }

    public Vector<Konto> getKontenList() {
        return KontoDAO.instance().getAllValid();
    }

    public Vector<Kostenart> getKoartList() {
        return KostenartDAO.instance().getAll();
    }

    public BigDecimal getDisposableSum() {
        BigDecimal sum = BigDecimal.ZERO;
        Vector<Konto> konten = KontoDAO.instance().getAll();
        for (Konto k : konten) {
            if (k.isValid() && k.isDisposable()) {
                sum = sum.add(k.getSaldo());
            }
        }
        return sum;
    }

    public BigDecimal getOffLimitSum() {
        BigDecimal sum = BigDecimal.ZERO;
        Vector<Konto> konten = KontoDAO.instance().getAll();
        for (Konto k : konten) {
            if (k.isValid() && !k.isDisposable()) {
                sum = sum.add(k.getSaldo());
            }
        }
        return sum;
    }

    public BigDecimal getTotalAssets() {
        return getDisposableSum().add(getOffLimitSum());
    }

    public BigDecimal getTotalRevenues() {
        BigDecimal totalRevenues = BigDecimal.ZERO;
        Vector<Buchung> monthBookings = getMonthBookings();
        for (Buchung b : monthBookings) {
            if (b.getKoart() != null) {
                if ("E".equals(b.getKoart().getKoartgrp().getKoartgrpkat())) {
                    totalRevenues = totalRevenues.add(b.getBuchbetr());
                }
            }
        }
        return totalRevenues;
    }

    public BigDecimal getTotalExpenses() {
        BigDecimal totalExpenses = BigDecimal.ZERO;
        Vector<Buchung> monthBookings = getMonthBookings();
        for (Buchung b : monthBookings) {
            if (b.getKoart() != null) {
                if ("A".equals(b.getKoart().getKoartgrp().getKoartgrpkat())) {
                    totalExpenses = totalExpenses.add(b.getBuchbetr());
                }
            }
        }
        return totalExpenses;
    }

    public BigDecimal getTotalSavings() {
        return getTotalRevenues().subtract(getTotalExpenses());
    }

    public SaveBuchung save(Buchung b) throws RuntimeException {
        SaveBuchung sb = new SaveBuchung();
        if (check(b)) {
            try {
                monthYearEndExecutor(b, sb);
                BuchungDAO.instance().saveOrUpdate(b);
                saveKoart(b);
                saveKontovon(b);
                saveKontonach(b);
                if (DB.instance().commit()) {
                    sb.setSuccess(true);
                    return sb;
                } else {
                    DB.instance().rollback();
                    throw new RuntimeException("Speichern fehlgeschlagen!");
                }
            } catch (RuntimeException re) {
                DB.instance().rollback();
                throw new RuntimeException("Speichern fehlgeschlagen!");
            } finally {
                BuchungDAO.instance().clear();
            }
        } else {
            sb.setSuccess(false);
            return sb;
        }
    }

    public Boolean delete(Buchung b) throws RuntimeException {
        try {
            BuchungDAO.instance().delete(b);
            saveKoartDelete(b);
            saveKontovonDelete(b);
            saveKontonachDelete(b);
            if (DB.instance().commit()) {
                return true;
            } else {
                DB.instance().rollback();
                throw new RuntimeException("Löschen fehlgeschlagen!");
            }
        } catch (RuntimeException re) {
            DB.instance().rollback();
            throw new RuntimeException("Löschen fehlgeschlagen!");
        }
    }

    private Boolean check(Buchung b) {
        b.setEindat(LocalDate.now());
        if (b.getBuchdat() == null) {
            throw new RuntimeException("Das Datum darf nicht leer sein!");
        } else {
            LocalDate bookDate = b.getBuchdat();
            LocalDate bookMonth = Tools.getLastMonth();
            LocalDate currentDate = LocalDate.now();

            if (bookDate.getYear() < bookMonth.getYear()) {
                throw new RuntimeException("Das Buchdatum liegt zu weit in der Vergangenheit!");
            }
            if ((bookDate.getMonthValue() < bookMonth.getMonthValue()) && bookDate.getYear() <= bookMonth.getYear()) {
                throw new RuntimeException("Das Buchdatum liegt zu weit in der Vergangenheit!");
            }
            if (bookDate.isAfter(currentDate)) {
                throw new RuntimeException("Das Buchdatum liegt in der Zukunft!");
            }
        }
        if (b.getBuchtext() == null) {
            throw new RuntimeException("Der Buchungstext darf nicht leer sein!");
        }
        if (b.getBuchbetr() == null) {
            throw new RuntimeException("Der Betrag darf nicht leer sein!");
        } else if (b.getBuchbetr().compareTo(new BigDecimal(0)) <= 0) {
            throw new RuntimeException("Der Betrag muss größer als 0 sein!");
        }
        if (b.getKoart() == null && b.getKontovon() == null && b.getKontonach() == null) {
            throw new RuntimeException("Kostenart, Konto von und Konto nach sind leer!");
        } else if (b.getKoart() != null) {
            if ("E".equals(b.getKoart().getKoartgrp().getKoartgrpkat())) {
                if (b.getKontovon() != null && b.getKontonach() == null) {
                    throw new RuntimeException(
                            "Bei dieser Kostenart muss Konto von leer sein und Konto nach ausgewählt sein!");
                } else if (b.getKontovon() != null && b.getKontonach() != null) {
                    throw new RuntimeException("Bei dieser Kostenart muss Konto von leer sein!");
                } else if (b.getKontovon() == null && b.getKontonach() == null) {
                    throw new RuntimeException("Bei dieser Kostenart muss Konto nach ausgewählt sein!");
                } else if (b.getKontovon() == null && b.getKontonach() != null) {
                    return true;
                }
            } else if ("A".equals(b.getKoart().getKoartgrp().getKoartgrpkat())) {
                if (b.getKontovon() != null && b.getKontonach() == null) {
                    return true;
                } else if (b.getKontovon() != null && b.getKontonach() != null) {
                    throw new RuntimeException("Bei dieser Kostenart muss Konto nach leer sein!");
                } else if (b.getKontovon() == null && b.getKontonach() == null) {
                    throw new RuntimeException("Bei dieser Kostenart muss Konto von ausgewählt sein!");
                } else if (b.getKontovon() == null && b.getKontonach() != null) {
                    throw new RuntimeException(
                            "Bei dieser Kostenart muss Konto von ausgewählt sein und Konto nach leer sein!");
                }
            }
        } else if (b.getKoart() == null) {
            if (b.getKontovon() != null && b.getKontonach() == null) {
                throw new RuntimeException("Bei leerer Kostenart müssen Konto von und Konto nach ausgewählt sein!");
            } else if (b.getKontovon() != null && b.getKontonach() != null) {
                if (b.getKontovon().equals(b.getKontonach())) {
                    throw new RuntimeException("Konto von und Konto nach dürfen nicht gleich sein!");
                } else {
                    return true;
                }
            } else if (b.getKontovon() == null && b.getKontonach() != null) {
                throw new RuntimeException("Bei leerer Kostenart müssen Konto von und Konto nach ausgewählt sein!");
            }
        }
        return false;
    }

    private void monthYearEndExecutor(Buchung b, SaveBuchung sb) throws RuntimeException {
        monthYearEnd(b.getBuchdat(), Tools.getLastDate(), sb);
    }

    private void monthYearEnd(LocalDate future, LocalDate current, SaveBuchung sb) {
        future = future.withDayOfMonth(1);
        current = current.withDayOfMonth(1);
        if (future.getYear() > current.getYear()) {
            monthEnd(current, sb);
            if (Month.DECEMBER.equals(current.getMonth())) {
                yearEnd(current, sb);
            }
            monthYearEnd(future, current.plusMonths(1), sb);
        }
        if (future.getMonthValue() > current.getMonthValue() && future.getYear() == current.getYear()) {
            monthEnd(current, sb);
            monthYearEnd(future, current.plusMonths(1), sb);
        }
    }

    private void yearEnd(LocalDate date, SaveBuchung sb) throws RuntimeException {
        try {
            Vector<Buchung> buchungen = BuchungDAO.instance().getAll();
            for (Buchung b : buchungen) {
                b.setBuchung(null);
                VJBuchungDAO.instance().saveOrUpdate(b);
            }
            BuchungDAO.instance().deleteAll();
            Vector<Konto> konten = KontoDAO.instance().getAll();
            for (Konto k : konten) {
                k.setJreinsaldo(k.getSaldo());
                KontoDAO.instance().saveOrUpdate(k);
            }
            sb.addYearEnd(date);
        } catch (RuntimeException re) {
            throw new RuntimeException("Jahresabschluss fehlgeschlagen!");
        }
    }

    private void monthEnd(LocalDate date, SaveBuchung sb) {
        try {
            // tkostenartsaldo befüllen, nur falls buchung im monat vorhanden
            // sind und dann über alle kostenarten
            if (getMonthBookings().size() > 0) {
                // Kostenartsaldo wird in tkostenart auf 0 gesetzt
                Vector<Kostenart> kostenarten = KostenartDAO.instance().getAll();
                for (Kostenart k : kostenarten) {
                    Kostenartsaldo ks = new Kostenartsaldo();
                    ks.setKoart(k);
                    ks.setKoartjrsaldo(getJrSaldo(k));
                    ks.setKoartjrsaldovj(getJrSaldovj(k, date));
                    ks.setKoartmonsaldo(k.getKoartsaldo());
                    ks.setKoartsalddat(date);
                    KostenartsaldoDAO.instance().saveOrUpdate(ks);

                    k.setKoartsaldo(BigDecimal.ZERO);
                    KostenartDAO.instance().saveOrUpdate(k);
                }
            }
            // tkontostand befüllen & tkonto: buchdat aktualisiern
            Vector<Konto> konten = KontoDAO.instance().getAll();
            for (Konto k : konten) {
                Kontostand ks = new Kontostand();
                ks.setKonto(k);
                ks.setKtostdat(date);
                ks.setKtostsaldo(k.getSaldo());
                KontostandDAO.instance().saveOrUpdate(ks);

                k.setBuchdat(date.plusMonths(1));
                KontoDAO.instance().saveOrUpdate(k);
            }
            sb.addMonthEnd(date);
        } catch (RuntimeException re) {
            throw new RuntimeException("Monatsabschluss fehlgeschlagen!");
        }
    }

    private void saveKoart(Buchung b) throws RuntimeException {
        if (b.getKoart() != null) {
            b.getKoart().setKoartsaldo(b.getKoart().getKoartsaldo().add(b.getBuchbetr()));
            KostenartDAO.instance().saveOrUpdate(b.getKoart());
        }
    }

    private void saveKontovon(Buchung b) throws RuntimeException {
        if (b.getKontovon() != null) {
            b.getKontovon().setSaldo(b.getKontovon().getSaldo().subtract(b.getBuchbetr()));
            KontoDAO.instance().saveOrUpdate(b.getKontovon());
        }
    }

    private void saveKontonach(Buchung b) throws RuntimeException {
        if (b.getKontonach() != null) {
            b.getKontonach().setSaldo(b.getKontonach().getSaldo().add(b.getBuchbetr()));
            KontoDAO.instance().saveOrUpdate(b.getKontonach());
        }
    }

    private void saveKoartDelete(Buchung b) throws RuntimeException {
        if (b.getKoart() != null) {
            b.getKoart().setKoartsaldo(b.getKoart().getKoartsaldo().subtract(b.getBuchbetr()));
            KostenartDAO.instance().saveOrUpdate(b.getKoart());
        }
    }

    private void saveKontovonDelete(Buchung b) throws RuntimeException {
        if (b.getKontovon() != null) {
            b.getKontovon().setSaldo(b.getKontovon().getSaldo().add(b.getBuchbetr()));
            KontoDAO.instance().saveOrUpdate(b.getKontovon());
        }
    }

    private void saveKontonachDelete(Buchung b) throws RuntimeException {
        if (b.getKontonach() != null) {
            b.getKontonach().setSaldo(b.getKontonach().getSaldo().subtract(b.getBuchbetr()));
            KontoDAO.instance().saveOrUpdate(b.getKontonach());
        }
    }

    private BigDecimal getJrSaldo(Kostenart koart) {
        BigDecimal jrSaldo = BigDecimal.ZERO;
        Vector<Buchung> buchungen = BuchungDAO.instance().getAll();
        for (Buchung b : buchungen) {
            if (b.getKoart() != null && b.getKoart().equals(koart)) {
                jrSaldo = jrSaldo.add(b.getBuchbetr());
            }
        }
        return jrSaldo;
    }

    private BigDecimal getJrSaldovj(Kostenart koart, LocalDate date) {
        Kostenartsaldo ks = KostenartsaldoDAO.instance().find(koart, date.minusYears(1));
        if (ks.getKoartjrsaldo() != null) {
            return ks.getKoartjrsaldo();
        } else {
            return BigDecimal.ZERO;
        }
    }

}