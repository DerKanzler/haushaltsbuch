package at.kanzler.haushaltsbuch.logic.admin;

import java.util.Vector;

import at.kanzler.haushaltsbuch.bean.Kostenartgruppe;
import at.kanzler.haushaltsbuch.dao.KostenartgruppeDAO;

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