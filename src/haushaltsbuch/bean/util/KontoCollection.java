package haushaltsbuch.bean.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Vector;

import haushaltsbuch.bean.Konto;
import haushaltsbuch.bean.Kontostand;
import haushaltsbuch.util.Tools;

public class KontoCollection {

    private Konto konto;
    private Integer year;
    private BigDecimal yearIn = new BigDecimal(0);
    private BigDecimal yearOut = new BigDecimal(0);
    private Vector<Kontostand> kontostaende = new Vector<Kontostand>();

    public KontoCollection(Integer y, Konto k) {
        this.year = y;
        this.konto = k;
    }

    public Konto getKonto() {
        return konto;
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
            return getKonto().getSaldo().subtract(getYearIn());
        } else {
            return getYearOut().subtract(getYearIn());
        }
    }

    public Vector<Kontostand> getKontostaende() {
        return kontostaende;
    }

    public void addKontostand(Kontostand kontostand) {
        LocalDate ktostdat = kontostand.getKtostdat();
        if (ktostdat.getYear() == getYear()) {
            this.kontostaende.add(kontostand);
            if (Month.DECEMBER.equals(ktostdat.getMonth())) {
                this.yearOut = kontostand.getKtostsaldo();
            }
        } else if (ktostdat.getYear() == getYear() - 1 && Month.DECEMBER.equals(ktostdat.getMonth())) {
            this.yearIn = kontostand.getKtostsaldo();
        }
    }

}