package haushaltsbuch.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Vector;

import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.bean.Kostenartsaldo;
import haushaltsbuch.db.KostenartsaldoDB;

public class KostenartsaldoDAO {

    private KostenartsaldoDB db = new KostenartsaldoDB();
    private static KostenartsaldoDAO instance;
    private HashMap<LocalDate, Vector<Kostenartsaldo>> koartsaldoMap = new HashMap<>();

    private KostenartsaldoDAO() {
        // nothing to do
    }

    public static KostenartsaldoDAO instance() {
        if (instance == null) {
            instance = new KostenartsaldoDAO();
        }
        return instance;
    }

    public Vector<Kostenartsaldo> getKostenartsaldi(LocalDate date) {
        if (koartsaldoMap.containsKey(date) == false) {
            try {
                Kostenartsaldo ks = new Kostenartsaldo();
                ks.setKoartsalddat(date);
                koartsaldoMap.put(date, db.find(ks));
            } catch (Exception e) {
                // nothing to do
            }
        }
        return koartsaldoMap.get(date);
    }

    public Boolean saveOrUpdate(Kostenartsaldo ks) {
        try {
            if (ks.getKoartsald() == null) {
                db.save(ks);
                Vector<Kostenartsaldo> koartsaldi;
                if (koartsaldoMap.containsKey(ks.getKoartsalddat())) {
                    koartsaldi = koartsaldoMap.get(ks.getKoartsalddat());
                } else {
                    koartsaldi = new Vector<>();
                }
                koartsaldi.add(ks);
                koartsaldoMap.put(ks.getKoartsalddat(), koartsaldi);
            } else {
                db.update(ks);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Kostenartsaldo find(Kostenart koart, LocalDate date) {
        try {
            Vector<Kostenartsaldo> kostenartsaldi = getKostenartsaldi(date);
            for (Kostenartsaldo ks : kostenartsaldi) {
                if (koart.equals(ks.getKoart())) {
                    return ks;
                }
            }
            return new Kostenartsaldo();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void clear() {
        koartsaldoMap = new HashMap<>();
    }

}
