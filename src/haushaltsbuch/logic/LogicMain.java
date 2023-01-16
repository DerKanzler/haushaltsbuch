package haushaltsbuch.logic;

import org.h2.tools.Backup;
import org.h2.tools.Restore;

import haushaltsbuch.bean.Benutzer;
import haushaltsbuch.dao.BenutzerDAO;
import haushaltsbuch.dao.BuchungDAO;
import haushaltsbuch.dao.KontoDAO;
import haushaltsbuch.dao.KontostandDAO;
import haushaltsbuch.dao.KostenartDAO;
import haushaltsbuch.dao.KostenartgruppeDAO;
import haushaltsbuch.dao.KostenartsaldoDAO;
import haushaltsbuch.dao.VJBuchungDAO;
import haushaltsbuch.db.DB;
import haushaltsbuch.db.DBConfiguration;
import haushaltsbuch.exceptions.ConfigurationException;
import haushaltsbuch.exceptions.InstallationException;

public class LogicMain {

    private static LogicMain logic = null;
    private String profile = "";

    private LogicMain() {}

    public static LogicMain instance() {
        if (logic == null) {
            logic = new LogicMain();
        }
        return logic;
    }

    public Boolean backupDatabase(String target) throws InstallationException, ConfigurationException {
        DBConfiguration dbconfig = new DBConfiguration();
        if (target != null) {
            try {
                DB.instance().close();
                Backup.execute(target, dbconfig.getDBPath(), dbconfig.getDBName(), true);
                clearDAO();
                return DB.instance().connect();
            } catch (Exception e) {
                clearDAO();
                DB.instance().connect();
                return false;
            }
        } else
            return false;
    }

    public Boolean restoreDatabase(String source) throws InstallationException, ConfigurationException {
        if (source != null) {
            DBConfiguration dbconfig = new DBConfiguration();
            try {
                DB.instance().close();
                Restore.execute(source, dbconfig.getDBPath(), dbconfig.getDBName());
                clearDAO();
                return DB.instance().connect();
            } catch (Exception e) {
                clearDAO();
                DB.instance().connect();
                return false;
            }
        } else
            return false;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Benutzer getUser() {
        Benutzer user = BenutzerDAO.instance().getUser();
        if (user == null) {
            return new Benutzer();
        } else
            return user;
    }

    public Boolean userAvailable() {
        return BenutzerDAO.instance().getAll().size() > 0;
    }

    public void exit() {
        DB.instance().close();
        System.exit(0);
    }

    private void clearDAO() {
        BuchungDAO.instance().clear();
        KontoDAO.instance().clear();
        KontostandDAO.instance().clear();
        KostenartDAO.instance().clear();
        KostenartgruppeDAO.instance().clear();
        KostenartsaldoDAO.instance().clear();
        VJBuchungDAO.instance().clear();
        BenutzerDAO.instance().clear();
    }

}