package haushaltsbuch.bean;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Konto {

    private Integer konto;
    private String kuerzel;
    private LocalDate abldat;
    private String ktobez;
    private LocalDate buchdat;
    private BigDecimal jreinsaldo;
    private String ktotyp;
    private String kzog;
    private BigDecimal saldo;

    @Override
    public String toString() {
        if (getKuerzel() == null) {
            return "";
        } else {
            return getKuerzel();
        }
    }

    public Integer getKonto() {
        return konto;
    }

    public void setKonto(Integer konto) {
        this.konto = konto;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }

    public LocalDate getAbldat() {
        return abldat;
    }

    public void setAbldat(LocalDate abldat) {
        this.abldat = abldat;
    }

    public String getKtobez() {
        return ktobez;
    }

    public void setKtobez(String ktobez) {
        this.ktobez = ktobez;
    }

    public LocalDate getBuchdat() {
        return buchdat;
    }

    public void setBuchdat(LocalDate buchdat) {
        this.buchdat = buchdat;
    }

    public BigDecimal getJreinsaldo() {
        return jreinsaldo;
    }

    public void setJreinsaldo(BigDecimal jreinsaldo) {
        this.jreinsaldo = jreinsaldo;
    }

    public String getKtotyp() {
        return ktotyp;
    }

    public void setKtotyp(String ktotyp) {
        this.ktotyp = ktotyp;
    }

    public String getKzog() {
        return kzog;
    }

    public void setKzog(String kzog) {
        this.kzog = kzog;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public boolean isValid() {
        return "O".equals(getKzog());
    }

    public boolean isDisposable() {
        return "V".equals(getKtotyp());
    }

}
