package at.kanzler.haushaltsbuch.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import at.kanzler.haushaltsbuch.exceptions.ConfigurationException;
import at.kanzler.haushaltsbuch.exceptions.InstallationException;

public class DB {

    private static DB database = null;
    private Connection connection;
    private String driver = "org.h2.Driver";
    private String url = "jdbc:h2:file:";
    private String user = "dba";
    private String pwd = "sql";
    private Boolean connected = false;

    private DB() {}

    public static DB instance() {
        if (database == null) {
            database = new DB();
        }
        return database;
    }

    public Boolean connect() throws InstallationException, ConfigurationException {
        try {
            DBConfiguration dbconfig = new DBConfiguration();
            Class.forName(driver);
            connection = DriverManager.getConnection(
                    url.concat(dbconfig.getDBPath()).concat(dbconfig.getDBName()).concat(";IFEXISTS=TRUE"), user, pwd);
            connection.setAutoCommit(false);
            connected = true;
            System.out.println("DB Connection established");
            return connected;
        } catch (SQLException se) {
            connection = null;
            connected = false;
            if (se.getErrorCode() == 90013) {
                return createDatabase();
            }
            if (se.getErrorCode() == 90020) {
                System.out.println("Close the database sucker!");
            }
            return connected;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public Connection getConnection() throws InstallationException, ConfigurationException {
        if (!connected) {
            connect();
        }
        return connection;
    }

    public Boolean commit() {
        try {
            getConnection().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean rollback() {
        try {
            getConnection().rollback();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void close() {
        try {
            connection.commit();
            connection.close();
            connection = null;
            connected = false;
            System.out.println("DB Connection closed");
        } catch (Exception e) {
            System.out.println("Database wasn't open!");
        }
    }

    private Boolean createDatabase() throws InstallationException, ConfigurationException {
        try {
            InputStream createSQL = this.getClass().getClassLoader().getResourceAsStream("haushb.ddl");
            Scanner createScanner = new Scanner(createSQL);
            createScanner.useDelimiter(";");

            DBConfiguration dbconfig = new DBConfiguration();
            Class.forName(driver);
            connection = DriverManager.getConnection(url.concat(dbconfig.getDBPath()).concat(dbconfig.getDBName()),
                    user, pwd);
            connection.setAutoCommit(false);
            connected = true;

            while (createScanner.hasNext()) {
                Statement st = getConnection().createStatement();
                st.execute(createScanner.next());
                commit();
            }
            createScanner.close();

            return connected;
        } catch (ClassNotFoundException cnfe) {
            connected = false;
            return connected;
        } catch (SQLException se) {
            connected = false;
            return connected;
        }
    }

}