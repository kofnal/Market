package app.fil.market.ceni_i_skidki;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
    int kolihestvo=1;
    Double kolvovupakovke=1.0;
    Double cenaZaOdinKg =0.0 ;
    Double cenaZaUpakovky =0.0 ;
    Double skidka =0.0 ;
    Double cenaSoSkidkoy=0.0;
    Double cenaFinalSkidkaZaOdinKg =0.0 ;
    boolean vibranLiObj=false;
    String znahrazmriada = " ";
    String mestovilova = " ";
    String typeUpakovki = " ";
    String edinica_izmerenia_upakovki = " ";
    String sostoianieTovara = " ";
    String raskazotovare = " ";

    //for KorzinaActivity


    public Ceni(String naimenovanie,
                String fotoStr,
                String cenaZaOdinKgStr,
                String skidkaStr,
                String cenaSoSkidkoyStr,
                String vibranLiStr,
                String id_sql_tovara_v_bazeStr,
                String id_sql_tovara_v_korzine_pokupatelia,
                String kolihestvoStr,
                String kolvovupakovkeStr
    ) {
        this.naimenovanie = naimenovanie;
        this.id_sql_tovara_v_korzine_pokupatelia = id_sql_tovara_v_korzine_pokupatelia;
        this.id_sql_tovara_v_baze = id_sql_tovara_v_bazeStr;
        if (!cenaZaOdinKg.equals("null"))
            this.cenaZaOdinKg = Double.parseDouble(cenaZaOdinKgStr);
        if (!skidka.equals("null"))
            this.skidka = Double.parseDouble(skidkaStr);
        if (!cenaFinalSkidkaZaOdinKg.equals("null"))
            this.cenaFinalSkidkaZaOdinKg = Double.parseDouble(cenaSoSkidkoyStr);
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
        this.foto= Utils.BASE_IP + fotoStr;
        if(!vibranLiStr.equals("null")){
            vibranLiObj= Boolean.valueOf(vibranLiStr);
        }
        else{
            vibranLiObj=false;
        }
        if(!kolihestvoStr.equals("null")) kolihestvo= Integer.valueOf(kolihestvoStr);
        else kolihestvo=0;
        if(!kolvovupakovkeStr.equals("null")) kolvovupakovke= Double.valueOf(kolvovupakovkeStr);
        else kolvovupakovke=1.0;
        cenaZaUpakovky=cenaFinalSkidkaZaOdinKg*kolvovupakovke;
//        System.out.println(Double.toString(cenaZaOdinKg)+", "+ Double.toString(skidka)+", "+ Double.toString(cenaFinalSkidkaZaOdinKg)+", "
//                + Boolean.toString(vibranLi)+", "+ id_sql_tovara_v_baze +" - create objekt Ceni in Double");
//        System.out.println(Double.toString(cenaZaOdinKg)+", "+ Double.toString(skidka)+", "+ Double.toString(cenaFinalSkidkaZaOdinKg)+"sadfghjgfdafsdghfkkmyntrhg");
//        System.out.println("Ceni Id SQL "+" ID tovara v baze="+id_sql_tovara_v_baze+", "+
//                "ID tovara v korzinePokupatelia="+id_sql_tovara_v_korzine_pokupatelia);
    }

    //for TovariActivity
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
        if(!vibranLiStr.equals("null")){
            vibranLiObj= Boolean.valueOf(vibranLiStr);
        }
        else {
            vibranLiObj=false;
        }
        id_sql_tovara_v_baze =id_sql_tovara_v_baze_Str;
        id_sql_tovara_v_korzine_pokupatelia =id_sql_tovara_v_korzine_pokupatelia_Str;
        if(!kolihestvoStr.equals("null")) kolihestvo= Integer.valueOf(kolihestvoStr);
        else kolihestvo=0;
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
                + Boolean.toString(vibranLiObj)+", "+ id_sql_tovara_v_baze +" - create objekt Ceni in Double");
        System.out.println(Double.toString(cenaZaOdinKg)+", "+ Double.toString(skidka)+", "+ Double.toString(cenaFinalSkidkaZaOdinKg)+"sadfghjgfdafsdghfkkmyntrhg");
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
            cenaSoSkidkoy = null;
        } else {
            cenaSoSkidkoy = in.readDouble();
        }
        if (in.readByte() == 0) {
            cenaFinalSkidkaZaOdinKg = null;
        } else {
            cenaFinalSkidkaZaOdinKg = in.readDouble();
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

    public Double getCenaZaOdinKg() {
            return cenaZaOdinKg;
    }
    public String getCenaStr() {
        return Utils.getStringFromDoubleFormated2Zerro(cenaZaOdinKg);
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
        return Utils.getStringFromDoubleFormated2Zerro(cenaZaUpakovky);
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
    public void setKolihestvo(int kolihestvo) {
        this.kolihestvo = kolihestvo;
        buySQLKolvo(id_sql_tovara_v_baze, Integer.toString(kolihestvo));
    }
    public void setKolihestvoStr(String kolihestvoStr) {
        this.kolihestvo = Integer.valueOf(kolihestvoStr);
        buySQLKolvo(id_sql_tovara_v_baze, kolihestvoStr);
    }
    public Double getCenaFinalSoSkidkoy() {
        return cenaFinalSkidkaZaOdinKg;
    }
    public String getCenaFinalSoSkidkoyStr() {
        return Utils.getStringFromDoubleFormated2Zerro(cenaFinalSkidkaZaOdinKg);
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
                            System.out.println("Ceni sendSQLTovarVibran = " + jsonObject.toString());
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
                System.out.println("Ceni sendSQLTovarVibran  SQL parametrs = " + parameters);



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
        if (cenaSoSkidkoy == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(cenaSoSkidkoy);
        }
        if (cenaFinalSkidkaZaOdinKg == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(cenaFinalSkidkaZaOdinKg);
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
