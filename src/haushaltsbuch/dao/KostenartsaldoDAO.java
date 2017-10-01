package haushaltsbuch.dao;

import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.bean.Kostenartsaldo;
import haushaltsbuch.db.KostenartsaldoDB;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

public class KostenartsaldoDAO {
	
	private KostenartsaldoDB db = new KostenartsaldoDB();
	private static KostenartsaldoDAO logic;
	private HashMap<Date, Vector<Kostenartsaldo>> koartsaldoMap = new HashMap<Date, Vector<Kostenartsaldo>>();
	
	private KostenartsaldoDAO() {}
	
	public static KostenartsaldoDAO instance() {
		if (logic == null) {
			logic = new KostenartsaldoDAO();
		}
		return logic;
	}
	
	public Vector<Kostenartsaldo> getKostenartsaldi(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.clear(GregorianCalendar.MILLISECOND);
		gc.clear(GregorianCalendar.SECOND);
		gc.clear(GregorianCalendar.MINUTE);
		gc.clear(GregorianCalendar.HOUR);
		gc.set(GregorianCalendar.DAY_OF_MONTH, 1);
		
		if (!koartsaldoMap.containsKey(gc.getTime())) {
			try {
				Kostenartsaldo ks = new Kostenartsaldo();
				ks.setKoartsalddat(gc.getTime());
				koartsaldoMap.put(gc.getTime(), db.find(ks));
			}
			catch (Exception e) {}			
		}
		return koartsaldoMap.get(gc.getTime());
	}
	
	public Boolean saveOrUpdate(Kostenartsaldo ks) throws RuntimeException {
		try {
			if (ks.getKoartsald() == null) {
				db.save(ks);
				Vector<Kostenartsaldo> koartsaldi;
				if (koartsaldoMap.containsKey(ks.getKoartsalddat())) {
					koartsaldi = koartsaldoMap.get(ks.getKoartsalddat());
				}
				else koartsaldi = new Vector<Kostenartsaldo>();
				koartsaldi.add(ks);
				koartsaldoMap.put(ks.getKoartsalddat(), koartsaldi);
				return true;
			}
			else {
				db.update(ks);
				return true;
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Kostenartsaldo find(Kostenart koart, GregorianCalendar gc) throws RuntimeException {
		try {
			for (Kostenartsaldo ks: getKostenartsaldi(gc.getTime())) {
				if (koart.equals(ks.getKoart())) return ks;
			}
			return new Kostenartsaldo();
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void clear() {
		koartsaldoMap = new HashMap<Date, Vector<Kostenartsaldo>>();
	}

}