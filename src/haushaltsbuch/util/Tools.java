package haushaltsbuch.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;

import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.dao.BenutzerDAO;
import haushaltsbuch.db.BuchungDB;
import haushaltsbuch.db.KontoDB;
import haushaltsbuch.db.VJBuchungDB;

public class Tools {

	private final static String expenses = "Ausgaben";
	private final static String income = "Einnahmen";
	private final static String normal = "Standard";
	private final static String household = "Haushalt";

	public static String printTodayLong() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy");
		return formatter.format(LocalDate.now());
	}

	public static String printYearDate(LocalDate date) {
		return DateTimeFormatter.ofPattern("yyyy", BenutzerDAO.instance().getUser().getFormat()).format(date);
	}

	public static String printMonthDate(LocalDate date) {
		return DateTimeFormatter.ofPattern("MMMM yyyy", BenutzerDAO.instance().getUser().getFormat()).format(date);
	}

	public static String printDate(LocalDate date) {
		return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
				.withLocale(BenutzerDAO.instance().getUser().getFormat()).format(date);
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
		} else if (k.getKoartgrpkat().equals("E")) {
			return income;
		}
		return null;
	}

	public static String getKoartgrpart(Kostenartgruppe k) {
		if (k.getKoartgrpart().equals("N")) {
			return normal;
		} else if (k.getKoartgrpart().equals("H")) {
			return household;
		}
		return null;
	}

	public static LocalDate toLocalDate(Date date) {
		return new java.sql.Date(date.getTime()).toLocalDate();
	}

	public static Date toDate(LocalDate date) {
		return java.sql.Date.valueOf(date);
	}

	public static LocalDate getLastDate() {
		try {
			LocalDate lastBookDate = new BuchungDB().getLastBookDate();
			LocalDate bookMonth = new KontoDB().getBookMonth();

			if (lastBookDate.isBefore(bookMonth)) {
				return bookMonth;
			} else {
				return lastBookDate;
			}
		} catch (Exception e) {
			return LocalDate.now();
		}
	}

	public static LocalDate getLastMonth() {
		LocalDate lastDate = getLastDate();
		return lastDate.withDayOfMonth(1);
	}

	public static LocalDate getFirstDate() {
		try {
			if (new VJBuchungDB().getFirstBookDate() != null) {
				return new VJBuchungDB().getFirstBookDate();
			} else if (new BuchungDB().getFirstBookDate() != null) {
				return new BuchungDB().getFirstBookDate();
			}
		} catch (Exception e) {
			return LocalDate.now();
		}
		return LocalDate.now();
	}

	public static LocalDate getFirstMonth() {
		LocalDate firstDate = getFirstDate();
		return firstDate.withDayOfMonth(1);
	}

}