package haushaltsbuch.db;

import haushaltsbuch.bean.Kostenartsaldo;
import haushaltsbuch.dao.KostenartDAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;

public class KostenartsaldoDB {
	
	private final String select = "SELECT \"koartsald#\", \"koartsalddat\", \"koartmonsaldo\", \"koartjrsaldo\", \"koartjrsaldovj\", \"koart#\" FROM \"tkostenartsaldo\" ";
	private final String order = "ORDER BY \"koartsalddat\" ASC, \"koartsald#\" DESC ";
	
	public Vector<Kostenartsaldo> find(Kostenartsaldo ks) throws Exception {
		String where = createWhere(ks);
		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + where + order);
		ResultSet rs = prepareStatement(ps, ks).executeQuery();
		Vector<Kostenartsaldo> kostenartsaldi = new Vector<Kostenartsaldo>();
		while (rs.next()) {
			kostenartsaldi.addElement(resultSetToKostenartsaldo(rs));
		}
		ps.close();
		return kostenartsaldi;
	}
	
	public Vector<Kostenartsaldo> getAll() throws Exception {		
		PreparedStatement ps = DB.instance().getConnection().prepareStatement(select + order);
		ResultSet rs = ps.executeQuery();
		Vector<Kostenartsaldo> kontostaende = new Vector<Kostenartsaldo>();
		while (rs.next()) {
			kontostaende.addElement(resultSetToKostenartsaldo(rs));
		}
		ps.close();
		return kontostaende;
	}

	public void save(Kostenartsaldo kostenartsaldo) throws Exception {
		String insert = "INSERT INTO \"tkostenartsaldo\" (\"koartsalddat\", \"koartmonsaldo\", \"koartjrsaldo\", \"koartjrsaldovj\", \"koart#\") ";
		String values = "VALUES (?, ?, ?, ?, ?) ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(insert + values, Statement.RETURN_GENERATED_KEYS);
		ps.setDate(1, new Date(kostenartsaldo.getKoartsalddat().getTime()));
		ps.setObject(2, kostenartsaldo.getKoartmonsaldo(), Types.DECIMAL);
		ps.setObject(3, kostenartsaldo.getKoartjrsaldo(), Types.DECIMAL);
		ps.setObject(4, kostenartsaldo.getKoartjrsaldovj(), Types.DECIMAL);
		ps.setInt(5, kostenartsaldo.getKoart().getKoart());
		ps.execute();
		
		ResultSet rs = ps.getGeneratedKeys();
		while (rs.next()) {
			kostenartsaldo.setKoartsald(rs.getInt(1));
		}
		ps.close();
	}
	
	public void update(Kostenartsaldo kostenartsaldo) throws Exception {
		String update = "UPDATE \"tkostenartsaldo\" ";
		String set = "SET \"koartsalddat\" = ?, \"koartmonsaldo\" = ?, \"koartjrsaldo\" = ?, \"koartjrsaldovj\" = ? ";
		String where= "WHERE \"koartsald#\" = ? ";

		PreparedStatement ps = DB.instance().getConnection().prepareStatement(update + set + where);
		ps.setDate(1, new Date(kostenartsaldo.getKoartsalddat().getTime()));
		ps.setObject(2, kostenartsaldo.getKoartmonsaldo(), Types.DECIMAL);
		ps.setObject(3, kostenartsaldo.getKoartjrsaldo(), Types.DECIMAL);
		ps.setObject(4, kostenartsaldo.getKoartjrsaldovj(), Types.DECIMAL);
		ps.setInt(5, kostenartsaldo.getKoartsald());
		ps.executeUpdate();
		ps.close();
	}
	
	private Kostenartsaldo resultSetToKostenartsaldo(ResultSet rs) throws Exception {
		Kostenartsaldo kostenartsaldo = new Kostenartsaldo();
		kostenartsaldo.setKoartsald(rs.getInt("koartsald#"));
		kostenartsaldo.setKoartsalddat(rs.getDate("koartsalddat"));
		kostenartsaldo.setKoartmonsaldo(rs.getBigDecimal("koartmonsaldo"));
		kostenartsaldo.setKoartjrsaldo(rs.getBigDecimal("koartjrsaldo"));
		kostenartsaldo.setKoartjrsaldovj(rs.getBigDecimal("koartjrsaldovj"));
		kostenartsaldo.setKoart(KostenartDAO.instance().getKostenart(rs.getInt("koart#")));
		return kostenartsaldo;
	}

	private String createWhere(Kostenartsaldo ks) {
		StringBuffer where = new StringBuffer("WHERE ");
		Integer count = 0;
		if (ks.getKoart() != null) {
			if (count > 0) {
				where.append("AND ");	
			}
			where.append("\"koart#\" LIKE ? ");
			count++;
		}
		if (ks.getKoartjrsaldo() != null) {
			if (count > 0) {
				where.append("AND ");	
			}
			where.append("\"koartjrsaldo\" = ? ");
			count++;
		}
		if (ks.getKoartjrsaldovj() != null) {
			if (count > 0) {
				where.append("AND ");	
			}
			where.append("\"koartjrsaldovj\" = ? ");
			count++;
		}
		if (ks.getKoartmonsaldo() != null) {
			if (count > 0) {
				where.append("AND ");	
			}
			where.append("\"koartmonsaldo\" = ? ");
			count++;
		}
		if (ks.getKoartsalddat() != null) {
			if (count > 0) {
				where.append("AND ");	
			}
			where.append("\"koartsalddat\" = ? ");
			count++;
		}
		if (ks.getKoartsald() != null) {
			if (count > 0) {
				where.append("AND ");	
			}
			where.append("\"koartsald#\" = ? ");
			count++;
		}
		if (count > 0) {
			return where.toString();
		}
		else return new String();
	}
	
	private PreparedStatement prepareStatement(PreparedStatement ps, Kostenartsaldo ks) throws Exception {
		Integer count = 1;
		if (ks.getKoart() != null) {
			ps.setInt(count, ks.getKoart().getKoart());
			count++;
		}
		if (ks.getKoartjrsaldo() != null) {
			ps.setObject(count, ks.getKoartjrsaldo(), Types.DECIMAL);
			count++;
		}
		if (ks.getKoartjrsaldovj() != null) {
			ps.setObject(count, ks.getKoartjrsaldovj(), Types.DECIMAL);
			count++;
		}
		if (ks.getKoartmonsaldo() != null) {
			ps.setObject(count, ks.getKoartmonsaldo(), Types.DECIMAL);
			count++;
		}
		if (ks.getKoartsalddat() != null) {
			ps.setDate(count, new Date(ks.getKoartsalddat().getTime()));
			count++;
		}
		if (ks.getKoartsald() != null) {
			ps.setInt(count, ks.getKoartsald());
			count++;
		}
		return ps;
	}
	
}