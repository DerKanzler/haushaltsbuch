package haushaltsbuch.dao;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.bean.util.SearchBuchung;
import haushaltsbuch.db.BuchungDB;

import java.util.Vector;

public class BuchungDAO {
	
	private BuchungDB db = new BuchungDB();
	private static BuchungDAO logic;
	private Vector<Buchung> buchungsVector;
	
	private BuchungDAO() {}
	
	public static BuchungDAO instance() {
		if (logic == null) {
			logic = new BuchungDAO();
		}
		return logic;
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

	public Boolean saveOrUpdate(Buchung b) throws RuntimeException {
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
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Vector<Buchung> search(SearchBuchung sb) throws RuntimeException {
		try {
			return db.search(sb);
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Boolean delete(Buchung b) throws RuntimeException {
		try {
			db.delete(b);
			return true;
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Boolean deleteAll() throws RuntimeException {
		try {
			db.deleteAll();
			return true;
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void clear() {
		buchungsVector = null;
	}
	
}