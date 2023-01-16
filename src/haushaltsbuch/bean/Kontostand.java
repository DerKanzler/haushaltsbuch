package haushaltsbuch.bean;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Kontostand {

    private Integer ktost;
    private LocalDate ktostdat;
    private BigDecimal ktostsaldo;
    private Konto konto;

    public Integer getKtost() {
        return ktost;
    }

    public void setKtost(Integer ktost) {
        this.ktost = ktost;
    }

    public LocalDate getKtostdat() {
        return ktostdat;
    }

    public void setKtostdat(LocalDate ktostdat) {
        this.ktostdat = ktostdat;
    }

    public BigDecimal getKtostsaldo() {
        return ktostsaldo;
    }

    public void setKtostsaldo(BigDecimal ktostsaldo) {
        this.ktostsaldo = ktostsaldo;
    }

    public Konto getKonto() {
        return konto;
    }

    public void setKonto(Konto konto) {
        this.konto = konto;
    }

}