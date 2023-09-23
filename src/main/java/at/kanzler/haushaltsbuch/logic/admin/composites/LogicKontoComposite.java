package at.kanzler.haushaltsbuch.logic.admin.composites;

import java.math.BigDecimal;

import at.kanzler.haushaltsbuch.bean.Konto;
import at.kanzler.haushaltsbuch.dao.KontoDAO;
import at.kanzler.haushaltsbuch.db.DB;

public class LogicKontoComposite {

    private String open = "Offen";
    private String ended = "Beendet";
    private String available = "Verfügbar";
    private String locked = "Gesperrt";

    public Boolean save(Konto k) throws RuntimeException {
        if (!k.isValid() && k.getSaldo().compareTo(new BigDecimal(0)) != 0) {
            k.setKzog("O");
            throw new RuntimeException("Das Konto hat einen Saldo ungleich 0. Es kann nicht beendet werden!");
        }
        try {
            if (KontoDAO.instance().saveOrUpdate(k)) {
                if (DB.instance().commit()) {
                    return true;
                } else
                    return false;
            } else
                return false;
        } catch (RuntimeException re) {
            throw new RuntimeException("Dieses Kontokürzel gibt es schon!");
        }
    }

    public String[] fillKzogCombo() {
        String[] items = new String[2];
        items[0] = open;
        items[1] = ended;
        return items;
    }

    public String getKzog(Konto k) {
        if (k.getKzog().equals("O")) {
            return open;
        } else if (k.getKzog().equals("G")) {
            return ended;
        } else
            return null;
    }

    public Integer setKzogSelection(Konto k) {
        if (k.getKzog().equals("O")) {
            return 0;
        } else if (k.getKzog().equals("G")) {
            return 1;
        } else
            return null;
    }

    public String getKzogSelection(Integer i) {
        if (i == 0) {
            return "O";
        } else if (i == 1) {
            return "G";
        } else
            return null;
    }

    public String[] fillKtotypCombo() {
        String[] items = new String[2];
        items[0] = available;
        items[1] = locked;
        return items;
    }

    public String getKtotyp(Konto k) {
        if (k.getKtotyp().equals("V")) {
            return available;
        } else if (k.getKtotyp().equals("G")) {
            return locked;
        } else
            return null;
    }

    public Integer setKtotypSelection(Konto k) {
        if (k.getKtotyp().equals("V")) {
            return 0;
        } else if (k.getKtotyp().equals("G")) {
            return 1;
        } else
            return null;
    }

    public String getKtotypSelection(Integer i) {
        if (i == 0) {
            return "V";
        } else if (i == 1) {
            return "G";
        } else
            return null;
    }

}