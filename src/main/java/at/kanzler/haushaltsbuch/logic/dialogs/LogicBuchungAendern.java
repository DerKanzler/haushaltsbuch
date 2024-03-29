package at.kanzler.haushaltsbuch.logic.dialogs;

import java.sql.SQLException;

import org.h2.jdbc.JdbcSQLException;

import at.kanzler.haushaltsbuch.bean.Buchung;
import at.kanzler.haushaltsbuch.dao.BuchungDAO;
import at.kanzler.haushaltsbuch.dao.VJBuchungDAO;
import at.kanzler.haushaltsbuch.db.DB;
import at.kanzler.haushaltsbuch.util.Tools;

public class LogicBuchungAendern {

    public void editBookingText(Buchung b) throws Exception {
        try {
            if (b.getBuchtext() == null || b.getBuchtext().length() == 0) {
                throw new Exception("Der Buchungstext darf nicht leer sein!");
            }
            Integer bookingYear = Tools.getLastMonth().getYear();
            Integer year = b.getBuchdat().getYear();
            Integer compareValue = bookingYear.compareTo(year);
            switch (compareValue) {
                case (0):
                    if (BuchungDAO.instance().saveOrUpdate(b)) {
                        if (DB.instance().commit()) {
                            break;
                        } else {
                            throw new Exception("Die Änderung ist fehlgeschlagen!");
                        }
                    } else {
                        throw new Exception("Die Änderung ist fehlgeschlagen!");
                    }
                case (1):
                    if (VJBuchungDAO.instance().saveOrUpdate(b)) {
                        if (DB.instance().commit()) {
                            break;
                        } else {
                            throw new Exception("Die Änderung ist fehlgeschlagen!");
                        }
                    } else {
                        throw new Exception("Die Änderung ist fehlgeschlagen!");
                    }
                case (-1):
                    throw new Exception("Die Änderung ist fehlgeschlagen!");
            }
        } catch (JdbcSQLException jsqle) {
            throw new Exception("Die Änderung ist fehlgeschlagen!");
        } catch (SQLException sqle) {
            throw new Exception("Die Änderung ist fehlgeschlagen!");
        } catch (RuntimeException re) {
            throw new Exception("Die Änderung ist fehlgeschlagen!");
        }
    }

}