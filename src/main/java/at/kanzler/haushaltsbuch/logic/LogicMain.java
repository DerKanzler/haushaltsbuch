package at.kanzler.haushaltsbuch.logic;

import org.h2.tools.Backup;
import org.h2.tools.Restore;

import at.kanzler.haushaltsbuch.bean.Benutzer;
import at.kanzler.haushaltsbuch.dao.BenutzerDAO;
import at.kanzler.haushaltsbuch.dao.BuchungDAO;
import at.kanzler.haushaltsbuch.dao.KontoDAO;
import at.kanzler.haushaltsbuch.dao.KontostandDAO;
import at.kanzler.haushaltsbuch.dao.KostenartDAO;
import at.kanzler.haushaltsbuch.dao.KostenartgruppeDAO;
import at.kanzler.haushaltsbuch.dao.KostenartsaldoDAO;
import at.kanzler.haushaltsbuch.dao.VJBuchungDAO;
import at.kanzler.haushaltsbuch.db.DB;
import at.kanzler.haushaltsbuch.db.DBConfiguration;
import at.kanzler.haushaltsbuch.exceptions.ConfigurationException;
import at.kanzler.haushaltsbuch.exceptions.InstallationException;

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