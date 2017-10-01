package haushaltsbuch.dao;

import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.db.KostenartDB;

import java.util.HashMap;
import java.util.Vector;

public class KostenartDAO {
	
	private KostenartDB db = new KostenartDB();
	private static KostenartDAO logic;
	private Vector<Kostenart> koartVector;
	private HashMap<Integer, Kostenart> koartMap;
	
	private KostenartDAO() {}
	
	public static KostenartDAO instance() {
		if (logic == null) {
			logic = new KostenartDAO();
		}
		return logic;
	}
	
	public Kostenart getKostenart(Integer i) {
		return getMap().get(i);
	}
	
	public Vector<Kostenart> getAll() {
		try {
			if (koartVector == null) {
				koartVector = db.getAll();
			}			
			return koartVector;
		}
		catch (Exception e) {
			return new Vector<Kostenart>();
		}
	}
	
	public HashMap<Integer, Kostenart> getMap() {
		if (koartMap == null) {
			koartMap = new HashMap<Integer, Kostenart>();
			for (Kostenart k: getAll()) {
				koartMap.put(k.getKoart(), k);
			}
		}			
		return koartMap;
	}
	
	public Boolean saveOrUpdate(Kostenart k) throws RuntimeException {
		try {
			if (k.getKoart() == null) {
				db.save(k);
				clear();
				return true;
			}
			else {
				db.update(k);
				return true;
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void clear() {
		koartVector = null;
		koartMap = null;
	}

}