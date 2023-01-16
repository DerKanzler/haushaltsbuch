package haushaltsbuch.logic.admin.composites;

import java.util.Vector;

import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.dao.KostenartDAO;
import haushaltsbuch.dao.KostenartgruppeDAO;
import haushaltsbuch.db.DB;

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
