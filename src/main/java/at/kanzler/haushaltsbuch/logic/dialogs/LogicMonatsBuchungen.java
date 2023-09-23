package at.kanzler.haushaltsbuch.logic.dialogs;

import java.sql.SQLException;
import java.util.Vector;

import at.kanzler.haushaltsbuch.bean.Buchung;
import at.kanzler.haushaltsbuch.bean.util.SearchBuchung;
import at.kanzler.haushaltsbuch.db.BuchungDB;
import at.kanzler.haushaltsbuch.db.VJBuchungDB;

public class LogicMonatsBuchungen {

    public Vector<Buchung> search(SearchBuchung suchBuchung) throws Exception {
        try {
            Vector<Buchung> data = new BuchungDB().search(suchBuchung);
            data.addAll(new VJBuchungDB().search(suchBuchung));
            return data;
        } catch (SQLException sqle) {
            if (sqle.getErrorCode() == 90039) {
                throw new Exception("Der angegebene Betrag ist zu groß!");
            } else
                throw new Exception("Ein unbekannter Fehler ist aufgetreten!");
        }
    }

}