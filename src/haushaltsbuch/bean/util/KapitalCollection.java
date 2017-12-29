package haushaltsbuch.bean.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import haushaltsbuch.bean.Konto;
import haushaltsbuch.bean.Kontostand;
import haushaltsbuch.gui.dialogs.UIKapitalJahrDialog;
import haushaltsbuch.util.Tools;

public class KapitalCollection {

	private Integer type;
	private Integer year;
	private BigDecimal current = BigDecimal.ZERO;
	private BigDecimal yearIn = BigDecimal.ZERO;
	private BigDecimal yearOut = BigDecimal.ZERO;
	private SortedMap<LocalDate, BigDecimal> values = new TreeMap<LocalDate, BigDecimal>();

	public KapitalCollection(Integer y, Map<Konto, KontoCollection> konten, Integer t) {
		this.year = y;
		this.type = t;

		Iterator<Map.Entry<Konto, KontoCollection>> i = konten.entrySet().iterator();
		while (i.hasNext()) {
			Entry<Konto, KontoCollection> e = i.next();
			Konto konto = e.getKey();
			switch (type) {
			case (UIKapitalJahrDialog.KAPITAL_VERFUEGBAR):
				if (Boolean.TRUE.equals(konto.isDisposable())) {
					calculateValues(konto, e.getValue());
				}
				break;
			case (UIKapitalJahrDialog.KAPITAL_GESPERRT):
				if (Boolean.TRUE.equals(konto.isDisposable()) == false) {
					calculateValues(konto, e.getValue());
				}
				break;
			case (UIKapitalJahrDialog.KAPITAL_GESAMT):
				calculateValues(konto, e.getValue());
				break;
			}
		}
	}

	private void calculateValues(Konto konto, KontoCollection collection) {
		yearIn = yearIn.add(collection.getYearIn());
		yearOut = yearOut.add(collection.getYearOut());
		current = current.add(konto.getSaldo());

		for (Kontostand k : collection.getKontostaende()) {
			if (values.get(k.getKtostdat()) == null) {
				values.put(k.getKtostdat(), BigDecimal.ZERO);
			}
			values.put(k.getKtostdat(), values.get(k.getKtostdat()).add(k.getKtostsaldo()));
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