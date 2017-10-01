package haushaltsbuch.bean.util;

import haushaltsbuch.bean.Konto;
import haushaltsbuch.bean.Kontostand;
import haushaltsbuch.util.Tools;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.Vector;

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
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(Tools.getLastDate());
		if (getYear().equals(gc.get(GregorianCalendar.YEAR))) return getKonto().getSaldo().subtract(getYearIn());
		else return getYearOut().subtract(getYearIn());
	}
	
	public Vector<Kontostand> getKontostaende() {
		return kontostaende;
	}
	
	public void addKontostand(Kontostand kontostand) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(kontostand.getKtostdat());
		if (gc.get(GregorianCalendar.YEAR) == getYear()) {
			this.kontostaende.add(kontostand);
			if (gc.get(GregorianCalendar.MONTH) == GregorianCalendar.DECEMBER) this.yearOut = kontostand.getKtostsaldo();
		}
		else if (gc.get(GregorianCalendar.YEAR) == getYear()-1 && gc.get(GregorianCalendar.MONTH) == GregorianCalendar.DECEMBER) {
			this.yearIn = kontostand.getKtostsaldo();
		}
	}

}