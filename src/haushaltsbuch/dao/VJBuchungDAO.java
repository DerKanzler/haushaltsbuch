package haushaltsbuch.dao;

import java.util.HashMap;
import java.util.Vector;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.db.VJBuchungDB;

public class VJBuchungDAO {

    private VJBuchungDB db = new VJBuchungDB();
    private static VJBuchungDAO instance;
    private Vector<Buchung> buchungList;
    private HashMap<Integer, Buchung> buchungMap;

    private VJBuchungDAO() {
        // nothing to do
    }

    public static VJBuchungDAO instance() {
        if (instance == null) {
            instance = new VJBuchungDAO();
        }
        return instance;
    }

    public Buchung getBuchung(Integer i) {
        return getMap().get(i);
    }

    public Vector<Buchung> getAll() {
        try {
            if (buchungList == null) {
                buchungList = db.getAll();
            }
            return buchungList;
        } catch (Exception e) {
            return new Vector<>();
        }
    }

    public HashMap<Integer, Buchung> getMap() {
        if (buchungMap == null) {
            buchungMap = new HashMap<>();
            Vector<Buchung> buchungen = getAll();
            for (Buchung b : buchungen) {
                buchungMap.put(b.getBuchung(), b);
            }
        }
        return buchungMap;
    }

    public Boolean saveOrUpdate(Buchung b) {
        try {
            if (b.getBuchung() == null) {
                db.save(b);
                clear();
            } else {
                db.update(b);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void clear() {
        buchungList = null;
        buchungMap = null;
    }

}
