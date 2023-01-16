package haushaltsbuch.logic.admin;

import java.util.Vector;

import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.dao.KostenartDAO;

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