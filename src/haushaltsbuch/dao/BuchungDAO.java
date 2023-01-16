package haushaltsbuch.dao;

import java.util.Vector;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.bean.util.SearchBuchung;
import haushaltsbuch.db.BuchungDB;

public class BuchungDAO {

    private BuchungDB db = new BuchungDB();
    private static BuchungDAO instance;
    private Vector<Buchung> buchungList;

    private BuchungDAO() {
        // nothing to do
    }

    public static BuchungDAO instance() {
        if (instance == null) {
            instance = new BuchungDAO();
        }
        return instance;
    }

    public Vector<Buchung> getAll() {
        try {
            if (buchungList == null) {
                buchungList = db.getAll();
            }
            return buchungList;
        } catch (Exception e) {
            return new Vector<Buchung>();
        }
    }

    public Boolean saveOrUpdate(Buchung b) {
        try {
            if (b.getBuchung() == null) {
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

    public Vector<Buchung> search(SearchBuchung sb) {
        try {
            return db.search(sb);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Boolean delete(Buchung b) throws RuntimeException {
        try {
            db.delete(b);
            clear();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Boolean deleteAll() throws RuntimeException {
        try {
            db.deleteAll();
            clear();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void clear() {
        buchungList = null;
    }

}