package haushaltsbuch.dao;

import java.util.HashMap;
import java.util.Vector;

import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.db.KostenartDB;

public class KostenartDAO {

	private KostenartDB db = new KostenartDB();
	private static KostenartDAO instance;
	private Vector<Kostenart> kostenartList;
	private HashMap<Integer, Kostenart> kostenartMap;

	private KostenartDAO() {
	}

	public static KostenartDAO instance() {
		if (instance == null) {
			instance = new KostenartDAO();
		}
		return instance;
	}

	public Kostenart getKostenart(Integer i) {
		return getMap().get(i);
	}

	public Vector<Kostenart> getAll() {
		try {
			if (kostenartList == null) {
				kostenartList = db.getAll();
			}
			return kostenartList;
		} catch (Exception e) {
			return new Vector<Kostenart>();
		}
	}

	public HashMap<Integer, Kostenart> getMap() {
		if (kostenartMap == null) {
			kostenartMap = new HashMap<Integer, Kostenart>();
			for (Kostenart k : getAll()) {
				kostenartMap.put(k.getKoart(), k);
			}
		}
		return kostenartMap;
	}

	public Boolean saveOrUpdate(Kostenart k) {
		try {
			if (k.getKoart() == null) {
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
		kostenartList = null;
		kostenartMap = null;
	}

}