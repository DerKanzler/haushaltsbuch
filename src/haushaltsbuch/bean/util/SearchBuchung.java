package haushaltsbuch.bean.util;


import haushaltsbuch.bean.Konto;
import haushaltsbuch.bean.Kostenart;

import java.math.BigDecimal;
import java.util.Date;

public class SearchBuchung {
	
	private String bookText;
	private Kostenart koart;
	private Konto toAccount;
	private Konto fromAccount;
	private Boolean sameAccount = false;
	private Date fromDate;
	private Date toDate;
	private String operator;
	private BigDecimal amount;
	
	public String getBookText() {
		return bookText;
	}
	
	public void setBookText(String bookText) {
		this.bookText = bookText;
	}
	
	public Kostenart getKoart() {
		return koart;
	}
	
	public void setKoart(Kostenart koart) {
		this.koart = koart;
	}
	
	public Konto getToAccount() {
		return toAccount;
	}
	
	public void setToAccount(Konto toAccount) {
		this.toAccount = toAccount;
	}
	
	public Konto getFromAccount() {
		return fromAccount;
	}
	
	public void setFromAccount(Konto fromAccount) {
		this.fromAccount = fromAccount;
	}
	
	public Boolean isSameAccount() {
		return sameAccount;
	}
	
	public void setSameAccount(Boolean same) {
		this.sameAccount = same;
	}
	
	public Date getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	public Date getToDate() {
		return toDate;
	}
	
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}