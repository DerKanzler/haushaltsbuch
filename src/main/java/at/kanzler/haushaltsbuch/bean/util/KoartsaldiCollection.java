package at.kanzler.haushaltsbuch.bean.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import at.kanzler.haushaltsbuch.bean.Konto;
import at.kanzler.haushaltsbuch.bean.Kontostand;
import at.kanzler.haushaltsbuch.gui.dialogs.UIKapitalJahrDialog;
import at.kanzler.haushaltsbuch.util.Tools;

import java.util.SortedMap;
import java.util.TreeMap;

public class KoartsaldiCollection {

    private Integer type;
    private Integer year;
    private BigDecimal current = BigDecimal.ZERO;
    private BigDecimal yearIn = BigDecimal.ZERO;
    private BigDecimal yearOut = BigDecimal.ZERO;
    private SortedMap<LocalDate, BigDecimal> values = new TreeMap<LocalDate, BigDecimal>();

    public KoartsaldiCollection(Integer y, Map<Konto, KontoCollection> konten, Integer t) {
        this.year = y;
        this.type = t;

        Iterator<Map.Entry<Konto, KontoCollection>> i = konten.entrySet().iterator();
        while (i.hasNext()) {
            Entry<Konto, KontoCollection> e = i.next();
            switch (type) {
                case (UIKapitalJahrDialog.KAPITAL_VERFUEGBAR):
                    if (e.getKey().isDisposable()) {
                        yearIn = yearIn.add(e.getValue().getYearIn());
                        yearOut = yearOut.add(e.getValue().getYearOut());
                        current = current.add(e.getKey().getSaldo());
                        for (Kontostand k : e.getValue().getKontostaende()) {
                            if (values.get(k.getKtostdat()) == null) {
                                values.put(k.getKtostdat(), new BigDecimal(0));
                            }
                            values.put(k.getKtostdat(), values.get(k.getKtostdat()).add(k.getKtostsaldo()));
                        }
                    }
                    break;
                case (UIKapitalJahrDialog.KAPITAL_GESPERRT):
                    if (!e.getKey().isDisposable()) {
                        yearIn = yearIn.add(e.getValue().getYearIn());
                        yearOut = yearOut.add(e.getValue().getYearOut());
                        current = current.add(e.getKey().getSaldo());
                        for (Kontostand k : e.getValue().getKontostaende()) {
                            if (values.get(k.getKtostdat()) == null) {
                                values.put(k.getKtostdat(), new BigDecimal(0));
                            }
                            values.put(k.getKtostdat(), values.get(k.getKtostdat()).add(k.getKtostsaldo()));
                        }
                    }
                    break;
                case (UIKapitalJahrDialog.KAPITAL_GESAMT):
                    yearIn = yearIn.add(e.getValue().getYearIn());
                    yearOut = yearOut.add(e.getValue().getYearOut());
                    current = current.add(e.getKey().getSaldo());
                    for (Kontostand k : e.getValue().getKontostaende()) {
                        if (values.get(k.getKtostdat()) == null) {
                            values.put(k.getKtostdat(), new BigDecimal(0));
                        }
                        values.put(k.getKtostdat(), values.get(k.getKtostdat()).add(k.getKtostsaldo()));
                    }
                    break;
            }
        }
    }

    public Integer getType() {
        return type;
    }

    public Integer getYear() {
        return year;
    }

    public BigDecimal getYearIn() {
        return yearIn;
    }

    public BigDecimal getYearOut() {
        return yearOut;
    }

    public BigDecimal getDiff() {
        if (getYear().equals(Tools.getLastDate().getYear())) {
            return current.subtract(yearIn);
        } else {
            return yearOut.subtract(yearIn);
        }
    }

    public BigDecimal getCurrent() {
        return current;
    }

    public SortedMap<LocalDate, BigDecimal> getValues() {
        return values;
    }

}