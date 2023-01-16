package haushaltsbuch.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Vector;

import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.dao.KostenartgruppeDAO;

public class KostenartDB {

    private final String select = "SELECT \"koart#\", \"koartkubez\", \"koartbez\", \"koartsaldo\", \"koartgrp#\" FROM \"tkostenart\" ";
    private final String order = "ORDER BY \"koartkubez\" ASC ";

    public Kostenart getKostenart(Integer id) throws Exception {
        String where = "WHERE \"koart#\" = ? ";

        PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + where + order);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Kostenart koart = new Kostenart();
        while (rs.next()) {
            koart = resultSetToKostenart(rs);
        }
        ps.close();
        return koart;
    }

    public Vector<Kostenart> getAll() throws Exception {
        PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + order);
        ResultSet rs = ps.executeQuery();
        Vector<Kostenart> kostenarten = new Vector<Kostenart>();
        while (rs.next()) {
            kostenarten.addElement(resultSetToKostenart(rs));
        }
        ps.close();
        return kostenarten;
    }

    public void save(Kostenart koart) throws Exception {
        String insert = "INSERT INTO \"tkostenart\" (\"koartkubez\", \"koartbez\", \"koartsaldo\", \"koartgrp#\" )";
        String values = "VALUES (?, ?, ?, ?) ";

        PreparedStatement ps = DB.instance().getConnection().prepareStatement(insert + values);
        ps.setString(1, koart.getKoartkubez());
        ps.setString(2, koart.getKoartbez());
        ps.setObject(3, koart.getKoartsaldo(), Types.DECIMAL);
        ps.setInt(4, koart.getKoartgrp().getKoartgrp());
        ps.execute();
        ps.close();
    }

    public void update(Kostenart koart) throws Exception {
        String update = "UPDATE \"tkostenart\" ";
        String set = "SET \"koartkubez\" = ?, \"koartbez\" = ?, \"koartsaldo\" = ?, \"koartgrp#\" = ? ";
        String where = "WHERE \"koart#\" = ? ";

        PreparedStatement ps = DB.instance().getConnection().prepareStatement(update + set + where);
        ps.setString(1, koart.getKoartkubez());
        ps.setString(2, koart.getKoartbez());
        ps.setObject(3, koart.getKoartsaldo(), Types.DECIMAL);
        ps.setInt(4, koart.getKoartgrp().getKoartgrp());
        ps.setInt(5, koart.getKoart());
        ps.executeUpdate();
        ps.close();
    }

    private Kostenart resultSetToKostenart(ResultSet rs) throws Exception {
        Kostenart kostenart = new Kostenart();
        kostenart.setKoart(rs.getInt("koart#"));
        kostenart.setKoartkubez(rs.getString("koartkubez"));
        kostenart.setKoartbez(rs.getString("koartbez"));
        kostenart.setKoartsaldo(rs.getBigDecimal("koartsaldo"));
        kostenart.setKoartgrp(KostenartgruppeDAO.instance().getKostenartgruppe(rs.getInt("koartgrp#")));
        return kostenart;
    }

}