package haushaltsbuch.logic;

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
import java.util.Vector;

import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.bean.Kostenartsaldo;
import haushaltsbuch.bean.util.KoartsaldiCollection;
import haushaltsbuch.bean.util.KostenartCollection;
import haushaltsbuch.dao.KostenartDAO;
import haushaltsbuch.dao.KostenartsaldoDAO;
import haushaltsbuch.exceptions.MathException;
import haushaltsbuch.util.Tools;

public class LogicJahrUEKoart {

    private Map<Kostenart, KostenartCollection> kostenarten = Collections
            .synchronizedMap(new HashMap<Kostenart, KostenartCollection>());

    private Integer year = LocalDate.now().getYear();

    private BigDecimal incPrevYear = BigDecimal.ZERO;
    private BigDecimal incCurYear = BigDecimal.ZERO;
    private BigDecimal expPrevYear = BigDecimal.ZERO;
    private BigDecimal expCurYear = BigDecimal.ZERO;
    private BigDecimal expHHPrevYear = BigDecimal.ZERO;
    private BigDecimal expHHCurYear = BigDecimal.ZERO;
    private BigDecimal incCurrent, expCurrent, expHHCurrent;

    public LogicJahrUEKoart(Integer year) {
        this.year = year;
        for (int i = -1; i < 12; i++) {
            LocalDate date;
            if (i == -1) {
                date = LocalDate.of(year + i, Month.DECEMBER.getValue(), 1);
            } else {
                date = LocalDate.of(year, i + 1, 1);
            }
            date.atStartOfDay(ZoneId.systemDefault());

            Vector<Kostenartsaldo> month = KostenartsaldoDAO.instance().getKostenartsaldi(date);
            for (Kostenartsaldo ks : month) {
                if (!kostenarten.containsKey(ks.getKoart())) {
                    KostenartCollection k = new KostenartCollection(year, ks.getKoart());
                    k.addKostenartsaldo(ks);
                    kostenarten.put(ks.getKoart(), k);
                } else
                    kostenarten.get(ks.getKoart()).addKostenartsaldo(ks);
            }
        }
        Iterator<Map.Entry<Kostenart, KostenartCollection>> i = kostenarten.entrySet().iterator();
        while (i.hasNext()) {
            Entry<Kostenart, KostenartCollection> e = i.next();
            if (e.getKey().getKoartgrp().getKoartgrpkat().equals(Kostenartgruppe.KAT_INCOME)) {
                incPrevYear = incPrevYear.add(e.getValue().getPrevYear());
                incCurYear = incCurYear.add(e.getValue().getCurYear());
            } else {
                expPrevYear = expPrevYear.add(e.getValue().getPrevYear());
                expCurYear = expCurYear.add(e.getValue().getCurYear());
                if (!e.getKey().getKoartgrp().getKoartgrpart().equals(Kostenartgruppe.ART_HH)) {
                    expHHPrevYear = expHHPrevYear.add(e.getValue().getPrevYear());
                    expHHCurYear = expHHCurYear.add(e.getValue().getCurYear());
                }
            }
        }
    }

    public BigDecimal getIncPrevYear() {
        return incPrevYear;
    }

    public BigDecimal getExpPrevYear() {
        return expPrevYear;
    }

    public BigDecimal getExpHHPrevYear() {
        return expHHPrevYear;
    }

    public BigDecimal getSavPrevYear() {
        return getIncPrevYear().subtract(getExpPrevYear());
    }

    public BigDecimal getIncCurYear() {
        return incCurYear;
    }

    public BigDecimal getExpCurYear() {
        return expCurYear;
    }

    public BigDecimal getExpHHCurYear() {
        return expHHCurYear;
    }

    public BigDecimal getSavCurYear() {
        return getIncCurYear().subtract(getExpCurYear());
    }

    public BigDecimal getIncDiff() {
        return getIncCurYear().subtract(getIncPrevYear());
    }

    public BigDecimal getExpDiff() {
        return getExpCurYear().subtract(getExpPrevYear());
    }

    public BigDecimal getExpHHDiff() {
        return getExpHHCurYear().subtract(getExpHHPrevYear());
    }

    public BigDecimal getSavDiff() {
        return getSavCurYear().subtract(getSavPrevYear());
    }

    public BigDecimal getIncCurrent() {
        if (incCurrent == null) {
            incCurrent = BigDecimal.ZERO;
            for (Kostenart k : KostenartDAO.instance().getAll()) {
                if (k.getKoartgrp().getKoartgrpkat().equals(Kostenartgruppe.KAT_INCOME))
                    incCurrent = incCurrent.add(k.getKoartsaldo());
            }
        }
        return incCurrent;
    }

    public BigDecimal getExpCurrent() {
        if (expCurrent == null) {
            expCurrent = BigDecimal.ZERO;
            for (Kostenart k : KostenartDAO.instance().getAll()) {
                if (k.getKoartgrp().getKoartgrpkat().equals(Kostenartgruppe.KAT_EXPENSES))
                    expCurrent = expCurrent.add(k.getKoartsaldo());
            }
        }
        return expCurrent;
    }

    public BigDecimal getExpHHCurrent() {
        if (expHHCurrent == null) {
            expHHCurrent = BigDecimal.ZERO;
            for (Kostenart k : KostenartDAO.instance().getAll()) {
                if (k.getKoartgrp().getKoartgrpkat().equals(Kostenartgruppe.KAT_EXPENSES)
                        && !k.getKoartgrp().getKoartgrpart().equals(Kostenartgruppe.ART_HH))
                    expHHCurrent = expHHCurrent.add(k.getKoartsaldo());
            }
        }
        return expHHCurrent;
    }

    public BigDecimal getSavCurrent() {
        return getIncCurrent().subtract(getExpCurrent());
    }

    public BigDecimal getIncAvg() {
        if (year.equals(Tools.getLastDate().getYear())) {
            return getIncCurYear().divide(BigDecimal.valueOf(Tools.getLastDate().getMonthValue()), 13,
                    BigDecimal.ROUND_HALF_UP);
        } else
            return getIncCurYear().divide(new BigDecimal(12), 13, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getExpAvg() {
        if (year.equals(Tools.getLastDate().getYear())) {
            return getExpCurYear().divide(BigDecimal.valueOf(Tools.getLastDate().getMonthValue()), 13,
                    BigDecimal.ROUND_HALF_UP);
        } else
            return getExpCurYear().divide(new BigDecimal(12), 13, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getExpHHAvg() {
        if (year.equals(Tools.getLastDate().getYear())) {
            return getExpHHCurYear().divide(BigDecimal.valueOf(Tools.getLastDate().getMonthValue()), 13,
                    BigDecimal.ROUND_HALF_UP);
        } else
            return getExpHHCurYear().divide(new BigDecimal(12), 13, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getSavAvg() {
        if (year.equals(Tools.getLastDate().getYear())) {
            return getSavCurYear().divide(BigDecimal.valueOf(Tools.getLastDate().getMonthValue()), 13,
                    BigDecimal.ROUND_HALF_UP);
        } else
            return getSavCurYear().divide(new BigDecimal(12), 13, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getIncPerc() throws MathException {
        if (getIncPrevYear().compareTo(BigDecimal.ZERO) == 0)
            throw new MathException();
        BigDecimal percentage = getIncDiff().divide(getIncPrevYear(), 13, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
        if (percentage.compareTo(BigDecimal.ZERO) < 0 && getIncDiff().compareTo(BigDecimal.ZERO) >= 0)
            percentage = percentage.multiply(new BigDecimal(-1));
        return percentage;
    }

    public BigDecimal getExpPerc() throws MathException {
        if (getExpPrevYear().compareTo(BigDecimal.ZERO) == 0)
            throw new MathException();
        BigDecimal percentage = getExpDiff().divide(getExpPrevYear(), 13, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
        if (percentage.compareTo(BigDecimal.ZERO) < 0 && getExpDiff().compareTo(BigDecimal.ZERO) >= 0)
            percentage = percentage.multiply(new BigDecimal(-1));
        return percentage;
    }

    public BigDecimal getExpHHPerc() throws MathException {
        if (getExpHHPrevYear().compareTo(BigDecimal.ZERO) == 0)
            throw new MathException();
        BigDecimal percentage = getExpHHDiff().divide(getExpHHPrevYear(), 13, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
        if (percentage.compareTo(BigDecimal.ZERO) < 0 && getExpHHDiff().compareTo(BigDecimal.ZERO) >= 0)
            percentage = percentage.multiply(new BigDecimal(-1));
        return percentage;
    }

    public BigDecimal getSavPerc() throws MathException {
        if (getSavPrevYear().compareTo(BigDecimal.ZERO) == 0)
            throw new MathException();
        BigDecimal percentage = getSavDiff().divide(getSavPrevYear(), 13, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
        if (percentage.compareTo(BigDecimal.ZERO) < 0 && getSavDiff().compareTo(BigDecimal.ZERO) >= 0)
            percentage = percentage.multiply(new BigDecimal(-1));
        return percentage;
    }

    public Collection<KostenartCollection> getKostenarten() {
        return this.kostenarten.values();
    }

    public KoartsaldiCollection getDetailedData(Integer view) {
        // KoartsaldiCollection kc = new KoartsaldiCollection(this.year,
        // this.kostenarten, view);
        // return kc;
        return null;
    }

}