package haushaltsbuch.logic;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.bean.Konto;
import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.bean.util.SearchBuchung;
import haushaltsbuch.dao.KontoDAO;
import haushaltsbuch.dao.KostenartDAO;
import haushaltsbuch.db.BuchungDB;
import haushaltsbuch.db.VJBuchungDB;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Vector;

public class LogicSuchen {

	private static LogicSuchen logic;
	
	private Vector<Buchung> data;
	
	private LogicSuchen() {}
	
	public static LogicSuchen instance() {
		if (logic == null) {
			logic = new LogicSuchen();
		}
		return logic;
	}
	
	public Vector<Buchung> search(SearchBuchung suchBuchung) throws Exception {
		try {
			data = new BuchungDB().search(suchBuchung);
			data.addAll(new VJBuchungDB().search(suchBuchung));
		} catch (SQLException sqle) {
			if (sqle.getErrorCode() == 90039) {
				throw new Exception("Der angegebene Betrag ist zu groß!");
			}
			else throw sqle;
		}
		return data;
	}
	
	public Vector<Konto> getKontenList() {
		return KontoDAO.instance().getAll();
	}
	
	public Vector<Kostenart> getKoartList() {
		return KostenartDAO.instance().getAll();
	}
	
	public BigDecimal getRevenueSum() {
		BigDecimal sum = new BigDecimal(0);
		sum.setScale(2);
		for (Buchung buchung: data) {
			if (buchung.getKoart() != null) {
				if (buchung.getKoart().getKoartgrp().getKoartgrpkat().equals("E")) {
					sum = sum.add(buchung.getBuchbetr());
				}
			}
		}
		return sum;
	}
	
	public BigDecimal getExpenseSum() {
		BigDecimal sum = new BigDecimal(0);
		sum.setScale(2);
		for (Buchung buchung: data) {
			if (buchung.getKoart() != null) {
				if (buchung.getKoart().getKoartgrp().getKoartgrpkat().equals("A")) {
					sum = sum.add(buchung.getBuchbetr());
				}
			}
		}
		return sum;
	}
	
	public BigDecimal getTotalSum() {
		return getRevenueSum().subtract(getExpenseSum());
	}
	
}