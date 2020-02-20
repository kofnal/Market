package app.fil.market.ceni_i_skidki;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.fil.market.MainActivity;
import app.fil.market.Utils;


public class Ceni implements Parcelable {
    String naimenovanie;
    String foto;
    String id_sql_tovara_v_baze;
    String id_sql_tovara_v_korzine_pokupatelia;
    int kolihestvo=0;
    Double kolvovupakovke=1.0;
    Double cenaZaUpak =0.0 ;//SQL
    Double skidka =0.0 ;    //SQL
    Double cenaFinalSoSkidkoZaUpak =0.0 ; //SQL
    Double cenaZaOdinKg =0.0 ;
    Double cenaSoSkidkoyZaUpak =0.0;
    boolean vibranLiObj=false; //SQL
    String znahrazmriada = " ";
    String mestovilova = " ";
    String typeUpakovki = " ";
    String edinica_izmerenia_upakovki = " ";
    String sostoianieTovara = " ";
    String raskazotovare = " ";
    //for KorzinaActivity
    public Ceni(String naimenovanie,
                String fotoStr,
                String cenaStr,
                String skidkaStr,
                String cenaSoSkidkoyZaUpakStr,
                String vibranLiStr,
                String id_sql_tovara_v_bazeStr,
                String id_sql_tovara_v_korzine_pokupatelia,
                String kolihestvoStr,
                String kolvovupakovkeStr
    ) {
        this.naimenovanie = naimenovanie;
        this.id_sql_tovara_v_korzine_pokupatelia = id_sql_tovara_v_korzine_pokupatelia;
        this.id_sql_tovara_v_baze = id_sql_tovara_v_bazeStr;
        if (!cenaZaUpak.equals("null"))
            this.cenaZaUpak = Double.parseDouble(cenaStr);
        if (!skidka.equals("null"))
            this.skidka = Double.parseDouble(skidkaStr);
        if (!cenaFinalSoSkidkoZaUpak.equals("null"))
            this.cenaFinalSoSkidkoZaUpak = Double.parseDouble(cenaSoSkidkoyZaUpakStr);
        if (cenaZaUpak > 0.0) {
            if (cenaFinalSoSkidkoZaUpak == 0.0 & skidka > 0.0) cenaFinalSoSkidkoZaUpak = cenaZaUpak *(1-skidka/100);
            if ((cenaFinalSoSkidkoZaUpak > 0.0 & skidka == 0.0)||(cenaFinalSoSkidkoZaUpak > 0.0 & skidka > 0.0)) {
                skidka=100.0- cenaFinalSoSkidkoZaUpak / cenaZaUpak *100;
                System.out.println("kambal "+ Double.toString(skidka));
            }
            if (cenaFinalSoSkidkoZaUpak > 0.0 & skidka > 0.0) skidka=100.0- cenaFinalSoSkidkoZaUpak / cenaZaUpak *100;
            if (cenaFinalSoSkidkoZaUpak == 0.0 & skidka == 0.0) {
                skidka = 0.0;
                cenaFinalSoSkidkoZaUpak = cenaZaUpak;
            }
        } else{ // cenaZaUpak<0.0
            cenaZaUpak = cenaFinalSoSkidkoZaUpak /(1-skidka/100);
        }
        this.foto= Utils.BASE_IP + fotoStr;
        if(!vibranLiStr.equals("null")){
            vibranLiObj= Boolean.valueOf(vibranLiStr);
        }
        else{
            vibranLiObj=false;
        }
        if(!kolihestvoStr.equals("null")) kolihestvo= Integer.valueOf(kolihestvoStr);

        if(!kolvovupakovkeStr.equals("null")) kolvovupakovke= Double.valueOf(kolvovupakovkeStr);
        else kolvovupakovke=1.0;
        cenaZaOdinKg = cenaFinalSoSkidkoZaUpak /kolvovupakovke;
//        System.out.println(Double.toString(cenaZaUpak)+", "+ Double.toString(skidka)+", "+ Double.toString(cenaFinalSoSkidkoZaUpak)+", "
//                + Boolean.toString(vibranLi)+", "+ id_sql_tovara_v_baze +" - create objekt Ceni in Double");
//        System.out.println(Double.toString(cenaZaUpak)+", "+ Double.toString(skidka)+", "+ Double.toString(cenaFinalSoSkidkoZaUpak)+"sadfghjgfdafsdghfkkmyntrhg");
//        System.out.println("Ceni Id SQL "+" ID tovara v baze="+id_sql_tovara_v_baze+", "+
//                "ID tovara v korzinePokupatelia="+id_sql_tovara_v_korzine_pokupatelia);
    }

    //for TovariActivity
    public Ceni(String naimenovanieStr,
                String fotoStr,
                String cenaStr,
                String skidkaStr,
                String cenaSoSkidkoyZaUpakStr,
                String vibranLiStr,
                String id_sql_tovara_v_baze_Str,
                String id_sql_tovara_v_korzine_pokupatelia_Str,
                String kolihestvoStr,
                String kolvovupakovkeStr,
                String znahrazmriadaStr,
                String mestovilovaStr,
                String typeUpakovkiStr,
                String sostoianieTovaraStr,
                String edinica_izmerenia_upakovkiStr,
                String raskazotovareStr
    ){
        System.out.println("ceni konsruktor AA"+
                "\n"+naimenovanieStr+",<000naimenovanieStr000 "+
                "\n"+fotoStr+",<000fotoStr000 "+
                "\n"+cenaStr+",<000cenaStr000 "+
                "\n"+skidkaStr+",<000skidkaStr000 "+
                "\n"+cenaSoSkidkoyZaUpakStr+",<000cenaSoSkidkoyZaUpakStr000 "+
                "\n"+vibranLiStr+",<000vibranLiStr000 "+
                "\n"+id_sql_tovara_v_baze_Str+",<000id_sql_tovara_v_baze_Str000 "+
                "\n"+id_sql_tovara_v_korzine_pokupatelia_Str+",<000id_sql_tovara_v_korzine_pokupatelia_Str000 "+
                "\n"+kolihestvoStr+",<000kolihestvoStr000 "+
                "\n"+kolvovupakovkeStr+",<000kolvovupakovkeStr000 "+
                "\n"+znahrazmriadaStr+",<000znahrazmriadaStr000 "+
                "\n"+mestovilovaStr+",<000mestovilovaStr000 "+
                "\n"+typeUpakovkiStr+",<000typeUpakovkiStr000 "+
                "\n"+sostoianieTovaraStr+",<000sostoianieTovaraStr000 "+
                "\n"+edinica_izmerenia_upakovkiStr+",<000edinica_izmerenia_upakovkiStr000 "+
                "\n"+raskazotovareStr+",<000raskazotovareStr000 "
       );

        System.out.println(cenaStr+", "+skidkaStr+", "+cenaSoSkidkoyZaUpakStr+", "+vibranLiStr+", "+id_sql_tovara_v_baze_Str+", "+id_sql_tovara_v_korzine_pokupatelia_Str+" - create objekt Ceni");
        System.out.println(cenaStr+", "+skidkaStr+", "+cenaSoSkidkoyZaUpakStr+" - create objekt Ceni rashet skidki");
        if (!cenaZaUpak.equals("null"))
            this.cenaZaUpak = Double.parseDouble(cenaStr);
        if (!skidka.equals("null"))
            this.skidka = Double.parseDouble(skidkaStr);
        if (!cenaFinalSoSkidkoZaUpak.equals("null"))
            this.cenaFinalSoSkidkoZaUpak = Double.parseDouble(cenaSoSkidkoyZaUpakStr);
        if (cenaZaUpak > 0.0) {
            if (cenaFinalSoSkidkoZaUpak == 0.0 & skidka > 0.0) cenaFinalSoSkidkoZaUpak = cenaZaUpak *(1-skidka/100);
            if ((cenaFinalSoSkidkoZaUpak > 0.0 & skidka == 0.0)||(cenaFinalSoSkidkoZaUpak > 0.0 & skidka > 0.0)) {
                skidka=100.0- cenaFinalSoSkidkoZaUpak / cenaZaUpak *100;
                System.out.println("kambal "+ Double.toString(skidka));
            }
            if (cenaFinalSoSkidkoZaUpak > 0.0 & skidka > 0.0) skidka=100.0- cenaFinalSoSkidkoZaUpak / cenaZaUpak *100;
            if (cenaFinalSoSkidkoZaUpak == 0.0 & skidka == 0.0) {
                skidka = 0.0;
                cenaFinalSoSkidkoZaUpak = cenaZaUpak;
            }
        } else{ // cenaZaUpak<0.0
            cenaZaUpak = cenaFinalSoSkidkoZaUpak /(1-skidka/100);
        }
        this.foto= Utils.BASE_IP + fotoStr;
        if(!vibranLiStr.equals("null")){
            vibranLiObj= Boolean.valueOf(vibranLiStr);
        }
        else{
            vibranLiObj=false;
        }
        if(!kolihestvoStr.equals("null")) kolihestvo= Integer.valueOf(kolihestvoStr);

        if(!kolvovupakovkeStr.equals("null")) kolvovupakovke= Double.valueOf(kolvovupakovkeStr);
        else kolvovupakovke=1.0;
        cenaZaOdinKg = cenaFinalSoSkidkoZaUpak /kolvovupakovke;

        this.naimenovanie=naimenovanieStr;
        this.id_sql_tovara_v_korzine_pokupatelia=id_sql_tovara_v_korzine_pokupatelia_Str;
        this.id_sql_tovara_v_baze=id_sql_tovara_v_baze_Str;
        this.znahrazmriada=znahrazmriadaStr;
        this.mestovilova=mestovilovaStr;
        this.typeUpakovki=typeUpakovkiStr;
        this.edinica_izmerenia_upakovki=edinica_izmerenia_upakovkiStr;
        this.sostoianieTovara =sostoianieTovaraStr;
        this.raskazotovare=raskazotovareStr;
        System.out.println(Double.toString(cenaZaUpak)+", "+ Double.toString(skidka)+", "+ Double.toString(cenaFinalSoSkidkoZaUpak)+", "
                + Boolean.toString(vibranLiObj)+", "+ id_sql_tovara_v_baze +" - create objekt Ceni in Double");
        System.out.println(Double.toString(cenaZaUpak)+", "+ Double.toString(skidka)+", "+ Double.toString(cenaFinalSoSkidkoZaUpak)+"sadfghjgfdafsdghfkkmyntrhg");
        System.out.println("Ceni Id SQL "+" ID tovara v baze="+id_sql_tovara_v_baze+", "+
                "ID tovara v korzinePokupatelia="+id_sql_tovara_v_korzine_pokupatelia);
    }


    protected Ceni(Parcel in) {
        naimenovanie = in.readString();
        foto = in.readString();
        id_sql_tovara_v_baze = in.readString();
        id_sql_tovara_v_korzine_pokupatelia = in.readString();
        kolihestvo = in.readInt();
        if (in.readByte() == 0) {
            kolvovupakovke = null;
        } else {
            kolvovupakovke = in.readDouble();
        }
        if (in.readByte() == 0) {
            cenaZaUpak = null;
        } else {
            cenaZaUpak = in.readDouble();
        }
        if (in.readByte() == 0) {
            cenaZaOdinKg = null;
        } else {
            cenaZaOdinKg = in.readDouble();
        }
        if (in.readByte() == 0) {
            skidka = null;
        } else {
            skidka = in.readDouble();
        }
        if (in.readByte() == 0) {
            cenaSoSkidkoyZaUpak = null;
        } else {
            cenaSoSkidkoyZaUpak = in.readDouble();
        }
        if (in.readByte() == 0) {
            cenaFinalSoSkidkoZaUpak = null;
        } else {
            cenaFinalSoSkidkoZaUpak = in.readDouble();
        }
        vibranLiObj = in.readByte() != 0;
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

    public Double getCenaZaUpak() {
            return cenaZaUpak;
    }
    public String getCenaStr() {
        return Utils.getStringFromDoubleFormated2Zerro(cenaZaUpak);
    }

    public Double getCenaZaOdinKg() {
        return cenaZaOdinKg;
    }

    public String getEdinica_izmerenia_upakovki() {
        return edinica_izmerenia_upakovki;
    }

    public String getTypeUpakovki() {
        return typeUpakovki;
    }

    public String getCenaZaOdinKgStr() {
        return Utils.getStringFromDoubleFormated2Zerro(cenaZaOdinKg);
    }
    public Double getSkidka() {
        return skidka;
    }
    public String getSkidkaStr() {
        return Utils.getStringFromDoubleFormated0Zerro(skidka);
    }
    public boolean getIsSelected() {
        return vibranLiObj;
    }
    public String getId_sql_tovara_v_baze() {
        return id_sql_tovara_v_baze;
    }
    public String getId_sql_tovara_v_korzine_pokupatelia() {
        return id_sql_tovara_v_korzine_pokupatelia;
    }
    public int getKolihestvo() {
        return kolihestvo;
    }
    public String getKolihestvoStr() {
        return Integer.toString(kolihestvo);
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
    public void setKolihestvoAndSendToSQL(int kolihestvo) {
        this.kolihestvo = kolihestvo;
        buySQLKolvo(id_sql_tovara_v_baze, Integer.toString(kolihestvo));
    }
    public void setKolihestvoStr(String kolihestvoStr) {
        setKolihestvoAndSendToSQL(Integer.valueOf(kolihestvoStr));
    }
    public Double getCenaFinalSoSkidkoyZaUpak() {
        return cenaFinalSoSkidkoZaUpak;
    }
    public String getCenaFinalSoSkidkoyZaUpakStr() {
        return Utils.getStringFromDoubleFormated2Zerro(cenaFinalSoSkidkoZaUpak);
    }
    public void setVibranLi(boolean vibranLi) {
        this.vibranLiObj = vibranLi;
        System.out.println("setVibranLi");
        sendSQLTovarVibran(id_sql_tovara_v_korzine_pokupatelia, vibranLi);

    }

    void sendSQLTovarVibran(final String tovarId_V_Korzine_klienta, final boolean vibranLiBool) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.FALSE_SELECTED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            System.out.println("sendSQLTovarVibran buySQLKolvo = " + jsonObject.toString());

                        } catch (JSONException e) {

                            System.out.println("\n ERR sendSQLTovarVibran buySQLKolvo = " + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("\n ERR sendSQLTovarVibran buySQLKolvo = " + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("idKorzina", tovarId_V_Korzine_klienta);
                parameters.put("isSelected", Boolean.toString(vibranLiBool));
                System.out.println("Ceni sendSQLTovarVibran SQL parametrs = " + parameters);
                return parameters;
            }
        };
        MainActivity.requestQueue.add(stringRequest);
    }
    void buySQLKolvo(final String tovarId_V_Baze_tovarov_SQL, final String kolvo) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.BUY_KOLVO_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int idKorzina = jsonObject.getInt("idKorzina");
                            MainActivity.userStatic.setKorzinaCountStr(idKorzina);
                            System.out.println("Ceni sendSQLTovarVibran = " + jsonObject.toString()+"\n" +
                                    "Main kol="+MainActivity.userStatic.getKorzina_kountInt());
                            JSONArray jsonArray = jsonObject.getJSONArray("count");

                        } catch (JSONException e) {

                            System.out.println("\n Ceni ERR sendSQLTovarVibran = " + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("\n Ceni ERR sendSQLTovarVibran - " + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("tovar", tovarId_V_Baze_tovarov_SQL);
                parameters.put("kolvo", kolvo);
                parameters.put("pokupatel", MainActivity.userStatic.getSqlId());
                System.out.println("Ceni sendSQLTovarVibran  SQL* parametrs = " + parameters);



                return parameters;
            }
        };

        MainActivity.requestQueue.add(stringRequest);
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
        parcel.writeInt(kolihestvo);
        if (kolvovupakovke == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(kolvovupakovke);
        }
        if (cenaZaUpak == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(cenaZaUpak);
        }
        if (cenaZaOdinKg == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(cenaZaOdinKg);
        }
        if (skidka == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(skidka);
        }
        if (cenaSoSkidkoyZaUpak == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(cenaSoSkidkoyZaUpak);
        }
        if (cenaFinalSoSkidkoZaUpak == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(cenaFinalSoSkidkoZaUpak);
        }
        parcel.writeByte((byte) (vibranLiObj ? 1 : 0));
        parcel.writeString(znahrazmriada);
        parcel.writeString(mestovilova);
        parcel.writeString(typeUpakovki);
        parcel.writeString(edinica_izmerenia_upakovki);
        parcel.writeString(sostoianieTovara);
        parcel.writeString(raskazotovare);
    }
}
