package haushaltsbuch.logic.admin;

import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.dao.KostenartgruppeDAO;

import java.util.Vector;

public class LogicKoartgrpAdmin {
	
	private static LogicKoartgrpAdmin logic;
	
	private LogicKoartgrpAdmin() {};
	
	public static LogicKoartgrpAdmin instance() {
		if (logic == null) {
			logic = new LogicKoartgrpAdmin();
		}
		return logic;
	}
	
	public Vector<Kostenartgruppe> getAll() {
		return KostenartgruppeDAO.instance().getAll();
	}

}