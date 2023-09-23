package at.kanzler.haushaltsbuch.logic;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import at.kanzler.haushaltsbuch.bean.Konto;
import at.kanzler.haushaltsbuch.bean.Kontostand;
import at.kanzler.haushaltsbuch.bean.util.KapitalCollection;
import at.kanzler.haushaltsbuch.bean.util.KontoCollection;
import at.kanzler.haushaltsbuch.dao.KontoDAO;
import at.kanzler.haushaltsbuch.dao.KontostandDAO;
import at.kanzler.haushaltsbuch.util.Tools;

import java.util.Vector;

public class LogicJahrUEKapital {

    private Map<Konto, KontoCollection> konten = Collections.synchronizedMap(new HashMap<Konto, KontoCollection>());

    private Integer year = LocalDate.now().getYear();

    private BigDecimal dispBeginn = BigDecimal.ZERO;
    private BigDecimal dispEnd = BigDecimal.ZERO;
    private BigDecimal lockBeginn = BigDecimal.ZERO;
    private BigDecimal lockEnd = BigDecimal.ZERO;
    private BigDecimal dispCurrent, lockCurrent;

    public LogicJahrUEKapital(Integer year) {
        this.year = year;
        for (int i = -1; i < 12; i++) {
            LocalDate date;
            if (i == -1) {
                date = LocalDate.of(year + i, Month.DECEMBER.getValue(), 1);
            } else {
                date = LocalDate.of(year, i + 1, 1);
            }
            date.atStartOfDay(ZoneId.systemDefault());

            Vector<Kontostand> month = KontostandDAO.instance().getKontostaende(date);
            for (Kontostand kst : month) {
                if (!konten.containsKey(kst.getKonto())) {
                    KontoCollection k = new KontoCollection(year, kst.getKonto());
                    k.addKontostand(kst);
                    konten.put(kst.getKonto(), k);
                } else {
                    konten.get(kst.getKonto()).addKontostand(kst);
                }
            }
        }

        Iterator<Map.Entry<Konto, KontoCollection>> i = konten.entrySet().iterator();
        while (i.hasNext()) {
            Entry<Konto, KontoCollection> e = i.next();
            if (isEligable(e, year) || e.getKey().isValid()
                    || e.getValue().getYearIn().compareTo(BigDecimal.ZERO) != 0) {
                if (e.getKey().isDisposable()) {
                    dispBeginn = dispBeginn.add(e.getValue().getYearIn());
                    dispEnd = dispEnd.add(e.getValue().getYearOut());
                } else {
                    lockBeginn = lockBeginn.add(e.getValue().getYearIn());
                    lockEnd = lockEnd.add(e.getValue().getYearOut());
                }
            } else {
                i.remove();
            }
        }
    }

    // Kontrolliert ob es einen Saldo in den 12 Monaten gegeben hat
    // Bewegungen im Jahr
    private Boolean isEligable(Entry<Konto, KontoCollection> e, Integer year) {
        Boolean eligable = false;

        KontoCollection k = e.getValue();
        for (Kontostand kst : k.getKontostaende()) {
            if (kst.getKtostsaldo().compareTo(BigDecimal.ZERO) != 0) {
                eligable = true;
            }
        }
        return eligable;
    }

    public BigDecimal getDispBeginn() {
        return dispBeginn;
    }

    public BigDecimal getLockBeginn() {
        return lockBeginn;
    }

    public BigDecimal getSumBeginn() {
        return dispBeginn.add(lockBeginn);
    }

    public BigDecimal getDispEnd() {
        return dispEnd;
    }

    public BigDecimal getLockEnd() {
        return lockEnd;
    }

    public BigDecimal getSumEnd() {
        return dispEnd.add(lockEnd);
    }

    public BigDecimal getDispDiff() {
        if (year.equals(Tools.getLastDate().getYear())) {
            return getDispCurrent().subtract(getDispBeginn());
        } else {
            return getDispEnd().subtract(getDispBeginn());
        }
    }

    public BigDecimal getLockDiff() {
        if (year.equals(Tools.getLastDate().getYear())) {
            return getLockCurrent().subtract(getLockBeginn());
        } else {
            return getLockEnd().subtract(getLockBeginn());
        }
    }

    public BigDecimal getSumDiff() {
        if (year.equals(Tools.getLastDate().getYear())) {
            return getSumCurrent().subtract(getSumBeginn());
        } else {
            return getSumEnd().subtract(getSumBeginn());
        }
    }

    public BigDecimal getDispCurrent() {
        if (dispCurrent == null) {
            dispCurrent = BigDecimal.ZERO;
            Vector<Konto> konten = KontoDAO.instance().getAll();
            for (Konto k : konten) {
                if (k.isValid() && k.isDisposable()) {
                    dispCurrent = dispCurrent.add(k.getSaldo());
                }
            }
        }
        return dispCurrent;
    }

    public BigDecimal getLockCurrent() {
        if (lockCurrent == null) {
            lockCurrent = BigDecimal.ZERO;
            Vector<Konto> konten = KontoDAO.instance().getAll();
            for (Konto k : konten) {
                if (k.isValid() && !k.isDisposable()) {
                    lockCurrent = lockCurrent.add(k.getSaldo());
                }
            }
        }
        return lockCurrent;
    }

    public BigDecimal getSumCurrent() {
        return getDispCurrent().add(getLockCurrent());
    }

    public Collection<KontoCollection> getKonten() {
        return this.konten.values();
    }

    public KapitalCollection getDetailedData(Integer view) {
        KapitalCollection kc = new KapitalCollection(year, konten, view);
        return kc;
    }

}