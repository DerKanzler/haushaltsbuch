package haushaltsbuch.bean;

public class Kostenartgruppe implements Comparable<Kostenartgruppe> {
	
	private Integer koartgrp;
	private String koartgrpbez;
	private String koartgrpkat;
	private String koartgrpart;
	
	public final static String KAT_EXPENSES = "A";
	public final static String KAT_INCOME = "E";
	public final static String ART_NORMAL = "N";
	public final static String ART_HH = "H";
	
	@Override
	public String toString() {
		return getKoartgrpbez();
	}
	
	public Integer getKoartgrp() {
		return koartgrp;
	}
	
	public void setKoartgrp(Integer koartgrp) {
		this.koartgrp = koartgrp;
	}
	
	public String getKoartgrpbez() {
		return koartgrpbez;
	}
	
	public void setKoartgrpbez(String koartgrpbez) {
		this.koartgrpbez = koartgrpbez;
	}
	
	public String getKoartgrpkat() {
		return koartgrpkat;
	}
	
	public void setKoartgrpkat(String koartgrpkat) {
		this.koartgrpkat = koartgrpkat;
	}
	
	public String getKoartgrpart() {
		return koartgrpart;
	}
	
	public void setKoartgrpart(String koartgrpart) {
		this.koartgrpart = koartgrpart;
	}

	public int compareTo(Kostenartgruppe o) {
		if (this.getKoartgrpkat().equals(o.getKoartgrpkat())) {
			return this.getKoartgrpbez().compareTo(o.getKoartgrpbez());
		}
		else return this.getKoartgrpkat().compareTo(o.getKoartgrpkat());
	}

}