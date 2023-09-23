package at.kanzler.haushaltsbuch.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Vector;

import at.kanzler.haushaltsbuch.bean.Buchung;
import at.kanzler.haushaltsbuch.bean.util.SearchBuchung;
import at.kanzler.haushaltsbuch.dao.KontoDAO;
import at.kanzler.haushaltsbuch.dao.KostenartDAO;

public class VJBuchungDB {

    private final String select = "SELECT \"vjbuchung#\", \"vjbuchbetr\", \"vjbuchdat\", \"vjbuchtext\", \"koart#\", \"kontovon#\", \"kontonach#\" FROM \"tvjbuchung\" ";
    private final String order = "ORDER BY \"vjbuchdat\" DESC, \"vjbuchung#\" DESC ";

    public Buchung getBuchung(Integer id) throws Exception {
        String where = "WHERE \"vjbuchung#\" = ? ";

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
        String insert = "INSERT INTO \"tvjbuchung\" (\"vjbuchbetr\", \"vjbuchdat\", \"vjbuchtext\", \"koart#\", \"kontovon#\", \"kontonach#\") ";
        String values = "VALUES (?, ?, ?, ?, ?, ?) ";

        PreparedStatement ps = DB.instance().getConnection().prepareStatement(insert + values);
        ps.setObject(1, buchung.getBuchbetr(), Types.DECIMAL);
        ps.setDate(2, Date.valueOf(buchung.getBuchdat()));
        ps.setString(3, buchung.getBuchtext());
        if (buchung.getKoart() == null) {
            ps.setNull(4, Types.INTEGER);
        } else
            ps.setInt(4, buchung.getKoart().getKoart());
        if (buchung.getKontovon() == null) {
            ps.setNull(5, Types.INTEGER);
        } else
            ps.setInt(5, buchung.getKontovon().getKonto());
        if (buchung.getKontonach() == null) {
            ps.setNull(6, Types.INTEGER);
        } else
            ps.setInt(6, buchung.getKontonach().getKonto());
        ps.execute();
        ps.close();
    }

    public void update(Buchung buchung) throws Exception {
        String update = "UPDATE \"tvjbuchung\" ";
        String set = "SET \"vjbuchtext\" = ? ";
        String where = "WHERE \"vjbuchung#\" = ? ";

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

    public LocalDate getFirstBookDate() throws Exception {
        LocalDate firstBookDate = null;
        PreparedStatement ps = DB.instance().getConnection()
                .prepareStatement("SELECT MIN(\"vjbuchdat\") AS \"firstBookDate\" FROM \"tvjbuchung\"");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            firstBookDate = rs.getDate("firstBookDate").toLocalDate();
        }
        ps.close();
        return firstBookDate;
    }

    public LocalDate getLastBookDate() throws Exception {
        LocalDate firstBookDate = null;
        PreparedStatement ps = DB.instance().getConnection()
                .prepareStatement("SELECT MAX(\"vjbuchdat\") AS \"lastBookDate\" FROM \"tvjbuchung\"");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            firstBookDate = rs.getDate("lastBookDate").toLocalDate();
        }
        ps.close();
        return firstBookDate;
    }

    private Buchung resultSetToBuchung(ResultSet rs) throws Exception {
        Buchung buchung = new Buchung();
        buchung.setBuchung(rs.getInt("vjbuchung#"));
        buchung.setBuchbetr(rs.getBigDecimal("vjbuchbetr"));
        buchung.setBuchdat(rs.getDate("vjbuchdat").toLocalDate());
        buchung.setBuchtext(rs.getString("vjbuchtext"));
        Integer i = rs.getInt("koart#");
        if (i > 0) {
            buchung.setKoart(KostenartDAO.instance().getMap().get(i));
        }
        Integer j = rs.getInt("kontovon#");
        if (j > 0) {
            buchung.setKontovon(KontoDAO.instance().getMap().get(j));
        }
        Integer k = rs.getInt("kontonach#");
        if (k > 0) {
            buchung.setKontonach(KontoDAO.instance().getMap().get(k));
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
            where = where.concat("LOWER(\"vjbuchtext\") LIKE LOWER(?) ");
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
            where = where.concat("\"vjbuchdat\" >= ? ");
            count++;
        }
        if (suchBuchung.getToDate() != null) {
            if (count > 0) {
                where = where.concat("AND ");
            }
            where = where.concat("\"vjbuchdat\" <= ? ");
            count++;
        }
        if (suchBuchung.getAmount() != null) {
            if (count > 0) {
                where = where.concat("AND ");
            }
            where = where.concat("\"vjbuchbetr\" " + suchBuchung.getOperator() + " ? ");
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