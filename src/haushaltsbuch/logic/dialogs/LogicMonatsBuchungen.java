package haushaltsbuch.logic.dialogs;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.bean.util.SearchBuchung;
import haushaltsbuch.db.BuchungDB;
import haushaltsbuch.db.VJBuchungDB;

import java.sql.SQLException;
import java.util.Vector;

public class LogicMonatsBuchungen {
	
	public Vector<Buchung> search(SearchBuchung suchBuchung) throws Exception {
		try {
			Vector<Buchung> data = new BuchungDB().search(suchBuchung);
			data.addAll(new VJBuchungDB().search(suchBuchung));
			return data;
		}
		catch (SQLException sqle) {
			if (sqle.getErrorCode() == 90039) {
				throw new Exception("Der angegebene Betrag ist zu groß!");
			}
			else throw new Exception("Ein unbekannter Fehler ist aufgetreten!");
		}		
	}

}