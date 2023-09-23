package at.kanzler.haushaltsbuch.db;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import at.kanzler.haushaltsbuch.Haushaltsbuch;
import at.kanzler.haushaltsbuch.exceptions.ConfigurationException;
import at.kanzler.haushaltsbuch.exceptions.InstallationException;
import at.kanzler.haushaltsbuch.logic.LogicMain;

public class DBConfiguration {

    private String path, name;

    private void readConfiguration() throws ConfigurationException {
        try {
            path = Preferences.userNodeForPackage(Haushaltsbuch.class).node(LogicMain.instance().getProfile())
                    .get("path", "");
            if (path == null || path.isEmpty())
                throw new ConfigurationException();
            name = Preferences.userNodeForPackage(Haushaltsbuch.class).node(LogicMain.instance().getProfile())
                    .get("name", "");
            if (name == null || name.isEmpty())
                throw new ConfigurationException();
        } catch (NullPointerException npe) {
            throw new ConfigurationException();
        }
    }

    public void writeConfiguration(String file) {
        Integer index = file.lastIndexOf("\\");
        if (index == -1)
            index = file.lastIndexOf("/");
        path = file.substring(0, index + 1);
        name = file.substring(index + 1, file.length());
        name = name.replace(".h2.db", "");

        try {
            Preferences pref = Preferences.userNodeForPackage(Haushaltsbuch.class)
                    .node(LogicMain.instance().getProfile());
            pref.put("path", path);
            pref.put("name", name);
            pref.flush();
        } catch (BackingStoreException bse) {
            bse.printStackTrace();
        }

        DB.instance().close();
    }

    public String getDBPath() throws InstallationException, ConfigurationException {
        if (path == null)
            readConfiguration();
        return path;
    }

    public String getDBName() throws InstallationException, ConfigurationException {
        if (name == null)
            readConfiguration();
        return name;
    }

}