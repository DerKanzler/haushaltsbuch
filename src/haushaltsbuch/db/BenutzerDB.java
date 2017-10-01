package haushaltsbuch.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Locale;
import java.util.Vector;

import haushaltsbuch.bean.Benutzer;
import haushaltsbuch.dao.KontoDAO;

public class BenutzerDB {

	private final String select = "SELECT \"benutzer#\", \"name\", TRIM(CHAR(0) FROM UTF8TOSTRING(DECRYPT('AES','07',\"passwort\"))) AS	\"passwort\", \"konto1#\", \"konto2#\", \"konto3#\", \"format\" FROM \"tbenutzer\" ";
	private final String order = "ORDER BY \"benutzer#\" ASC ";

	public Benutzer getBenutzer(Integer id) throws Exception {
		String where = "WHERE \"benutzer#\" = ? ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + where + order);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		Benutzer benutzer = new Benutzer();
		while (rs.next()) {
			benutzer = resultSetToBenutzer(rs);
		}
		ps.close();
		return benutzer;
	}

	public Vector<Benutzer> getAll() throws Exception {
		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + order);
		ResultSet rs = ps.executeQuery();
		Vector<Benutzer> benutzer = new Vector<Benutzer>();
		while (rs.next()) {
			benutzer.addElement(resultSetToBenutzer(rs));
		}
		ps.close();
		return benutzer;
	}

	public void save(Benutzer benutzer) throws Exception {
		String insert = "INSERT INTO \"tbenutzer\" (\"name\", \"passwort\", \"konto1#\", \"konto2#\", \"konto3#\", \"format\") ";
		String values = "VALUES (?, ENCRYPT('AES','07',STRINGTOUTF8(?)), ?, ?, ?, ?) ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(insert + values);
		ps.setString(1, benutzer.getName());
		StringBuffer sb = new StringBuffer();
		for (char c : benutzer.getPasswort()) {
			sb.append(c);
		}
		ps.setString(2, sb.toString());
		if (benutzer.getKonto1() == null) {
			ps.setNull(3, Types.NULL);
		} else
			ps.setInt(3, benutzer.getKonto1().getKonto());
		if (benutzer.getKonto2() == null) {
			ps.setNull(4, Types.NULL);
		} else
			ps.setInt(4, benutzer.getKonto2().getKonto());
		if (benutzer.getKonto3() == null) {
			ps.setNull(5, Types.NULL);
		} else
			ps.setInt(5, benutzer.getKonto3().getKonto());
		ps.setString(6, benutzer.getFormat().getLanguage());
		ps.execute();
		ps.close();
	}

	public void update(Benutzer benutzer) throws Exception {
		String update = "UPDATE \"tbenutzer\" ";
		String set = "SET \"name\" = ?, \"passwort\" = ENCRYPT('AES','07',STRINGTOUTF8(?)), \"konto1#\" = ?, \"konto2#\" = ?, \"konto3#\" = ?, \"format\" = ? ";
		String where = "WHERE \"benutzer#\" = ? ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(update + set + where);
		ps.setString(1, benutzer.getName());
		StringBuffer sb = new StringBuffer();
		for (char c : benutzer.getPasswort()) {
			sb.append(c);
		}
		ps.setString(2, sb.toString());
		if (benutzer.getKonto1() == null) {
			ps.setNull(3, Types.NULL);
		} else
			ps.setInt(3, benutzer.getKonto1().getKonto());
		if (benutzer.getKonto2() == null) {
			ps.setNull(4, Types.NULL);
		} else
			ps.setInt(4, benutzer.getKonto2().getKonto());
		if (benutzer.getKonto3() == null) {
			ps.setNull(5, Types.NULL);
		} else
			ps.setInt(5, benutzer.getKonto3().getKonto());
		ps.setString(6, benutzer.getFormat().getLanguage());
		ps.setInt(7, benutzer.getBenutzer());
		ps.executeUpdate();
		ps.close();
	}

	private Benutzer resultSetToBenutzer(ResultSet rs) throws SQLException {
		Benutzer benutzerData = new Benutzer();
		benutzerData.setBenutzer(rs.getInt("benutzer#"));
		benutzerData.setName(rs.getString("name"));
		benutzerData.setPasswort((rs.getString("passwort")).toCharArray());
		benutzerData.setKonto1(KontoDAO.instance().getKonto(rs.getInt("konto1#")));
		benutzerData.setKonto2(KontoDAO.instance().getKonto(rs.getInt("konto2#")));
		benutzerData.setKonto3(KontoDAO.instance().getKonto(rs.getInt("konto3#")));
		benutzerData.setFormat(new Locale(rs.getString("format")));
		return benutzerData;
	}

}