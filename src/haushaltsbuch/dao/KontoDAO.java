package haushaltsbuch.dao;

import haushaltsbuch.bean.Konto;
import haushaltsbuch.db.KontoDB;

import java.util.HashMap;
import java.util.Vector;

public class KontoDAO{
	
	private KontoDB db = new KontoDB();
	private static KontoDAO logic;
	private Vector<Konto> kontenVector;
	private HashMap<Integer, Konto> kontenMap;
	
	private KontoDAO() {}
	
	public static KontoDAO instance() {
		if (logic == null) {
			logic = new KontoDAO();
		}
		return logic;
	}
	
	public Konto getKonto(Integer i) {
		return getMap().get(i);
	}
	
	public Vector<Konto> getAll() {
		try {
			if (kontenVector == null) {
				kontenVector = db.getAll();
			}			
			return kontenVector;
		}
		catch (Exception e) {
			return new Vector<Konto>();
		}
	}
	
	public Vector<Konto> getAllValid() {
		Vector<Konto> konten = new Vector<Konto>();
		for (Konto k: getAll()) {
			if (k.isValid()) {
				konten.addElement(k);
			}
		}
		return konten;
	}
	
	public HashMap<Integer, Konto> getMap() {
		if (kontenMap == null) {
			kontenMap = new HashMap<Integer, Konto>();
			for (Konto k: getAll()) {
				kontenMap.put(k.getKonto(), k);
			}
		}			
		return kontenMap;
	}
	
	public Boolean saveOrUpdate(Konto k) throws RuntimeException {
		try {
			if (k.getKonto() == null) {
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
		kontenVector = null;
		kontenMap = null;
		BenutzerDAO.instance().clear();
	}

}