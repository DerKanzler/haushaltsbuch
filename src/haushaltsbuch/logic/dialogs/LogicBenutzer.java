package haushaltsbuch.logic.dialogs;

import haushaltsbuch.bean.Benutzer;
import haushaltsbuch.db.BenutzerDB;

public class LogicBenutzer {

    private BenutzerDB benutzerDB = new BenutzerDB();

    public Boolean save(Benutzer b) throws RuntimeException {
        try {
            benutzerDB.save(b);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Benutzer konnte nicht gespeichert werden!");
        }
    }

}