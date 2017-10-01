package haushaltsbuch.util;

import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.dao.BenutzerDAO;
import haushaltsbuch.db.BuchungDB;
import haushaltsbuch.db.KontoDB;
import haushaltsbuch.db.VJBuchungDB;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;

public class Tools {
	
	private final static String expenses = "Ausgaben";
	private final static String income = "Einnahmen";
	private final static String normal = "Standard";
	private final static String household = "Haushalt";
	
	public static Date getMonthFirst(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	
	/**
	 * Returns the year as an integer for the given date.
	 * 
	 * @param Date
	 * @return Integer
	 */
	public static Integer getYear(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(GregorianCalendar.YEAR);
	}
	
	/**
	 * Returns the month as an integer for the given date.
	 * 
	 * @param Date
	 * @return Integer
	 */
	public static Integer getMonth(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(GregorianCalendar.MONTH);
	}

	public static String printTodayLong() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd.MM.yyyy");
		return dateFormat.format(new Date());
	}
	
	public static String printMonthDate(Date date) {
		return new SimpleDateFormat("MMMM yyyy", BenutzerDAO.instance().getUser().getFormat()).format(date);
	}

	public static String printDate(Date date) {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, BenutzerDAO.instance().getUser().getFormat()).format(date);
	}
	
	public static String printBigDecimal(BigDecimal bd) {
		NumberFormatter formatter = new NumberFormatter("#,###,##0.00", BenutzerDAO.instance().getUser().getFormat());
	    formatter.setFixedLengths(true, true);
	    formatter.setDecimalSeparatorAlwaysShown(true);
	    formatter.setValue(bd);
		return formatter.getDisplayString();
	}
	
	public static String printBigDecimalWithoutDigits(BigDecimal bd) {
		NumberFormatter formatter = new NumberFormatter("#,###,##0", BenutzerDAO.instance().getUser().getFormat());
	    formatter.setFixedLengths(true, true);
	    formatter.setDecimalSeparatorAlwaysShown(false);
	    formatter.setValue(bd);
		return formatter.getDisplayString();
	}
	
	public static String getKoartgrpkat(Kostenartgruppe k) {
		if (k.getKoartgrpkat().equals("A")) {
			return expenses;
		}
		else if (k.getKoartgrpkat().equals("E")) {
			return income;
		}
		else return null;
	}

	public static String getKoartgrpart(Kostenartgruppe k) {
		if (k.getKoartgrpart().equals("N")) {
			return normal;
		}
		else if (k.getKoartgrpart().equals("H")) {
			return household;
		}
		else return null;
	}
	
	public static Date getLastDate() {
		try {
			Date lastBookDate = new BuchungDB().getLastBookDate();
			Date bookMonth = new KontoDB().getBookMonth();
			GregorianCalendar gc = new GregorianCalendar();
			
			if (lastBookDate.before(bookMonth)) {
				gc.setTime(bookMonth);
			}
			else gc.setTime(lastBookDate);
			return gc.getTime();
		}
		catch (Exception e) {
			return new GregorianCalendar().getTime();
		}
	}
	
	public static Date getLastMonth() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(getLastDate());
		return new GregorianCalendar(gc.get(GregorianCalendar.YEAR), gc.get(GregorianCalendar.MONTH), 1).getTime();
	}
	
	public static Date getFirstDate() {
		GregorianCalendar gc = new GregorianCalendar();
		try {
			if (new VJBuchungDB().getFirstBookDate() != null) gc.setTime(new VJBuchungDB().getFirstBookDate());
			else if (new BuchungDB().getFirstBookDate() != null) gc.setTime(new BuchungDB().getFirstBookDate());
		}
		catch (Exception e) {
			return new GregorianCalendar().getTime();
		}
		return gc.getTime();
	}
	
	public static Date getFirstMonth() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(getFirstDate());
		return new GregorianCalendar(gc.get(GregorianCalendar.YEAR), gc.get(GregorianCalendar.MONTH), 1).getTime();		
	}
	
}