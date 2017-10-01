package haushaltsbuch.logic.dialogs;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.dao.BuchungDAO;
import haushaltsbuch.dao.VJBuchungDAO;
import haushaltsbuch.db.DB;
import haushaltsbuch.util.Tools;

import java.sql.SQLException;

import org.h2.jdbc.JdbcSQLException;

public class LogicBuchungAendern {
	
	public void editBookingText(Buchung b) throws Exception {
		try {
			if (b.getBuchtext().length() == 0) throw new Exception("Der Buchungstext darf nicht leer sein!");
			Integer bookingYear = Tools.getYear(Tools.getLastMonth());
			Integer year = Tools.getYear(b.getBuchdat());
			Integer compareValue = bookingYear.compareTo(year);
			switch (compareValue) {
			case (0):
				if (BuchungDAO.instance().saveOrUpdate(b)) {
					if (DB.instance().commit()) {
						break;
					}
					else throw new Exception("Die Änderung ist fehlgeschlagen!");
				}
				else throw new Exception("Die Änderung ist fehlgeschlagen!");
			case (1):
				if (VJBuchungDAO.instance().saveOrUpdate(b)) {
					if (DB.instance().commit()) {
						break;
					}
					else throw new Exception("Die Änderung ist fehlgeschlagen!");
				}
				else throw new Exception("Die Änderung ist fehlgeschlagen!");
			case (-1):
				throw new Exception("Die Änderung ist fehlgeschlagen!");
			}
		}
		catch (JdbcSQLException jsqle) {
			throw new Exception("Die Änderung ist fehlgeschlagen!");
		}
		catch (SQLException sqle) {
			throw new Exception("Die Änderung ist fehlgeschlagen!");
		}
		catch (RuntimeException re) {
			throw new Exception("Die Änderung ist fehlgeschlagen!");
		}
	}

}