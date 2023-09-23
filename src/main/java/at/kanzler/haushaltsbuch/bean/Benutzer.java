package at.kanzler.haushaltsbuch.bean;

import java.util.Locale;

public class Benutzer {

    private Integer benutzer;
    private String name;
    private char[] passwort;
    private Konto konto1, konto2, konto3;
    private Locale format;

    public Integer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Integer benutzer) {
        this.benutzer = benutzer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char[] getPasswort() {
        return passwort;
    }

    public void setPasswort(char[] passwort) {
        this.passwort = passwort;
    }

    public Konto getKonto1() {
        return konto1;
    }

    public void setKonto1(Konto konto1) {
        this.konto1 = konto1;
    }

    public Konto getKonto2() {
        return konto2;
    }

    public void setKonto2(Konto konto2) {
        this.konto2 = konto2;
    }

    public Konto getKonto3() {
        return konto3;
    }

    public void setKonto3(Konto konto3) {
        this.konto3 = konto3;
    }

    public Locale getFormat() {
        return format;
    }

    public void setFormat(Locale format) {
        this.format = format;
    }

}