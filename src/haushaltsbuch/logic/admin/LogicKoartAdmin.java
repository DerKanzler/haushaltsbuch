package haushaltsbuch.logic.admin;

import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.dao.KostenartDAO;

import java.util.Vector;

public class LogicKoartAdmin {
	
	private static LogicKoartAdmin logic;
	
	private LogicKoartAdmin() {};
	
	public static LogicKoartAdmin instance() {
		if (logic == null) {
			logic = new LogicKoartAdmin();
		}
		return logic;
	}

	public Vector<Kostenart> getAll() {
		Vector<Kostenart> kostenarten = KostenartDAO.instance().getAll();
		return kostenarten;
	}

}