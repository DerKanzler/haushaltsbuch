package haushaltsbuch.bean.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Vector;

import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.bean.Kostenartsaldo;
import haushaltsbuch.util.Tools;

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
		LocalDate koartsalddat = koartsaldo.getKoartsalddat();
		if (koartsalddat.getYear() == getYear()) {
			this.koartsaldi.add(koartsaldo);
			if (koartsaldo.getKoartjrsaldo().compareTo(this.curYear) > 0) {
				this.curYear = koartsaldo.getKoartjrsaldo();
			}
			if (getYear().equals(Tools.getLastDate().getYear())
					&& koartsaldo.getKoartjrsaldovj().compareTo(this.prevYear) > 0) {
				this.prevYear = koartsaldo.getKoartjrsaldovj();
			}
		} else if (koartsalddat.getYear() == getYear() - 1 && Month.DECEMBER.equals(koartsalddat.getMonth())
				&& getYear().equals(Tools.getLastDate().getYear()) == false) {
			this.prevYear = koartsaldo.getKoartjrsaldo();
		}
	}

}