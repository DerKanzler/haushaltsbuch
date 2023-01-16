package haushaltsbuch.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Vector;

import haushaltsbuch.bean.Kontostand;
import haushaltsbuch.db.KontostandDB;

public class KontostandDAO {

    private KontostandDB db = new KontostandDB();
    private static KontostandDAO instance;
    private HashMap<LocalDate, Vector<Kontostand>> kontostandMap = new HashMap<LocalDate, Vector<Kontostand>>();

    private KontostandDAO() {}

    public static KontostandDAO instance() {
        if (instance == null) {
            instance = new KontostandDAO();
        }
        return instance;
    }

    public Vector<Kontostand> getKontostaende(LocalDate date) {
        date = date.withDayOfMonth(1);
        if (kontostandMap.containsKey(date) == false) {
            try {
                kontostandMap.put(date, db.getKontostaendeMonat(date));
            } catch (Exception e) {
                // nothing to do
            }
        }
        return kontostandMap.get(date);
    }

    public Boolean saveOrUpdate(Kontostand k) {
        try {
            if (k.getKtost() == null) {
                db.save(k);
            } else {
                db.update(k);
            }
            clear();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clear() {
        kontostandMap = new HashMap<LocalDate, Vector<Kontostand>>();
    }

}