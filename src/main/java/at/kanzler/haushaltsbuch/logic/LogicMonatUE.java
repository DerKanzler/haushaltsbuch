package at.kanzler.haushaltsbuch.logic;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import at.kanzler.haushaltsbuch.bean.Buchung;
import at.kanzler.haushaltsbuch.bean.Konto;
import at.kanzler.haushaltsbuch.bean.Kontostand;
import at.kanzler.haushaltsbuch.bean.Kostenart;
import at.kanzler.haushaltsbuch.bean.Kostenartgruppe;
import at.kanzler.haushaltsbuch.bean.Kostenartsaldo;
import at.kanzler.haushaltsbuch.bean.util.SearchBuchung;
import at.kanzler.haushaltsbuch.conf.HB;
import at.kanzler.haushaltsbuch.dao.BuchungDAO;
import at.kanzler.haushaltsbuch.dao.KontoDAO;
import at.kanzler.haushaltsbuch.dao.KontostandDAO;
import at.kanzler.haushaltsbuch.dao.KostenartDAO;
import at.kanzler.haushaltsbuch.dao.KostenartsaldoDAO;
import at.kanzler.haushaltsbuch.util.Tools;

public class LogicMonatUE {

    private SortedMap<Kostenartgruppe, Vector<Kostenartsaldo>> koart = new TreeMap<>();

    private BigDecimal disposableSum = BigDecimal.ZERO;
    private BigDecimal offlimitSum = BigDecimal.ZERO;
    private BigDecimal totalRevenues = BigDecimal.ZERO;
    private BigDecimal totalExpenses = BigDecimal.ZERO;

    public LogicMonatUE(LocalDate date) {
        if (date.equals(Tools.getLastMonth())) {
            Vector<Konto> konten = KontoDAO.instance().getAllValid();
            for (Konto k : konten) {
                if (k.isDisposable()) {
                    disposableSum = disposableSum.add(k.getSaldo());
                } else {
                    offlimitSum = offlimitSum.add(k.getSaldo());
                }
            }
            getCosttypeGroups();
        } else {
            Vector<Kontostand> kontostaende = KontostandDAO.instance().getKontostaende(date);
            for (Kontostand kost : kontostaende) {
                if (kost.getKonto().isDisposable()) {
                    disposableSum = disposableSum.add(kost.getKtostsaldo());
                } else {
                    offlimitSum = offlimitSum.add(kost.getKtostsaldo());
                }
            }
            getCosttypeGroups(date);
        }
    }

    public Vector<Buchung> getBookings(SearchBuchung sb) {
        return BuchungDAO.instance().search(sb);
    }

    public Vector<Kontostand> getAccounts(LocalDate date) {
        Vector<Kontostand> kost = new Vector<>();
        if (date.equals(Tools.getLastMonth())) {
            Vector<Konto> konten = KontoDAO.instance().getAll();
            for (Konto konto : konten) {
                Kontostand ks = new Kontostand();
                ks.setKonto(konto);
                ks.setKtostdat(konto.getBuchdat());
                ks.setKtostsaldo(konto.getSaldo());
                if (displayAccount(date, ks)) {
                    kost.add(ks);
                }
            }
        } else {
            Vector<Kontostand> kontostaende = KontostandDAO.instance().getKontostaende(date);
            for (Kontostand ks : kontostaende) {
                if (displayAccount(date, ks)) {
                    kost.add(ks);
                }
            }
        }
        return kost;
    }

    public SortedMap<Kostenartgruppe, Vector<Kostenartsaldo>> getCosttypes() {
        return koart;
    }

    private SortedMap<Kostenartgruppe, Vector<Kostenartsaldo>> getCosttypeGroups() {
        Vector<Kostenartsaldo> koartsaldi = new Vector<>();
        Vector<Kostenart> kostenarten = KostenartDAO.instance().getAll();
        for (Kostenart koart : kostenarten) {
            Kostenartsaldo k = new Kostenartsaldo();
            k.setKoart(koart);
            k.setKoartmonsaldo(koart.getKoartsaldo());
            k.setKoartsalddat(Tools.getLastMonth());
            koartsaldi.add(k);
        }
        return createCosttypeGroupsHashMap(koartsaldi);
    }

    private SortedMap<Kostenartgruppe, Vector<Kostenartsaldo>> getCosttypeGroups(LocalDate date) {
        return createCosttypeGroupsHashMap(KostenartsaldoDAO.instance().getKostenartsaldi(date));
    }

    private SortedMap<Kostenartgruppe, Vector<Kostenartsaldo>> createCosttypeGroupsHashMap(Vector<Kostenartsaldo> koartsaldi) {
        for (Kostenartsaldo ct : koartsaldi) {
            if (ct.getKoartmonsaldo().compareTo(BigDecimal.ZERO) != 0) {
                if (ct.getKoart().getKoartgrp().getKoartgrpkat().equals(HB.AUSGABE)) {
                    totalExpenses = totalExpenses.add(ct.getKoartmonsaldo());
                } else {
                    totalRevenues = totalRevenues.add(ct.getKoartmonsaldo());
                }
                if (!koart.containsKey(ct.getKoart().getKoartgrp())) {
                    Vector<Kostenartsaldo> cts = new Vector<>();
                    cts.add(ct);
                    koart.put(ct.getKoart().getKoartgrp(), cts);
                } else {
                    koart.get(ct.getKoart().getKoartgrp()).add(ct);
                }
            }
        }
        return koart;
    }

    public BigDecimal getDisposableSum() {
        return disposableSum;
    }

    public BigDecimal getOffLimitSum() {
        return offlimitSum;
    }

    public BigDecimal getTotalAssets() {
        return getDisposableSum().add(getOffLimitSum());
    }

    public BigDecimal getTotalRevenues() {
        return totalRevenues;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public BigDecimal getTotalSavings() {
        return getTotalRevenues().subtract(getTotalExpenses());
    }

    private Boolean displayAccount(LocalDate date, Kontostand ks) {
        // Falls das Abldaufdatums des Kontos in der Zukunft liegt (egal ob geschlossen
        // oder offen), wird Konto angezeigt
        // Aber auch falls das Konto noch offen ist .
        // Oder wenn es in dem Monat eine Buchung gegeben hat.
        if (ks.getKonto().getAbldat().compareTo(date) >= 0 || ks.getKonto().isValid()
                || ks.getKtostsaldo().compareTo(BigDecimal.ZERO) != 0) {
            return true;
        } else {
            return false;
        }
    }

}