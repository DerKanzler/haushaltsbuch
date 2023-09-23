package at.kanzler.haushaltsbuch.bean.util;

import java.math.BigDecimal;
import java.time.LocalDate;

import at.kanzler.haushaltsbuch.bean.Konto;
import at.kanzler.haushaltsbuch.bean.Kostenart;

public class SearchBuchung {

    private String bookText;
    private Kostenart koart;
    private Konto toAccount;
    private Konto fromAccount;
    private Boolean sameAccount = false;
    private LocalDate fromDate;
    private LocalDate toDate;
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

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
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