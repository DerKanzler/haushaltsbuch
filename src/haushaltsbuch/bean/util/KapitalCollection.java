package haushaltsbuch.bean.util;

import haushaltsbuch.bean.Konto;
import haushaltsbuch.bean.Kontostand;
import haushaltsbuch.gui.dialogs.UIKapitalJahrDialog;
import haushaltsbuch.logic.LogicBuchen;
import haushaltsbuch.util.Tools;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class KapitalCollection {
	
	private Integer type;
	private Integer year;
	private BigDecimal current = new BigDecimal(0);
	private BigDecimal currentYear = new BigDecimal(0);
	private BigDecimal yearIn = new BigDecimal(0);
	private BigDecimal yearOut = new BigDecimal(0);
	private SortedMap<Date, BigDecimal> values = new TreeMap<Date, BigDecimal>();
	
	public KapitalCollection(Integer y, Map<Konto, KontoCollection> konten, Integer t) {
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
						for (Kontostand k: e.getValue().getKontostaende()) {
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
						for (Kontostand k: e.getValue().getKontostaende()) {
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
		    		for (Kontostand k: e.getValue().getKontostaende()) {
						if (values.get(k.getKtostdat()) == null) {
							values.put(k.getKtostdat(), new BigDecimal(0));
						}
						values.put(k.getKtostdat(), values.get(k.getKtostdat()).add(k.getKtostsaldo()));
					}
		    		break;
		    	case (UIKapitalJahrDialog.KAPITAL_ERSPARNIS):
		    		yearIn = yearIn.add(e.getValue().getYearIn());
		    		yearOut = yearOut.add(e.getValue().getYearOut());
		    		currentYear = currentYear.add(e.getKey().getSaldo());
		    		current = LogicBuchen.instance().getTotalSavings();
		    		
		    		for (int j=0; j<e.getValue().getKontostaende().size(); j++) {
		    			Kontostand k = e.getValue().getKontostaende().elementAt(j);
						if (values.get(k.getKtostdat()) == null) {
							values.put(k.getKtostdat(), new BigDecimal(0));
						}
						GregorianCalendar gc = new GregorianCalendar();
						gc.setTime(k.getKtostdat());
						if (gc.get(GregorianCalendar.MONTH) == GregorianCalendar.JANUARY) {
							values.put(k.getKtostdat(), values.get(k.getKtostdat()).add(k.getKtostsaldo().subtract(e.getValue().getYearIn())));
						}
						else {
							BigDecimal previousMonth = null;
							try {
								previousMonth = e.getValue().getKontostaende().elementAt(j-1).getKtostsaldo();
							}
							catch (ArrayIndexOutOfBoundsException aioobe) {
								previousMonth = new BigDecimal(0);
							}
							finally {
								BigDecimal saving = k.getKtostsaldo().subtract(previousMonth);
								values.put(k.getKtostdat(), values.get(k.getKtostdat()).add(saving));
							}
						}
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
		if (type.equals(UIKapitalJahrDialog.KAPITAL_ERSPARNIS)) {
			return null;
		}
		return yearIn;
	}
	
	public BigDecimal getYearOut() {
		if (type.equals(UIKapitalJahrDialog.KAPITAL_ERSPARNIS)) {
			return null;
		}
		return yearOut;
	}
	
	public BigDecimal getDiff() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(Tools.getLastDate());
		if (getYear().equals(gc.get(GregorianCalendar.YEAR))) {
			if (type.equals(UIKapitalJahrDialog.KAPITAL_ERSPARNIS)) {
				return currentYear.subtract(yearIn);
			}
			else return current.subtract(yearIn);
		}
		else return yearOut.subtract(yearIn);
	}
	
	public BigDecimal getCurrent() {
		return current;
	}

	public SortedMap<Date, BigDecimal> getValues() {
		return values;
	}

}