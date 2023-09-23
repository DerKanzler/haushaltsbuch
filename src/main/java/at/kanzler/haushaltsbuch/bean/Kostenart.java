package at.kanzler.haushaltsbuch.bean;

import java.math.BigDecimal;

public class Kostenart {

    private Integer koart;
    private String koartkubez;
    private String koartbez;
    private BigDecimal koartsaldo;
    private Kostenartgruppe koartgrp;

    @Override
    public String toString() {
        return getKoartkubez();
    }

    public Integer getKoart() {
        return koart;
    }

    public void setKoart(Integer koart) {
        this.koart = koart;
    }

    public String getKoartkubez() {
        return koartkubez;
    }

    public void setKoartkubez(String koartkubez) {
        this.koartkubez = koartkubez;
    }

    public String getKoartbez() {
        return koartbez;
    }

    public void setKoartbez(String koartbez) {
        this.koartbez = koartbez;
    }

    public BigDecimal getKoartsaldo() {
        return koartsaldo;
    }

    public void setKoartsaldo(BigDecimal koartsaldo) {
        this.koartsaldo = koartsaldo;
    }

    public Kostenartgruppe getKoartgrp() {
        return koartgrp;
    }

    public void setKoartgrp(Kostenartgruppe koartgrp) {
        this.koartgrp = koartgrp;
    }

}