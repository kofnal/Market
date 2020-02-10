package app.fil.market.recikler_tovari;



public class TovariList {

    public TovariList(String id, String naimenovanie, int skidka, String foto_url, Double cenaBezSkidki,
                      Double cenaSoSkidkoy, int kolihestvo, boolean selected) {
        this.id = id;
        this.naimenovanie = naimenovanie;
        this.skidka = skidka;
        this.foto_url = foto_url;
        this.cenaBezSkidki = cenaBezSkidki;
        this.cenaSoSkidkoy = cenaSoSkidkoy;
        this.kolihestvo = kolihestvo;
        this.selected = selected;
    }


    public boolean isSelected() {
        return selected;
    }

    public int getKolihestvo() {
        return kolihestvo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaimenovanie() {
        return naimenovanie;
    }

    public void setNaimenovanie(String naimenovanie) {
        this.naimenovanie = naimenovanie;
    }

    public int getSkidka() {
        return skidka;
    }

    public void setSkidka(int skidka) {
        this.skidka = skidka;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

    public Double getCenaBezSkidki() {
        return cenaBezSkidki;
    }

    public void setCenaBezSkidki(Double cenaBezSkidki) {
        this.cenaBezSkidki = cenaBezSkidki;
    }

    public Double getCenaSoSkidkoy() {
        return cenaSoSkidkoy;
    }

    public void setCenaSoSkidkoy(Double cenaSoSkidkoy) {
        this.cenaSoSkidkoy = cenaSoSkidkoy;
    }

    private final boolean selected;
    private final int kolihestvo;
    private String id;
    private String naimenovanie;
    private int skidka;
    private String foto_url;
    private Double cenaBezSkidki;
    private Double cenaSoSkidkoy;



}
