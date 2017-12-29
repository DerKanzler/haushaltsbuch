package haushaltsbuch.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Vector;

import haushaltsbuch.bean.Kontostand;
import haushaltsbuch.dao.KontoDAO;

public class KontostandDB {

	private final String select = "SELECT \"ktost#\", \"ktostdat\", \"ktostsaldo\", \"konto#\" FROM \"tkontostand\" ";
	private final String order = "ORDER BY \"ktost#\" ASC ";

	public Kontostand getKontostand(Integer id) throws Exception {
		String where = "WHERE \"ktost#\" = ? ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + where + order);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		Kontostand kontostand = new Kontostand();
		while (rs.next()) {
			kontostand = resultSetToKontostand(rs);
		}
		ps.close();
		return kontostand;
	}

	public Vector<Kontostand> getKontostaendeMonat(LocalDate date) throws Exception {
		String where = "WHERE \"ktostdat\" = ? ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + where + order);
		ps.setDate(1, Date.valueOf(date));
		ResultSet rs = ps.executeQuery();
		Vector<Kontostand> kontostaende = new Vector<Kontostand>();
		while (rs.next()) {
			Kontostand ktost = resultSetToKontostand(rs);
			// if (ktost.getKonto().getAbldat().after(ktost.getKtostdat()) ||
			// ktost.getKtostsaldo().compareTo(new BigDecimal(0)) != 0)
			// kontostaende.addElement(ktost);
			kontostaende.addElement(ktost);
		}
		ps.close();
		return kontostaende;
	}

	public Vector<Kontostand> getAll() throws Exception {
		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + order);
		ResultSet rs = ps.executeQuery();
		Vector<Kontostand> kontostaende = new Vector<Kontostand>();
		while (rs.next()) {
			kontostaende.addElement(resultSetToKontostand(rs));
		}
		ps.close();
		return kontostaende;
	}

	public void save(Kontostand kontostand) throws Exception {
		String insert = "INSERT INTO \"tkontostand\" (\"ktostdat\", \"ktostsaldo\", \"konto#\") ";
		String values = "VALUES (?, ?, ?) ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(insert + values);
		ps.setDate(1, Date.valueOf(kontostand.getKtostdat()));
		ps.setObject(2, kontostand.getKtostsaldo(), Types.DECIMAL);
		ps.setInt(3, kontostand.getKonto().getKonto());
		ps.execute();
		ps.close();
	}

	public void update(Kontostand kontostand) throws Exception {
		String update = "UPDATE \"tkontostand\" ";
		String set = "SET \"ktostdat\" = ?, \"ktostsaldo\" = ? ";
		String where = "WHERE \"ktost#\" = ? ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(update + set + where);
		ps.setDate(1, Date.valueOf(kontostand.getKtostdat()));
		ps.setObject(2, kontostand.getKtostsaldo(), Types.DECIMAL);
		ps.setInt(3, kontostand.getKtost());
		ps.executeUpdate();
		ps.close();
	}

	private Kontostand resultSetToKontostand(ResultSet rs) throws SQLException {
		Kontostand kontostand = new Kontostand();
		kontostand.setKtost(rs.getInt("ktost#"));
		kontostand.setKtostdat(rs.getDate("ktostdat").toLocalDate());
		kontostand.setKtostsaldo(rs.getBigDecimal("ktostsaldo"));
		kontostand.setKonto(KontoDAO.instance().getKonto(rs.getInt("konto#")));
		return kontostand;
	}

}