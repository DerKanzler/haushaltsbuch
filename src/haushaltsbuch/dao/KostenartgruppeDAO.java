package haushaltsbuch.dao;

import java.util.HashMap;
import java.util.Vector;

import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.db.KostenartgruppeDB;

public class KostenartgruppeDAO {

	private KostenartgruppeDB db = new KostenartgruppeDB();
	private static KostenartgruppeDAO instance;
	private Vector<Kostenartgruppe> koartgruppeList;
	private HashMap<Integer, Kostenartgruppe> koartgruppeMap;

	private KostenartgruppeDAO() {
		// nothing to do
	}

	public static KostenartgruppeDAO instance() {
		if (instance == null) {
			instance = new KostenartgruppeDAO();
		}
		return instance;
	}

	public Kostenartgruppe getKostenartgruppe(Integer i) {
		return getMap().get(i);
	}

	public Vector<Kostenartgruppe> getAll() {
		try {
			if (koartgruppeList == null) {
				koartgruppeList = db.getAll();
			}
			return koartgruppeList;
		} catch (Exception e) {
			return new Vector<Kostenartgruppe>();
		}
	}

	public HashMap<Integer, Kostenartgruppe> getMap() {
		if (koartgruppeMap == null) {
			koartgruppeMap = new HashMap<Integer, Kostenartgruppe>();
			for (Kostenartgruppe k : getAll()) {
				koartgruppeMap.put(k.getKoartgrp(), k);
			}
		}
		return koartgruppeMap;
	}

	public Boolean saveOrUpdate(Kostenartgruppe k) {
		try {
			if (k.getKoartgrp() == null) {
				db.save(k);
				clear();
			} else {
				db.update(k);
			}
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void clear() {
		koartgruppeList = null;
		koartgruppeMap = null;
		KostenartDAO.instance().clear();
	}

}