package at.kanzler.haushaltsbuch.logic.admin.composites;

import java.util.Vector;

import at.kanzler.haushaltsbuch.bean.Kostenart;
import at.kanzler.haushaltsbuch.bean.Kostenartgruppe;
import at.kanzler.haushaltsbuch.dao.KostenartDAO;
import at.kanzler.haushaltsbuch.dao.KostenartgruppeDAO;
import at.kanzler.haushaltsbuch.db.DB;

public class LogicKoartComposite {

    public Boolean save(Kostenart k) throws RuntimeException {
        try {
            if (KostenartDAO.instance().saveOrUpdate(k)) {
                if (DB.instance().commit()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (RuntimeException re) {
            throw new RuntimeException("Dieses Kostenartkürzel gibt es schon!");
        }
    }

    public Vector<Kostenartgruppe> getAllKoartgrp() {
        return KostenartgruppeDAO.instance().getAll();
    }

    public Vector<Kostenartgruppe> getCorrespondingKoartgrp(String s) {
        Vector<Kostenartgruppe> data = new Vector<>();
        Vector<Kostenartgruppe> kostenartenGruppen = KostenartgruppeDAO.instance().getAll();
        for (Kostenartgruppe k : kostenartenGruppen) {
            if (k.getKoartgrpkat().equals(s)) {
                data.add(k);
            }
        }
        return data;
    }

}
