package haushaltsbuch.bean;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Buchung {

    private Integer buchung;
    private BigDecimal buchbetr;
    private LocalDate buchdat;
    private String buchtext;
    private LocalDate eindat;
    private Kostenart koart;
    private Konto kontovon;
    private Konto kontonach;

    public Integer getBuchung() {
        return buchung;
    }

    public void setBuchung(Integer buchung) {
        this.buchung = buchung;
    }

    public BigDecimal getBuchbetr() {
        return buchbetr;
    }

    public void setBuchbetr(BigDecimal buchbetr) {
        this.buchbetr = buchbetr;
    }

    public LocalDate getBuchdat() {
        return buchdat;
    }

    public void setBuchdat(LocalDate buchdat) {
        this.buchdat = buchdat;
    }

    public String getBuchtext() {
        return buchtext;
    }

    public void setBuchtext(String buchtext) {
        this.buchtext = buchtext;
    }

    public LocalDate getEindat() {
        return eindat;
    }

    public void setEindat(LocalDate eindat) {
        this.eindat = eindat;
    }

    public Kostenart getKoart() {
        return koart;
    }

    public void setKoart(Kostenart koart) {
        this.koart = koart;
    }

    public Konto getKontovon() {
        return kontovon;
    }

    public void setKontovon(Konto kontovon) {
        this.kontovon = kontovon;
    }

    public Konto getKontonach() {
        return kontonach;
    }

    public void setKontonach(Konto kontonach) {
        this.kontonach = kontonach;
    }

}