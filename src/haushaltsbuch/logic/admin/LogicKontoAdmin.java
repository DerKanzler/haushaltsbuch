package haushaltsbuch.logic.admin;

import haushaltsbuch.bean.Konto;
import haushaltsbuch.dao.KontoDAO;

import java.util.Vector;

public class LogicKontoAdmin {
	
	private static LogicKontoAdmin logic;
	
	private LogicKontoAdmin() {};
	
	public static LogicKontoAdmin instance() {
		if (logic == null) {
			logic = new LogicKontoAdmin();
		}
		return logic;
	}

	public Vector<Konto> getAll() {
		return KontoDAO.instance().getAll();
	}
	
}