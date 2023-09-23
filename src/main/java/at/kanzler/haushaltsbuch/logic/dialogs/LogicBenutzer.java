package at.kanzler.haushaltsbuch.logic.dialogs;

import at.kanzler.haushaltsbuch.bean.Benutzer;
import at.kanzler.haushaltsbuch.db.BenutzerDB;

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