package haushaltsbuch.dao;

import haushaltsbuch.bean.Kontostand;
import haushaltsbuch.db.KontostandDB;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

public class KontostandDAO {
	
	private KontostandDB db = new KontostandDB();
	private static KontostandDAO logic;
	private HashMap<Date, Vector<Kontostand>> kontostandMap = new HashMap<Date, Vector<Kontostand>>();
	
	private KontostandDAO() {}
	
	public static KontostandDAO instance() {
		if (logic == null) {
			logic = new KontostandDAO();
		}
		return logic;
	}
	
	public Vector<Kontostand> getKontostaende(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.clear(GregorianCalendar.MILLISECOND);
		gc.clear(GregorianCalendar.SECOND);
		gc.clear(GregorianCalendar.MINUTE);
		gc.clear(GregorianCalendar.HOUR);
		gc.set(GregorianCalendar.DAY_OF_MONTH, 1);
		if (!kontostandMap.containsKey(gc.getTime())) {
			try {
				kontostandMap.put(gc.getTime(), db.getKontostaendeMonat(new java.sql.Date(gc.getTime().getTime())));
			}
			catch (Exception e) {}			
		}
		return kontostandMap.get(gc.getTime());		
	}
	
	public Boolean saveOrUpdate(Kontostand k) {
		try {
			if (k.getKtost() == null) {
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
			return false;
		}
	}

	public void clear() {
		kontostandMap = new HashMap<Date, Vector<Kontostand>>();
	}

}