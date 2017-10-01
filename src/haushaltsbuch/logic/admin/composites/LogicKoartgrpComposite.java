package haushaltsbuch.logic.admin.composites;

import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.dao.KostenartgruppeDAO;
import haushaltsbuch.db.DB;

public class LogicKoartgrpComposite {
	
	private String expenses = "Ausgaben";
	private String income = "Einnahmen";
	private String normal = "Standard";
	private String household = "Haushalt";
	
	public Boolean save(Kostenartgruppe k) throws RuntimeException {
		try {
			if (KostenartgruppeDAO.instance().saveOrUpdate(k)) {
				if (DB.instance().commit()) {
					return true;
				}
				else return false;
			}
			else return false;
		}
		catch (RuntimeException re) {
			throw new RuntimeException("Diese Kostenartgruppenbezeichnung gibt es schon!");
		}
	}
	
	public String[] fillKoartgrpkatCombo() {
		String[] items = new String[2];
		items[0] = expenses;
		items[1] = income;
		return items;
	}
	
	public String getKoartgrpkat(Kostenartgruppe k) {
		if (k.getKoartgrpkat().equals("A")) {
			return expenses;
		}
		else if (k.getKoartgrpkat().equals("E")) {
			return income;
		}
		else return null;
	}
	
	public Integer setKoartgrpkatSelection(Kostenartgruppe k) {
		if (k.getKoartgrpkat().equals("A")) {
			return 0;
		}
		else if (k.getKoartgrpkat().equals("E")) {
			return 1;
		}
		else return null;
	}
	
	public String getKoartgrpkatSelection(Integer i) {
		if (i == 0) {
			return "A";
		}
		else if (i == 1) {
			return "E";
		}
		else return null;
	}
	
	public String[] fillKoartgrpartCombo() {
		String[] items = new String[2];
		items[0] = normal;
		items[1] = household;
		return items;
	}
	
	public String getKoartgrpart(Kostenartgruppe k) {
		if (k.getKoartgrpart().equals("N")) {
			return normal;
		}
		else if (k.getKoartgrpart().equals("H")) {
			return household;
		}
		else return null;
	}
	
	public Integer setKoartgrpartSelection(Kostenartgruppe k) {
		if (k.getKoartgrpart().equals("N")) {
			return 0;
		}
		else if (k.getKoartgrpart().equals("H")) {
			return 1;
		}
		else return null;
	}
	
	public String getKoartgrpartSelection(Integer i) {
		if (i == 0) {
			return "N";
		}
		else if (i == 1) {
			return "H";
		}
		else return null;
	}

}