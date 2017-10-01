package haushaltsbuch.dao;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.db.VJBuchungDB;

import java.util.HashMap;
import java.util.Vector;

public class VJBuchungDAO {
	
	private VJBuchungDB db = new VJBuchungDB();
	private static VJBuchungDAO logic;
	private Vector<Buchung> buchungsVector;
	private HashMap<Integer, Buchung> buchungsMap;
	
	private VJBuchungDAO() {}
	
	public static VJBuchungDAO instance() {
		if (logic == null) {
			logic = new VJBuchungDAO();
		}
		return logic;
	}
	
	public Buchung getBuchung(Integer i) {
		return getMap().get(i);
	}
	
	public Vector<Buchung> getAll() {
		try {
			if (buchungsVector == null) {
				buchungsVector = db.getAll();
			}			
			return buchungsVector;
		}
		catch (Exception e) {
			return new Vector<Buchung>();
		}
	}
	
	public HashMap<Integer, Buchung> getMap() {
		if (buchungsMap == null) {
			buchungsMap = new HashMap<Integer, Buchung>();
			for (Buchung b: getAll()) {
				buchungsMap.put(b.getBuchung(), b);
			}
		}			
		return buchungsMap;
	}
	
	public Boolean saveOrUpdate(Buchung b) {
		try {
			if (b.getBuchung() == null) {
				db.save(b);
				return true;
			}
			else {
				db.update(b);
				return true;
			}
		}
		catch (Exception e) {
			return false;
		}
	}

	public void clear() {
		buchungsVector = null;
		buchungsMap = null;
	}

}