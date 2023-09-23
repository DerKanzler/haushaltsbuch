package at.kanzler.haushaltsbuch.logic;

import at.kanzler.haushaltsbuch.bean.Benutzer;
import at.kanzler.haushaltsbuch.dao.BenutzerDAO;
import at.kanzler.haushaltsbuch.exceptions.DatabaseException;
import at.kanzler.haushaltsbuch.exceptions.LoginException;

public class LogicLogin {

    private Integer failedLogins = 0;

    public Boolean login(String password) throws LoginException, DatabaseException {
        try {
            Benutzer b = BenutzerDAO.instance().getBenutzer();
            char[] pwd = password.toCharArray();
            if (pwd.length == b.getPasswort().length) {
                for (int i = 0; i < pwd.length; i++) {
                    if (pwd[i] != b.getPasswort()[i]) {
                        failedLogins++;
                        if (failedLogins > 2) {
                            throw new LoginException();
                        }
                        return false;
                    }
                }
                return true;
            } else {
                failedLogins++;
                if (failedLogins > 2) {
                    throw new LoginException();
                }
                return false;
            }
        } catch (RuntimeException re) {
            throw new DatabaseException();
        }
    }

}