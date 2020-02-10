package app.fil.market.ceni_i_skidki;
import android.os.Parcel;
import android.os.Parcelable;

import app.fil.market.Utils;


public class Ceni implements Parcelable {
    String naimenovanie;
    String foto;
    String id_sql_tovara_v_baze;
    String id_sql_tovara_v_korzine_pokupatelia;
    Double kolihestvo=1.0;
    Double kolvovupakovke=1.0;
    Double cenaZaOdinKg =0.0 ;
    Double cenaZaUpakovky =0.0 ;
    Double skidka =0.0 ;
    Double cenaFinalSkidkaZaOdinKg =0.0 ;
    boolean vibranLi;
    String znahrazmriada = " ";
    String mestovilova = " ";
    String typeUpakovki = " ";
    String edinica_izmerenia_upakovki = " ";
    String sostoianieTovara = " ";
    String raskazotovare = " ";

    public Ceni(String naimenovanieStr, String fotoStr, String cenaStr, String skidkaStr, String cenaSkidkaStr, String vibranLiStr, String id_sql_tovara_v_baze_Str,
                String id_sql_tovara_v_korzine_pokupatelia_Str, String kolihestvoStr, String kolvovupakovkeStr,
                String znahrazmriadaStr, String mestovilovaStr, String  typeUpakovkiStr, String sostoianieTovaraStr,
                String edinica_izmerenia_upakovkiStr,  String raskazotovareStr){
        System.out.println(cenaStr+", "+skidkaStr+", "+cenaSkidkaStr+", "+vibranLiStr+", "+id_sql_tovara_v_baze_Str+", "+id_sql_tovara_v_korzine_pokupatelia_Str+" - create objekt Ceni");
        System.out.println(cenaStr+", "+skidkaStr+", "+cenaSkidkaStr+" - create objekt Ceni rashet skidki");
        if (!cenaZaOdinKg.equals("null")) this.cenaZaOdinKg = Double.parseDouble(cenaStr);
        if (!skidka.equals("null")) this.skidka = Double.parseDouble(skidkaStr);
        if (!cenaFinalSkidkaZaOdinKg.equals("null")) this.cenaFinalSkidkaZaOdinKg = Double.parseDouble(cenaSkidkaStr);
        if (cenaZaOdinKg > 0.0) {
            if (cenaFinalSkidkaZaOdinKg == 0.0 & skidka > 0.0) cenaFinalSkidkaZaOdinKg = cenaZaOdinKg *(1-skidka/100);
            if ((cenaFinalSkidkaZaOdinKg > 0.0 & skidka == 0.0)||(cenaFinalSkidkaZaOdinKg > 0.0 & skidka > 0.0)) {
                skidka=100.0- cenaFinalSkidkaZaOdinKg / cenaZaOdinKg *100;
                System.out.println("kambal "+ Double.toString(skidka));
            }
            if (cenaFinalSkidkaZaOdinKg > 0.0 & skidka > 0.0) skidka=100.0- cenaFinalSkidkaZaOdinKg / cenaZaOdinKg *100;
            if (cenaFinalSkidkaZaOdinKg == 0.0 & skidka == 0.0) {
                skidka = 0.0;
                cenaFinalSkidkaZaOdinKg = cenaZaOdinKg;
            }
        } else{ // cenaZaOdinKg<0.0
            cenaZaOdinKg = cenaFinalSkidkaZaOdinKg /(1-skidka/100);
        }

        naimenovanie=naimenovanieStr;
        foto= Utils.BASE_IP + fotoStr;
        if(!vibranLiStr.equals("null"))
        vibranLi= Boolean.valueOf(vibranLiStr);
        else vibranLi=false;
        id_sql_tovara_v_baze =id_sql_tovara_v_baze_Str;
        id_sql_tovara_v_korzine_pokupatelia =id_sql_tovara_v_korzine_pokupatelia_Str;
        if(!kolihestvoStr.equals("null")) kolihestvo= Double.valueOf(kolihestvoStr);
        else kolihestvo=0.0;
        if(!kolvovupakovkeStr.equals("null")) kolvovupakovke= Double.valueOf(kolvovupakovkeStr);
        else kolvovupakovke=1.0;
        cenaZaUpakovky=cenaFinalSkidkaZaOdinKg*kolvovupakovke;

        znahrazmriada=znahrazmriadaStr;
        mestovilova=mestovilovaStr;
        typeUpakovki=typeUpakovkiStr;
        edinica_izmerenia_upakovki=edinica_izmerenia_upakovkiStr;
        sostoianieTovara =sostoianieTovaraStr;
        raskazotovare=raskazotovareStr;
        System.out.println(Double.toString(cenaZaOdinKg)+", "+ Double.toString(skidka)+", "+ Double.toString(cenaFinalSkidkaZaOdinKg)+", "
                + Boolean.toString(vibranLi)+", "+ id_sql_tovara_v_baze +" - create objekt Ceni in Double");
        System.out.println(Double.toString(cenaZaOdinKg)+", "+ Double.toString(skidka)+", "+ Double.toString(cenaFinalSkidkaZaOdinKg)+"sadfghjgfdafsdghfkkmyntrhg");
        System.out.println("Ceni Id SQL "+" ID tovara v baze="+id_sql_tovara_v_baze+", "+
                "ID tovara v korzinePokupatelia="+id_sql_tovara_v_korzine_pokupatelia);
    }


    protected Ceni(Parcel in) {
        naimenovanie = in.readString();
        foto = in.readString();
        id_sql_tovara_v_baze = in.readString();
        id_sql_tovara_v_korzine_pokupatelia = in.readString();
        if (in.readByte() == 0) {
            kolihestvo = null;
        } else {
            kolihestvo = in.readDouble();
        }
        if (in.readByte() == 0) {
            kolvovupakovke = null;
        } else {
            kolvovupakovke = in.readDouble();
        }
        if (in.readByte() == 0) {
            cenaZaOdinKg = null;
        } else {
            cenaZaOdinKg = in.readDouble();
        }
        if (in.readByte() == 0) {
            cenaZaUpakovky = null;
        } else {
            cenaZaUpakovky = in.readDouble();
        }
        if (in.readByte() == 0) {
            skidka = null;
        } else {
            skidka = in.readDouble();
        }
        if (in.readByte() == 0) {
            cenaFinalSkidkaZaOdinKg = null;
        } else {
            cenaFinalSkidkaZaOdinKg = in.readDouble();
        }
        vibranLi = in.readByte() != 0;
        znahrazmriada = in.readString();
        mestovilova = in.readString();
        typeUpakovki = in.readString();
        edinica_izmerenia_upakovki = in.readString();
        sostoianieTovara = in.readString();
        raskazotovare = in.readString();
    }

    public static final Creator<Ceni> CREATOR = new Creator<Ceni>() {
        @Override
        public Ceni createFromParcel(Parcel in) {
            return new Ceni(in);
        }

        @Override
        public Ceni[] newArray(int size) {
            return new Ceni[size];
        }
    };

    public String getNaimenovanie() {
        return naimenovanie;
    }

    public String getFoto() {
        return foto;
    }

    public Double getCenaZaOdinKg() {
            return cenaZaOdinKg;
    }
    public String getCenaStr() {
        return Utils.getStringFromDoubleFormated(cenaZaOdinKg);
    }

    public Double getCenaZaUpakovky() {
        return cenaZaUpakovky;
    }

    public String getEdinica_izmerenia_upakovki() {
        return edinica_izmerenia_upakovki;
    }

    public String getTypeUpakovki() {
        return typeUpakovki;
    }

    public String getCenaZaUpakovkyStr() {
        return Utils.getStringFromDoubleFormated(cenaZaUpakovky);
    }
    public Double getSkidka() {
        return skidka;
    }
    public boolean getIsSelected() {
        return vibranLi;
    }
    public String getId_sql_tovara_v_baze() {
        return id_sql_tovara_v_baze;
    }
    public String getId_sql_tovara_v_korzine_pokupatelia() {
        return id_sql_tovara_v_korzine_pokupatelia;
    }
    public Double getKolihestvo() {
        return kolihestvo;
    }
    public Double getKolihestvoV_Upakovke() {
        return kolvovupakovke;
    }
    public String getZnahrazmriada() {
        return znahrazmriada;
    }
    public String getMestovilova() {
        return mestovilova;
    }
    public String getSostoianieTovara() {
        return sostoianieTovara;
    }
    public String getRaskazotovare() {
        return raskazotovare;
    }
    public void setKolihestvo(Double kolihestvo) {
        this.kolihestvo = kolihestvo;
    }
    public Double getCenaFinalSoSkidkoy() {
        return cenaFinalSkidkaZaOdinKg;
    }
    public String getCenaFinalSoSkidkoyStr() {
        return Utils.getStringFromDoubleFormated(cenaFinalSkidkaZaOdinKg);
    }
    public void setVibranLi(boolean vibranLi) {
        this.vibranLi = vibranLi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(naimenovanie);
        parcel.writeString(foto);
        parcel.writeString(id_sql_tovara_v_baze);
        parcel.writeString(id_sql_tovara_v_korzine_pokupatelia);
        if (kolihestvo == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(kolihestvo);
        }
        if (kolvovupakovke == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(kolvovupakovke);
        }
        if (cenaZaOdinKg == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(cenaZaOdinKg);
        }
        if (cenaZaUpakovky == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(cenaZaUpakovky);
        }
        if (skidka == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(skidka);
        }
        if (cenaFinalSkidkaZaOdinKg == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(cenaFinalSkidkaZaOdinKg);
        }
        parcel.writeByte((byte) (vibranLi ? 1 : 0));
        parcel.writeString(znahrazmriada);
        parcel.writeString(mestovilova);
        parcel.writeString(typeUpakovki);
        parcel.writeString(edinica_izmerenia_upakovki);
        parcel.writeString(sostoianieTovara);
        parcel.writeString(raskazotovare);
    }
}
