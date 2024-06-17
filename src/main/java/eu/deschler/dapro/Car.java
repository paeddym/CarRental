package eu.deschler.dapro;

import java.util.Arrays;

public class Car {
    private int id;
    private String bezeichnung;
    private String hersteller;
    private int autoart;
    private int sitzplaetze;
    private int kw;
    private String treibstoff;
    private float preisTag;
    private float preisKm;
    private String achsen;
    private String ladeVolumen;
    private String zuladung;
    private String fuehrerschein;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public int getAutoart() {
        return autoart;
    }

    public void setAutoart(int autoart) {
        this.autoart = autoart;
    }

    public int getSitzplaetze() {
        return sitzplaetze;
    }

    public void setSitzplaetze(int sitzplaetze) {
        this.sitzplaetze = sitzplaetze;
    }

    public int getKw() {
        return kw;
    }

    public void setKw(int kw) {
        this.kw = kw;
    }

    public String getTreibstoff() {
        return treibstoff;
    }

    public void setTreibstoff(String treibstoff) {
        this.treibstoff = treibstoff;
    }

    public float getPreisTag() {
        return preisTag;
    }

    public void setPreisTag(float preisTag) {
        this.preisTag = preisTag;
    }

    public float getPreisKm() {
        return preisKm;
    }

    public void setPreisKm(float preisKm) {
        this.preisKm = preisKm;
    }

    public String getAchsen() {
        return achsen;
    }

    public void setAchsen(String achsen) {
        this.achsen = achsen;
    }

    public String getLadeVolumen() {
        return ladeVolumen;
    }

    public void setLadeVolumen(String ladeVolumen) {
        this.ladeVolumen = ladeVolumen;
    }

    public String getZuladung() {
        return zuladung;
    }

    public void setZuladung(String zuladung) {
        this.zuladung = zuladung;
    }

    public String getFuehrerschein() {
        return fuehrerschein;
    }

    public void setFuehrerschein(String fuehrerschein) {
        this.fuehrerschein = fuehrerschein;
    }

    @Override
    public String toString() {
        return "Car [bezeichnung=" + bezeichnung + ", id=" + id + ", hersteller=" + hersteller
                + ", autoart=" + autoart + ", sitzplaetze=" + sitzplaetze + ", kw=" + kw + ", treibstoff="
                + treibstoff + ", preisTag=" + preisTag + ", preisKm=" + preisKm + ", achsen="
                + achsen + ", ladeVolumen=" + ladeVolumen + ", zuladung=" + zuladung  + ", fuehrerscheinKlassen="
                + fuehrerschein + "]";
    }

}

