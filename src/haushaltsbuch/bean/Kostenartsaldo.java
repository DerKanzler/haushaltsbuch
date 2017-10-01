package haushaltsbuch.bean;

import java.math.BigDecimal;
import java.util.Date;

public class Kostenartsaldo {
	
	private Integer koartsald;
	private Date koartsalddat;
	private BigDecimal koartmonsaldo;
	private BigDecimal koartjrsaldo;
	private BigDecimal koartjrsaldovj;
	private Kostenart koart;
	
	public Integer getKoartsald() {
		return koartsald;
	}
	
	public void setKoartsald(Integer koartsald) {
		this.koartsald = koartsald;
	}
	
	public Date getKoartsalddat() {
		return koartsalddat;
	}
	
	public void setKoartsalddat(Date koartsalddat) {
		this.koartsalddat = koartsalddat;
	}
	
	public BigDecimal getKoartmonsaldo() {
		return koartmonsaldo;
	}
	
	public void setKoartmonsaldo(BigDecimal koartmonsaldo) {
		this.koartmonsaldo = koartmonsaldo;
	}
	
	public BigDecimal getKoartjrsaldo() {
		return koartjrsaldo;
	}
	
	public void setKoartjrsaldo(BigDecimal koartjrsaldo) {
		this.koartjrsaldo = koartjrsaldo;
	}
	
	public BigDecimal getKoartjrsaldovj() {
		return koartjrsaldovj;
	}
	
	public void setKoartjrsaldovj(BigDecimal koartjrsaldovj) {
		this.koartjrsaldovj = koartjrsaldovj;
	}
	
	public Kostenart getKoart() {
		return koart;
	}
	
	public void setKoart(Kostenart koart) {
		this.koart = koart;
	}
	
}