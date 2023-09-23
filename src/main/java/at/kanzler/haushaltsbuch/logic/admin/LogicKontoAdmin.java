package at.kanzler.haushaltsbuch.logic.admin;

import java.util.Vector;

import at.kanzler.haushaltsbuch.bean.Konto;
import at.kanzler.haushaltsbuch.dao.KontoDAO;

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