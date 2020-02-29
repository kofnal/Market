package app.fil.market.ui.podkategorii;

public class PodkategoriaObjectSQL {
    private String name, foto, id;

    public PodkategoriaObjectSQL(String id, String name, String foto) {
        this.name = name;
        this.id = id;
        this.foto = foto;
    }

    public String getName() {
        return name;
    }


    public String getFoto() {
        return foto;
    }

    public String getId() {
        return id;
    }
}
