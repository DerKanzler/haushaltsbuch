package haushaltsbuch.logic.admin;

import java.util.Vector;

import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.dao.KostenartgruppeDAO;

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