package at.kanzler.haushaltsbuch.logic.admin;

import java.util.Vector;

import at.kanzler.haushaltsbuch.bean.Kostenart;
import at.kanzler.haushaltsbuch.dao.KostenartDAO;

public class LogicKoartAdmin {

    private static LogicKoartAdmin logic;

    private LogicKoartAdmin() {}

    public static LogicKoartAdmin instance() {
        if (logic == null) {
            logic = new LogicKoartAdmin();
        }
        return logic;
    }

    public Vector<Kostenart> getAll() {
        return KostenartDAO.instance().getAll();
    }

}
