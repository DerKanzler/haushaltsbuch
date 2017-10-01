package haushaltsbuch.dao;

import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.db.KostenartgruppeDB;

import java.util.HashMap;
import java.util.Vector;

public class KostenartgruppeDAO {
	
	private KostenartgruppeDB db = new KostenartgruppeDB();
	private static KostenartgruppeDAO logic;
	private Vector<Kostenartgruppe> koartgrpVector;
	private HashMap<Integer, Kostenartgruppe> koartgrpMap;
	
	private KostenartgruppeDAO() {}
	
	public static KostenartgruppeDAO instance() {
		if (logic == null) {
			logic = new KostenartgruppeDAO();
		}
		return logic;
	}
	
	public Kostenartgruppe getKostenartgruppe(Integer i) {
		return getMap().get(i);
	}
	
	public Vector<Kostenartgruppe> getAll() {
		try {
			if (koartgrpVector == null) {
				koartgrpVector = db.getAll();
			}			
			return koartgrpVector;
		}
		catch (Exception e) {
			return new Vector<Kostenartgruppe>();
		}
	}
	
	public HashMap<Integer, Kostenartgruppe> getMap() {
		if (koartgrpMap == null) {
			koartgrpMap = new HashMap<Integer, Kostenartgruppe>();
			for (Kostenartgruppe k: getAll()) {
				koartgrpMap.put(k.getKoartgrp(), k);
			}
		}			
		return koartgrpMap;
	}
	
	public Boolean saveOrUpdate(Kostenartgruppe k) throws RuntimeException {
		try {
			if (k.getKoartgrp() == null) {
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
		koartgrpVector = null;
		koartgrpMap = null;
		KostenartDAO.instance().clear();
	}

}