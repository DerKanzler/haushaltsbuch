package haushaltsbuch.logic;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.bean.Konto;
import haushaltsbuch.bean.Kontostand;
import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.bean.Kostenartsaldo;
import haushaltsbuch.bean.util.SaveBuchung;
import haushaltsbuch.bean.util.SearchBuchung;
import haushaltsbuch.dao.BuchungDAO;
import haushaltsbuch.dao.KontoDAO;
import haushaltsbuch.dao.KontostandDAO;
import haushaltsbuch.dao.KostenartDAO;
import haushaltsbuch.dao.KostenartsaldoDAO;
import haushaltsbuch.dao.VJBuchungDAO;
import haushaltsbuch.db.DB;
import haushaltsbuch.util.Tools;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

public class LogicBuchen {

	private static LogicBuchen logic;

	private LogicBuchen() {}

	public static LogicBuchen instance() {
		if (logic == null) {
			logic = new LogicBuchen();
		}
		return logic;
	}

	public Vector<Buchung> getMonthBookings() {
		try {
			SearchBuchung sb = new SearchBuchung();
			sb.setFromDate(Tools.getLastMonth());
			return BuchungDAO.instance().search(sb);
		} catch (Exception e) {
			return new Vector<Buchung>();
		}
	}

	public Vector<Konto> getKontenList() {
		Vector<Konto> konten = KontoDAO.instance().getAllValid();
		return konten;
	}

	public Vector<Kostenart> getKoartList() {
		Vector<Kostenart> kostenarten = KostenartDAO.instance().getAll();
		return kostenarten;
	}

	public BigDecimal getDisposableSum() {
		BigDecimal sum = new BigDecimal(0);
		for (Konto k : KontoDAO.instance().getAll()) {
			if (k.isValid() && k.isDisposable()) {
				sum = sum.add(k.getSaldo());
			}
		}
		return sum;
	}

	public BigDecimal getOffLimitSum() {
		BigDecimal sum = new BigDecimal(0);
		for (Konto k : KontoDAO.instance().getAll()) {
			if (k.isValid() && !k.isDisposable()) {
				sum = sum.add(k.getSaldo());
			}
		}
		return sum;
	}

	public BigDecimal getTotalAssets() {
		return getDisposableSum().add(getOffLimitSum());
	}

	public BigDecimal getTotalRevenues() {
		BigDecimal totalRevenues = new BigDecimal(0.00);
		for (Buchung b : getMonthBookings()) {
			if (b.getKoart() != null) {
				if (b.getKoart().getKoartgrp().getKoartgrpkat().equals("E")) {
					totalRevenues = totalRevenues.add(b.getBuchbetr());
				}
			}
		}
		return totalRevenues;
	}

	public BigDecimal getTotalExpenses() {
		BigDecimal totalExpenses = new BigDecimal(0.00);
		for (Buchung b : getMonthBookings()) {
			if (b.getKoart() != null) {
				if (b.getKoart().getKoartgrp().getKoartgrpkat().equals("A")) {
					totalExpenses = totalExpenses.add(b.getBuchbetr());
				}
			}
		}
		return totalExpenses;
	}

	public BigDecimal getTotalSavings() {
		return getTotalRevenues().subtract(getTotalExpenses());
	}

	public SaveBuchung save(Buchung b) throws RuntimeException {
		SaveBuchung sb = new SaveBuchung();
		if (check(b)) {
			try {
				sb = monthYearEndDecider(b, sb);
				BuchungDAO.instance().saveOrUpdate(b);
				saveKoart(b);
				saveKontovon(b);
				saveKontonach(b);
				if (DB.instance().commit()) {
					sb.setSuccess(true);
					return sb;
				} else {
					DB.instance().rollback();
					throw new RuntimeException("Speichern fehlgeschlagen!");
				}
			} catch (RuntimeException re) {
				DB.instance().rollback();
				throw new RuntimeException("Speichern fehlgeschlagen!");
			} finally {
				BuchungDAO.instance().clear();
			}
		} else {
			sb.setSuccess(false);
			return sb;
		}
	}

	public Boolean delete(Buchung b) throws RuntimeException {
		try {
			BuchungDAO.instance().delete(b);
			saveKoartDelete(b);
			saveKontovonDelete(b);
			saveKontonachDelete(b);
			if (DB.instance().commit()) {
				return true;
			} else {
				DB.instance().rollback();
				throw new RuntimeException("Löschen fehlgeschlagen!");
			}
		} catch (RuntimeException re) {
			DB.instance().rollback();
			throw new RuntimeException("Löschen fehlgeschlagen!");
		}
	}

	private Boolean check(Buchung b) throws RuntimeException {
		b.setEindat(new Date());
		if (b.getBuchdat() == null) {
			throw new RuntimeException("Das Datum darf nicht leer sein!");
		} else {
			GregorianCalendar bookDate = new GregorianCalendar();
			bookDate.setTime(b.getBuchdat());

			GregorianCalendar bookMonth = new GregorianCalendar();
			bookMonth.setTime(Tools.getLastMonth());

			GregorianCalendar currentDate = new GregorianCalendar();
			currentDate.setTime(new Date());
			currentDate.add(GregorianCalendar.DAY_OF_MONTH, 1);
			currentDate.set(GregorianCalendar.HOUR_OF_DAY, 0);
			currentDate.set(GregorianCalendar.MINUTE, 0);
			currentDate.set(GregorianCalendar.SECOND, 0);
			currentDate.set(GregorianCalendar.MILLISECOND, 0);

			if (bookDate.get(GregorianCalendar.YEAR) < bookMonth.get(GregorianCalendar.YEAR)) {
				throw new RuntimeException("Das Buchdatum liegt zu weit in der Vergangenheit!");
			}
			if ((bookDate.get(GregorianCalendar.MONTH) < bookMonth.get(GregorianCalendar.MONTH))
					&& bookDate.get(GregorianCalendar.YEAR) <= bookMonth.get(GregorianCalendar.YEAR)) {
				throw new RuntimeException("Das Buchdatum liegt zu weit in der Vergangenheit!");
			}
			if (bookDate.after(currentDate)) {
				throw new RuntimeException("Das Buchdatum liegt in der Zukunft!");
			}
		}
		if (b.getBuchtext() == null) {
			throw new RuntimeException("Der Buchungstext darf nicht leer sein!");
		}
		if (b.getBuchbetr() == null) {
			throw new RuntimeException("Der Betrag darf nicht leer sein!");
		} else if (b.getBuchbetr().compareTo(new BigDecimal(0)) <= 0) {
			throw new RuntimeException("Der Betrag muss größer als 0 sein!");
		}
		if (b.getKoart() == null && b.getKontovon() == null
				&& b.getKontonach() == null) {
			throw new RuntimeException("Kostenart, Konto von und Konto nach sind leer!");
		} else if (b.getKoart() != null) {
			if (b.getKoart().getKoartgrp().getKoartgrpkat().equals("E")) {
				if (b.getKontovon() != null && b.getKontonach() == null) {
					throw new RuntimeException("Bei dieser Kostenart muss Konto von leer sein und Konto nach ausgewählt sein!");
				} else if (b.getKontovon() != null && b.getKontonach() != null) {
					throw new RuntimeException("Bei dieser Kostenart muss Konto von leer sein!");
				} else if (b.getKontovon() == null && b.getKontonach() == null) {
					throw new RuntimeException("Bei dieser Kostenart muss Konto nach ausgewählt sein!");
				} else if (b.getKontovon() == null && b.getKontonach() != null) {
					return true;
				}
			} else if (b.getKoart().getKoartgrp().getKoartgrpkat().equals("A")) {
				if (b.getKontovon() != null && b.getKontonach() == null) {
					return true;
				} else if (b.getKontovon() != null && b.getKontonach() != null) {
					throw new RuntimeException("Bei dieser Kostenart muss Konto nach leer sein!");
				} else if (b.getKontovon() == null && b.getKontonach() == null) {
					throw new RuntimeException("Bei dieser Kostenart muss Konto von ausgewählt sein!");
				} else if (b.getKontovon() == null && b.getKontonach() != null) {
					throw new RuntimeException("Bei dieser Kostenart muss Konto von ausgewählt sein und Konto nach leer sein!");
				}
			}
		} else if (b.getKoart() == null) {
			if (b.getKontovon() != null && b.getKontonach() == null) {
				throw new RuntimeException("Bei leerer Kostenart müssen Konto von und Konto nach ausgewählt sein!");
			} else if (b.getKontovon() != null && b.getKontonach() != null) {
				if (b.getKontovon().equals(b.getKontonach())) {
					throw new RuntimeException("Konto von und Konto nach dürfen nicht gleich sein!");
				} else
					return true;
			} else if (b.getKontovon() == null && b.getKontonach() != null) {
				throw new RuntimeException("Bei leerer Kostenart müssen Konto von und Konto nach ausgewählt sein!");
			}
		}
		return false;
	}

	private SaveBuchung monthYearEndDecider(Buchung b, SaveBuchung sb)
			throws RuntimeException {
		GregorianCalendar bookDate = new GregorianCalendar();
		bookDate.setTime(b.getBuchdat());

		GregorianCalendar bookMonth = new GregorianCalendar();
		bookMonth.setTime(Tools.getLastDate());

		return monthYearEnd(bookDate, bookMonth, sb);
	}

	private SaveBuchung monthYearEnd(GregorianCalendar future,
			GregorianCalendar current, SaveBuchung sb) throws RuntimeException {
		future.set(GregorianCalendar.DAY_OF_MONTH, 1);
		current.set(GregorianCalendar.DAY_OF_MONTH, 1);
		if (future.get(GregorianCalendar.YEAR) > current.get(GregorianCalendar.YEAR)) {
			sb = monthEnd(current, sb);
			if (current.get(GregorianCalendar.MONTH) == GregorianCalendar.DECEMBER) {
				sb = yearEnd(current, sb);
			}
			current.set(GregorianCalendar.MONTH, current.get(GregorianCalendar.MONTH) + 1);
			monthYearEnd(future, current, sb);
		}
		if ((future.get(GregorianCalendar.MONTH) > current .get(GregorianCalendar.MONTH))
				&& future.get(GregorianCalendar.YEAR) == current.get(GregorianCalendar.YEAR)) {
			sb = monthEnd(current, sb);
			current.set(GregorianCalendar.MONTH, current.get(GregorianCalendar.MONTH) + 1);
			monthYearEnd(future, current, sb);
		}
		return sb;
	}

	private SaveBuchung yearEnd(GregorianCalendar gc, SaveBuchung sb)
			throws RuntimeException {
		try {
			for (Buchung b : BuchungDAO.instance().getAll()) {
				b.setBuchung(null);
				VJBuchungDAO.instance().saveOrUpdate(b);
			}
			BuchungDAO.instance().deleteAll();
			for (Konto k : KontoDAO.instance().getAll()) {
				k.setJreinsaldo(k.getSaldo());
				KontoDAO.instance().saveOrUpdate(k);
			}
			sb.addYearEnd(gc.getTime());
			return sb;
		} catch (RuntimeException re) {
			throw new RuntimeException("Jahresabschluss fehlgeschlagen!");
		}
	}

	private SaveBuchung monthEnd(GregorianCalendar gc, SaveBuchung sb)
			throws RuntimeException {
		try {
			// tkostenartsaldo befüllen, nur falls buchung im monat vorhanden
			// sind und dann über alle kostenarten
			if (getMonthBookings().size() > 0) {
				// Kostenartsaldo wird in tkostenart auf 0 gesetzt
				for (Kostenart k : KostenartDAO.instance().getAll()) {
					Kostenartsaldo ks = new Kostenartsaldo();
					ks.setKoart(k);
					ks.setKoartjrsaldo(getJrSaldo(k));
					ks.setKoartjrsaldovj(getJrSaldovj(k, gc));
					ks.setKoartmonsaldo(k.getKoartsaldo());
					ks.setKoartsalddat(gc.getTime());
					KostenartsaldoDAO.instance().saveOrUpdate(ks);
					
					k.setKoartsaldo(new BigDecimal(0));
					KostenartDAO.instance().saveOrUpdate(k);
				}
			}
			// tkontostand befüllen & tkonto: buchdat aktualisiern
			for (Konto k : KontoDAO.instance().getAll()) {
				Kontostand ks = new Kontostand();
				ks.setKonto(k);
				ks.setKtostdat(gc.getTime());
				ks.setKtostsaldo(k.getSaldo());
				KontostandDAO.instance().saveOrUpdate(ks);

				GregorianCalendar buchdatGC = new GregorianCalendar();
				buchdatGC.setTime(gc.getTime());
				buchdatGC.set(GregorianCalendar.MONTH, buchdatGC.get(GregorianCalendar.MONTH) + 1);
				k.setBuchdat(buchdatGC.getTime());
				KontoDAO.instance().saveOrUpdate(k);
			}
			sb.addMonthEnd(gc.getTime());
			return sb;
		} catch (RuntimeException re) {
			throw new RuntimeException("Monatsabschluss fehlgeschlagen!");
		}
	}

	private void saveKoart(Buchung b) throws RuntimeException {
		if (b.getKoart() != null) {
			b.getKoart().setKoartsaldo(b.getKoart().getKoartsaldo().add(b.getBuchbetr()));
			KostenartDAO.instance().saveOrUpdate(b.getKoart());
		}
	}

	private void saveKontovon(Buchung b) throws RuntimeException {
		if (b.getKontovon() != null) {
			b.getKontovon().setSaldo(b.getKontovon().getSaldo().subtract(b.getBuchbetr()));
			KontoDAO.instance().saveOrUpdate(b.getKontovon());
		}
	}

	private void saveKontonach(Buchung b) throws RuntimeException {
		if (b.getKontonach() != null) {
			b.getKontonach().setSaldo(b.getKontonach().getSaldo().add(b.getBuchbetr()));
			KontoDAO.instance().saveOrUpdate(b.getKontonach());
		}
	}

	private void saveKoartDelete(Buchung b) throws RuntimeException {
		if (b.getKoart() != null) {
			b.getKoart().setKoartsaldo(b.getKoart().getKoartsaldo().subtract(b.getBuchbetr()));
			KostenartDAO.instance().saveOrUpdate(b.getKoart());
		}
	}

	private void saveKontovonDelete(Buchung b) throws RuntimeException {
		if (b.getKontovon() != null) {
			b.getKontovon().setSaldo(b.getKontovon().getSaldo().add(b.getBuchbetr()));
			KontoDAO.instance().saveOrUpdate(b.getKontovon());
		}
	}

	private void saveKontonachDelete(Buchung b) throws RuntimeException {
		if (b.getKontonach() != null) {
			b.getKontonach().setSaldo(b.getKontonach().getSaldo().subtract(b.getBuchbetr()));
			KontoDAO.instance().saveOrUpdate(b.getKontonach());
		}
	}

	private BigDecimal getJrSaldo(Kostenart koart) {
		BigDecimal jrSaldo = BigDecimal.ZERO;
		for (Buchung b : BuchungDAO.instance().getAll()) {
			if (b.getKoart() != null) {
				if (b.getKoart().equals(koart)) {
					jrSaldo = jrSaldo.add(b.getBuchbetr());
				}
			}
		}
		return jrSaldo;
	}

	private BigDecimal getJrSaldovj(Kostenart koart, GregorianCalendar gc) {
		BigDecimal jrSaldovj = new BigDecimal(0);
		GregorianCalendar ksGC = new GregorianCalendar();
		ksGC.setTime(gc.getTime());
		ksGC.set(GregorianCalendar.YEAR, ksGC.get(GregorianCalendar.YEAR) - 1);
		Kostenartsaldo ks = KostenartsaldoDAO.instance().find(koart, ksGC);
		if (ks.getKoartjrsaldo() != null)
			jrSaldovj = ks.getKoartjrsaldo();
		return jrSaldovj;
	}

}