package haushaltsbuch.dao;

import java.util.Vector;

import haushaltsbuch.bean.Benutzer;
import haushaltsbuch.db.BenutzerDB;

public class BenutzerDAO {

	private BenutzerDB db = new BenutzerDB();
	private static BenutzerDAO logic;
	private Vector<Benutzer> benutzerVector;

	private BenutzerDAO() {
	}

	public static BenutzerDAO instance() {
		if (logic == null) {
			logic = new BenutzerDAO();
		}
		return logic;
	}

	public Benutzer getUser() {
		Benutzer b = getBenutzer();
		return b;
	}

	public Benutzer getBenutzer() {
		return getAll().lastElement();
	}

	public Vector<Benutzer> getAll() {
		try {
			if (benutzerVector == null) {
				benutzerVector = db.getAll();
			}
			return benutzerVector;
		} catch (Exception e) {
			return new Vector<Benutzer>();
		}
	}

	public Boolean saveOrUpdate(Benutzer b) throws RuntimeException {
		try {
			if (b.getBenutzer() == null) {
				db.save(b);
				return true;
			} else {
				db.update(b);
				return true;
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void clear() {
		benutzerVector = null;
	}

}