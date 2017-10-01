package haushaltsbuch.db;

import haushaltsbuch.bean.Kostenartgruppe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class KostenartgruppeDB {

	private final String select = "SELECT \"koartgrp#\", \"koartgrpbez\", \"koartgrpkat\", \"koartgrpart\" FROM \"tkoartgruppe\" ";
	private final String order = "ORDER BY \"koartgrpbez\" ASC ";
	
	public Kostenartgruppe getKostenartgruppe(Integer id) throws Exception {
		String where = "WHERE \"koartgrp#\" = ? ";
		
		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + where + order);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();		
		Kostenartgruppe koartgrp = new Kostenartgruppe();
		while (rs.next()) {
			koartgrp = resultSetToKostenartgruppe(rs);
		}
		ps.close();
		return koartgrp;
	}
	
	public Vector<Kostenartgruppe> getAll() throws Exception {
		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + order);
		ResultSet rs = ps.executeQuery();
		Vector<Kostenartgruppe> kostenartgruppen = new Vector<Kostenartgruppe>();
		while (rs.next()) {
			kostenartgruppen.addElement(resultSetToKostenartgruppe(rs));
		}
		ps.close();
		return kostenartgruppen;
	}
	
	public void save(Kostenartgruppe koartgrp) throws Exception {
		String insert = "INSERT INTO \"tkoartgruppe\" (\"koartgrpbez\", \"koartgrpkat\", \"koartgrpart\") ";
		String values = "VALUES (?, ?, ?) ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(insert + values);
		ps.setString(1, koartgrp.getKoartgrpbez());
		ps.setString(2, koartgrp.getKoartgrpkat());
		ps.setString(3, koartgrp.getKoartgrpart());
		ps.execute();
		ps.close();
	}
	
	public void update(Kostenartgruppe koartgrp) throws Exception {
		String update = "UPDATE \"tkoartgruppe\" ";
		String set = "SET \"koartgrpbez\" = ?, \"koartgrpart\" = ? ";
		String where = "WHERE \"koartgrp#\" = ? ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(update + set + where);
		ps.setString(1, koartgrp.getKoartgrpbez());
		ps.setString(2, koartgrp.getKoartgrpart());
		ps.setInt(3, koartgrp.getKoartgrp());
		ps.executeUpdate();
		ps.close();
	}
	
	private Kostenartgruppe resultSetToKostenartgruppe(ResultSet rs) throws Exception {
		Kostenartgruppe koartgrp = new Kostenartgruppe();
		koartgrp.setKoartgrp(rs.getInt("koartgrp#"));
		koartgrp.setKoartgrpbez(rs.getString("koartgrpbez"));
		koartgrp.setKoartgrpkat(rs.getString("koartgrpkat"));
		koartgrp.setKoartgrpart(rs.getString("koartgrpart"));
		return koartgrp;
	}
	
}