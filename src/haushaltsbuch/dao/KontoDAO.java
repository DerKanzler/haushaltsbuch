package haushaltsbuch.dao;

import java.util.HashMap;
import java.util.Vector;

import haushaltsbuch.bean.Konto;
import haushaltsbuch.db.KontoDB;

public class KontoDAO {

    private KontoDB db = new KontoDB();
    private static KontoDAO instance;
    private Vector<Konto> kontoList;
    private HashMap<Integer, Konto> kontoMap;

    private KontoDAO() {
        // nothing to do
    }

    public static KontoDAO instance() {
        if (instance == null) {
            instance = new KontoDAO();
        }
        return instance;
    }

    public Konto getKonto(Integer i) {
        return getMap().get(i);
    }

    public Vector<Konto> getAll() {
        try {
            if (kontoList == null) {
                kontoList = db.getAll();
            }
            return kontoList;
        } catch (Exception e) {
            return new Vector<>();
        }
    }

    public Vector<Konto> getAllValid() {
        Vector<Konto> konten = new Vector<>();
        Vector<Konto> originalKonten = getAll();
        for (Konto k : originalKonten) {
            if (k.isValid()) {
                konten.addElement(k);
            }
        }
        return konten;
    }

    public HashMap<Integer, Konto> getMap() {
        if (kontoMap == null) {
            kontoMap = new HashMap<>();
            Vector<Konto> originalKonten = getAll();
            for (Konto k : originalKonten) {
                kontoMap.put(k.getKonto(), k);
            }
        }
        return kontoMap;
    }

    public Boolean saveOrUpdate(Konto k) {
        try {
            if (k.getKonto() == null) {
                db.save(k);
            } else {
                db.update(k);
            }
            clear();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void clear() {
        kontoList = null;
        kontoMap = null;
        BenutzerDAO.instance().clear();
    }

}