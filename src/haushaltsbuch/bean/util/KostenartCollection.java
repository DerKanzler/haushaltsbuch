package haushaltsbuch.bean.util;

import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.bean.Kostenartsaldo;
import haushaltsbuch.util.Tools;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.Vector;

public class KostenartCollection {
	
	private Kostenart koart;
	private Integer year;
	private BigDecimal prevYear = new BigDecimal(0);
	private BigDecimal curYear = new BigDecimal(0);
	private Vector<Kostenartsaldo> koartsaldi = new Vector<Kostenartsaldo>();
	
	public KostenartCollection(Integer y, Kostenart k) {
		this.year = y;
		this.koart = k;
	}
	
	public Kostenart getKostenart() {
		return koart;
	}
	
	public Integer getYear() {
		return year;
	}
	
	public BigDecimal getPrevYear() {
		return prevYear;
	}
	
	public BigDecimal getCurYear() {
		return curYear;
	}
	
	public BigDecimal getDiff() {
		return getCurYear().subtract(getPrevYear());
	}
	
	public Vector<Kostenartsaldo> getKostenartsaldi() {
		return koartsaldi;
	}
	
	public void addKostenartsaldo(Kostenartsaldo koartsaldo) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(koartsaldo.getKoartsalddat());
		if (gc.get(GregorianCalendar.YEAR) == getYear()) {
			this.koartsaldi.add(koartsaldo);
			if (koartsaldo.getKoartjrsaldo().compareTo(this.curYear) > 0) this.curYear = koartsaldo.getKoartjrsaldo();
			if (Tools.getYear(Tools.getLastDate()).equals(getYear())) {
				if (koartsaldo.getKoartjrsaldovj().compareTo(this.prevYear) > 0) this.prevYear = koartsaldo.getKoartjrsaldovj();
			}
		}
		else if (gc.get(GregorianCalendar.YEAR) == getYear()-1 && gc.get(GregorianCalendar.MONTH) == GregorianCalendar.DECEMBER && !Tools.getYear(Tools.getLastDate()).equals(getYear())) {
			this.prevYear = koartsaldo.getKoartjrsaldo();
		}
	}

}