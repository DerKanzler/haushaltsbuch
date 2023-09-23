package at.kanzler.haushaltsbuch.dao;

import java.util.Vector;

import at.kanzler.haushaltsbuch.bean.Benutzer;
import at.kanzler.haushaltsbuch.db.BenutzerDB;

public class BenutzerDAO {

    private BenutzerDB db = new BenutzerDB();
    private static BenutzerDAO instance;
    private Vector<Benutzer> benutzerList;

    private BenutzerDAO() {
        // nothing to do
    }

    public static BenutzerDAO instance() {
        if (instance == null) {
            instance = new BenutzerDAO();
        }
        return instance;
    }

    public Benutzer getUser() {
        return getBenutzer();
    }

    public Benutzer getBenutzer() {
        return getAll().lastElement();
    }

    public Vector<Benutzer> getAll() {
        try {
            if (benutzerList == null) {
                benutzerList = db.getAll();
            }
            return benutzerList;
        } catch (Exception e) {
            return new Vector<>();
        }
    }

    public Boolean saveOrUpdate(Benutzer b) {
        try {
            if (b.getBenutzer() == null) {
                db.save(b);
            } else {
                db.update(b);
            }
            clear();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void clear() {
        benutzerList = null;
    }

}
