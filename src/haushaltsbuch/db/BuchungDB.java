package haushaltsbuch.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Vector;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.bean.util.SearchBuchung;
import haushaltsbuch.dao.KontoDAO;
import haushaltsbuch.dao.KostenartDAO;

public class BuchungDB {

	private final String select = "SELECT \"buchung#\", \"buchbetr\", \"buchdat\", \"buchtext\", \"eindat\", \"koart#\", \"kontovon#\", \"kontonach#\" FROM \"tbuchung\" ";
	private final String order = "ORDER BY \"buchdat\" ASC, \"buchung#\" DESC ";

	public Buchung getBuchung(Integer id) throws Exception {
		String where = "WHERE \"buchung#\" = ? ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + where + order);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		Buchung buchung = new Buchung();
		while (rs.next()) {
			buchung = resultSetToBuchung(rs);
		}
		ps.close();
		return buchung;
	}

	public Vector<Buchung> getAll() throws Exception {
		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + order);
		ResultSet rs = ps.executeQuery();
		Vector<Buchung> buchungen = new Vector<Buchung>();
		while (rs.next()) {
			buchungen.addElement(resultSetToBuchung(rs));
		}
		ps.close();
		return buchungen;
	}

	public void save(Buchung buchung) throws Exception {
		String insert = "INSERT INTO \"tbuchung\" (\"buchbetr\", \"buchdat\", \"buchtext\", \"eindat\", \"koart#\", \"kontovon#\", \"kontonach#\") ";
		String values = "VALUES (?, ?, ?, ?, ?, ?, ?) ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(insert + values);
		ps.setObject(1, buchung.getBuchbetr(), Types.DECIMAL);
		ps.setDate(2, Date.valueOf(buchung.getBuchdat()));
		ps.setString(3, buchung.getBuchtext());
		ps.setDate(4, Date.valueOf(buchung.getEindat()));
		if (buchung.getKoart() == null) {
			ps.setNull(5, Types.INTEGER);
		} else
			ps.setInt(5, buchung.getKoart().getKoart());
		if (buchung.getKontovon() == null) {
			ps.setNull(6, Types.INTEGER);
		} else
			ps.setInt(6, buchung.getKontovon().getKonto());
		if (buchung.getKontonach() == null) {
			ps.setNull(7, Types.INTEGER);
		} else
			ps.setInt(7, buchung.getKontonach().getKonto());
		ps.execute();
		ps.close();
	}

	public void update(Buchung buchung) throws Exception {
		String update = "UPDATE \"tbuchung\" ";
		String set = "SET \"buchtext\" = ? ";
		String where = "WHERE \"buchung#\" = ? ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(update + set + where);
		ps.setString(1, buchung.getBuchtext());
		ps.setInt(2, buchung.getBuchung());
		ps.executeUpdate();
		ps.close();
	}

	public Vector<Buchung> search(SearchBuchung suchBuchung) throws Exception {
		String where = createWhere(suchBuchung);
		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + where + order);
		ResultSet rs = prepareStatement(ps, suchBuchung).executeQuery();
		Vector<Buchung> buchungen = new Vector<Buchung>();
		while (rs.next()) {
			buchungen.addElement(resultSetToBuchung(rs));
		}
		ps.close();
		return buchungen;
	}

	public void delete(Buchung b) throws Exception {
		String delete = "DELETE FROM \"tbuchung\" ";
		String where = "WHERE \"buchung#\" = ? ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(delete + where);
		ps.setInt(1, b.getBuchung());
		ps.execute();
		ps.close();
	}

	public void deleteAll() throws Exception {
		String delete = "DELETE FROM \"tbuchung\" ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(delete);
		ps.execute();
		ps.close();
	}

	public LocalDate getFirstBookDate() throws Exception {
		LocalDate firstBookDate = null;
		PreparedStatement ps = DB.instance().getConnection()
				.prepareStatement("SELECT MIN(\"buchdat\") AS \"firstBookDate\" FROM \"tbuchung\"");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			firstBookDate = rs.getDate("firstBookDate").toLocalDate();
		}
		ps.close();
		return firstBookDate;
	}

	public LocalDate getLastBookDate() throws Exception {
		LocalDate lastBookDate = null;
		PreparedStatement ps = DB.instance().getConnection()
				.prepareStatement("SELECT MAX(\"buchdat\") AS \"lastBookDate\" FROM \"tbuchung\"");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			lastBookDate = rs.getDate("lastBookDate").toLocalDate();
		}
		ps.close();
		return lastBookDate;
	}

	private Buchung resultSetToBuchung(ResultSet rs) throws SQLException {
		Buchung buchung = new Buchung();
		buchung.setBuchung(rs.getInt("buchung#"));
		buchung.setBuchbetr(rs.getBigDecimal("buchbetr"));
		buchung.setBuchdat(rs.getDate("buchdat").toLocalDate());
		buchung.setBuchtext(rs.getString("buchtext"));
		buchung.setEindat(rs.getDate("eindat").toLocalDate());
		Integer i = rs.getInt("koart#");
		if (i > 0) {
			buchung.setKoart(KostenartDAO.instance().getKostenart(i));
		}
		Integer j = rs.getInt("kontovon#");
		if (j > 0) {
			buchung.setKontovon(KontoDAO.instance().getKonto(j));
		}
		Integer k = rs.getInt("kontonach#");
		if (k > 0) {
			buchung.setKontonach(KontoDAO.instance().getKonto(k));
		}
		return buchung;
	}

	private String createWhere(SearchBuchung suchBuchung) {
		String where = "WHERE ";
		Integer count = 0;
		if (suchBuchung.getBookText() != null) {
			if (count > 0) {
				where = where.concat("AND ");
			}
			where = where.concat("LOWER(\"buchtext\") LIKE LOWER(?) ");
			count++;
		}
		if (suchBuchung.getKoart() != null) {
			if (count > 0) {
				where = where.concat("AND ");
			}
			where = where.concat("\"koart#\" = ? ");
			count++;
		}
		if (suchBuchung.getFromAccount() != null) {
			if (count > 0) {
				where = where.concat("AND ");
			}
			if (suchBuchung.isSameAccount()) {
				where = where.concat("(");
			}
			where = where.concat("\"kontovon#\" = ? ");
			count++;
		}
		if (suchBuchung.getToAccount() != null) {
			if (count > 0) {
				if (suchBuchung.isSameAccount()) {
					where = where.concat("OR ");
				} else
					where = where.concat("AND ");
			}
			where = where.concat("\"kontonach#\" = ?");
			if (suchBuchung.isSameAccount()) {
				where = where.concat(") ");
			} else
				where = where.concat(" ");
			count++;
		}
		if (suchBuchung.getFromDate() != null) {
			if (count > 0) {
				where = where.concat("AND ");
			}
			where = where.concat("\"buchdat\" >= ? ");
			count++;
		}
		if (suchBuchung.getToDate() != null) {
			if (count > 0) {
				where = where.concat("AND ");
			}
			where = where.concat("\"buchdat\" <= ? ");
			count++;
		}
		if (suchBuchung.getAmount() != null) {
			if (count > 0) {
				where = where.concat("AND ");
			}
			where = where.concat("\"buchbetr\" " + suchBuchung.getOperator() + " ? ");
			count++;
		}
		if (count > 0) {
			return where;
		} else
			return new String();
	}

	private PreparedStatement prepareStatement(PreparedStatement ps, SearchBuchung suchBuchung) throws SQLException {
		Integer count = 1;
		if (suchBuchung.getBookText() != null) {
			ps.setString(count, suchBuchung.getBookText());
			count++;
		}
		if (suchBuchung.getKoart() != null) {
			ps.setInt(count, suchBuchung.getKoart().getKoart());
			count++;
		}
		if (suchBuchung.getFromAccount() != null) {
			ps.setInt(count, suchBuchung.getFromAccount().getKonto());
			count++;
		}
		if (suchBuchung.getToAccount() != null) {
			ps.setInt(count, suchBuchung.getToAccount().getKonto());
			count++;
		}
		if (suchBuchung.getFromDate() != null) {
			ps.setDate(count, Date.valueOf(suchBuchung.getFromDate()));
			count++;
		}
		if (suchBuchung.getToDate() != null) {
			ps.setDate(count, Date.valueOf(suchBuchung.getToDate()));
			count++;
		}
		if (suchBuchung.getAmount() != null) {
			ps.setObject(count, suchBuchung.getAmount(), Types.DECIMAL);
			count++;
		}
		return ps;
	}

}