package haushaltsbuch.db;

import haushaltsbuch.bean.Konto;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Vector;

public class KontoDB {

	private final String select = "SELECT \"kuerzel\", \"abldat\", \"ktobez\", \"buchdat\", \"jreinsaldo\", \"ktotyp\", \"kzog\", \"saldo\", \"konto#\" FROM \"tkonto\" ";
	private final String order = "ORDER BY \"kzog\" DESC, \"abldat\" DESC, \"kuerzel\" ASC ";
	
	public Konto getKonto(Integer id) throws Exception {
		String where = "WHERE \"konto#\" = ? ";
		
		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + where + order);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();		
		Konto konto = new Konto();
		while (rs.next()) {
			konto = resultSetToKonto(rs);
		}
		ps.close();
		return konto;
	}
	
	public Vector<Konto> getAll() throws Exception {
		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + order);
		ResultSet rs = ps.executeQuery();
		Vector<Konto> konten = new Vector<Konto>();
		while (rs.next()) {
			konten.addElement(resultSetToKonto(rs));
		}
		ps.close();
		return konten;
	}
	
	public void save(Konto konto) throws Exception {
		String insert = "INSERT INTO \"tkonto\" (\"kuerzel\", \"abldat\", \"ktobez\", \"buchdat\", \"jreinsaldo\", \"ktotyp\", \"kzog\", \"saldo\") ";
		String values = "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(insert + values);
		ps.setString(1, konto.getKuerzel());
		ps.setDate(2, new Date(konto.getAbldat().getTime()));
		ps.setString(3, konto.getKtobez());
		ps.setDate(4, new Date (konto.getBuchdat().getTime()));
		ps.setObject(5, konto.getJreinsaldo(), Types.DECIMAL);
		ps.setString(6, konto.getKtotyp());
		ps.setString(7, konto.getKzog());
		ps.setObject(8, konto.getSaldo(), Types.DECIMAL);
		ps.execute();
		ps.close();
	}
	
	public void update(Konto konto) throws Exception {
		String update = "UPDATE \"tkonto\" ";
		String set = "SET \"kuerzel\" = ?, \"abldat\" = ?, \"ktobez\" = ?, \"buchdat\" = ?, \"jreinsaldo\" = ?, \"ktotyp\" = ?, \"kzog\" = ?, \"saldo\" = ? ";
		String where = "WHERE \"konto#\" = ? ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(update + set + where);
		ps.setString(1, konto.getKuerzel());
		ps.setDate(2, new Date(konto.getAbldat().getTime()));
		ps.setString(3, konto.getKtobez());
		ps.setDate(4, new Date (konto.getBuchdat().getTime()));
		ps.setObject(5, konto.getJreinsaldo(), Types.DECIMAL);
		ps.setString(6, konto.getKtotyp());
		ps.setString(7, konto.getKzog());
		ps.setObject(8, konto.getSaldo(), Types.DECIMAL);
		ps.setInt(9, konto.getKonto());
		ps.executeUpdate();
		ps.close();
	}
	
	public Date getBookMonth() throws Exception {
		Date bookMonth = null;
		PreparedStatement ps = DB.instance().getConnection().prepareStatement("SELECT MAX(\"buchdat\") AS \"bookMonth\" FROM \"tkonto\"");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			bookMonth = rs.getDate("bookMonth");
		}
		ps.close();
		return bookMonth;
	}
	
	private Konto resultSetToKonto(ResultSet rs) throws Exception {
		Konto kontoData = new Konto();
		kontoData.setKonto(rs.getInt("konto#"));
		kontoData.setKuerzel(rs.getString("kuerzel"));
		kontoData.setAbldat(rs.getDate("abldat"));
		kontoData.setKtobez(rs.getString("ktobez"));
		kontoData.setBuchdat(rs.getDate("buchdat"));
		kontoData.setJreinsaldo(rs.getBigDecimal("jreinsaldo"));
		kontoData.setKtotyp(rs.getString("ktotyp"));
		kontoData.setKzog(rs.getString("kzog"));
		kontoData.setSaldo(rs.getBigDecimal("saldo"));
		return kontoData;
	}
	
}