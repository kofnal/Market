package app.fil.market.ui.tovari;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.fil.market.BokovoeMenu;
import app.fil.market.MainActivity;
import app.fil.market.Model.Utils;



public class TovarFromSQL
//        implements Parcelable
{
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
    public boolean estLi_V_KorzineObj=false; //SQL
    String znahrazmriada = " ";
    String mestovilova = " ";
    String typeUpakovki = " ";
    String edinica_izmerenia_upakovki = " ";
    String sostoianieTovara = " ";
    String raskazotovare = " ";
    //for KorzinaActivity
    public TovarFromSQL(String naimenovanie,
                        String fotoStr,
                        String cenaStr,
                        String skidkaStr,
                        String cenaSoSkidkoyZaUpakStr,
                        String vibranLiStr,
                        String id_sql_tovara_v_bazeStr,
                        String id_sql_tovara_v_korzine_pokupateliaStr,
                        String kolihestvoStr,
                        String kolvovupakovkeStr
    ) {
        this.naimenovanie = naimenovanie;
        this.id_sql_tovara_v_korzine_pokupatelia = id_sql_tovara_v_korzine_pokupatelia;
        this.id_sql_tovara_v_baze = id_sql_tovara_v_bazeStr;
        if (!cenaZaUpak.equals("null"))
            this.cenaZaUpak = Double.parseDouble(cenaStr);
//            this.cenaZaUpak = Double.parseDouble("1.0");
        if (!skidka.equals("null"))
//            this.skidka = Double.parseDouble("1.0");
            this.skidka = Double.parseDouble(skidkaStr);
        if (!cenaFinalSoSkidkoZaUpak.equals("null"))
//            this.cenaFinalSoSkidkoZaUpak = Double.parseDouble("1.0");
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
        if(id_sql_tovara_v_korzine_pokupateliaStr.equals("null")){
            estLi_V_KorzineObj= false;
        }
        else{
            estLi_V_KorzineObj=true;
        }
        if(!kolihestvoStr.equals("null")) kolihestvo= Integer.valueOf(kolihestvoStr);

        if(!kolvovupakovkeStr.equals("null")) kolvovupakovke= Double.valueOf(kolvovupakovkeStr);
        else kolvovupakovke=1.0;
        cenaZaOdinKg = cenaFinalSoSkidkoZaUpak /kolvovupakovke;
//        System.out.println(Double.toString(cenaZaUpak)+", "+ Double.toString(skidka)+", "+ Double.toString(cenaFinalSoSkidkoZaUpak)+", "
//                + Boolean.toString(vibranLi)+", "+ id_sql_tovara_v_baze +" - create objekt TovarFromSQL in Double");
//        System.out.println(Double.toString(cenaZaUpak)+", "+ Double.toString(skidka)+", "+ Double.toString(cenaFinalSoSkidkoZaUpak)+"sadfghjgfdafsdghfkkmyntrhg");
//        System.out.println("TovarFromSQL Id SQL "+" ID tovara v baze="+id_sql_tovara_v_baze+", "+
//                "ID tovara v korzinePokupatelia="+id_sql_tovara_v_korzine_pokupatelia);
    }

    //for TovariActivity
    public TovarFromSQL(String naimenovanieStr,
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

        System.out.println(cenaStr+", "+skidkaStr+", "+cenaSoSkidkoyZaUpakStr+", "+vibranLiStr+", "+id_sql_tovara_v_baze_Str+", "+id_sql_tovara_v_korzine_pokupatelia_Str+" - create objekt TovarFromSQL");
        System.out.println(cenaStr+", "+skidkaStr+", "+cenaSoSkidkoyZaUpakStr+" - create objekt TovarFromSQL rashet skidki");
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
        if(id_sql_tovara_v_korzine_pokupatelia_Str.equals("null")){
            estLi_V_KorzineObj= false;
        }
        else{
            estLi_V_KorzineObj=true;
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
                + Boolean.toString(vibranLiObj)+", "+ id_sql_tovara_v_baze +" - create objekt TovarFromSQL in Double");
        System.out.println(Double.toString(cenaZaUpak)+", "+ Double.toString(skidka)+", "+ Double.toString(cenaFinalSoSkidkoZaUpak)+"sadfghjgfdafsdghfkkmyntrhg");
        System.out.println("TovarFromSQL Id SQL "+" ID tovara v baze="+id_sql_tovara_v_baze+", "+
                "ID tovara v korzinePokupatelia="+id_sql_tovara_v_korzine_pokupatelia);
    }



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
    public void setVibranLiAndSendSQL(boolean vibranLi) {
        this.vibranLiObj = vibranLi;
        System.out.println("setVibranLiAndSendSQL");
        sendSQLTovarVibran(id_sql_tovara_v_korzine_pokupatelia, vibranLi);

    }

    public boolean isEstLi_V_KorzineObj() {
        return estLi_V_KorzineObj;
    }

    public void setEstLi_V_KorzineObj(boolean estLi_V_KorzineObj) {
        this.estLi_V_KorzineObj = estLi_V_KorzineObj;
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
                System.out.println("TovarFromSQL sendSQLTovarVibran SQL parametrs = " + parameters);
                return parameters;
            }
        };
        BokovoeMenu.requestQueue.add(stringRequest);
    }
    void buySQLKolvo(final String tovarId_V_Baze_tovarov_SQL, final String kolvo) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.BUY_KOLVO_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int idKorzina = jsonObject.getInt("idKorzina");
                            MainActivity.pokupatelStatic.setKorzinaCountStr(idKorzina);
                            System.out.println("TovarFromSQL sendSQLTovarVibran = " + jsonObject.toString()+"\n" +
                                    "Main kol="+MainActivity.pokupatelStatic.getKorzina_kountInt());
                            JSONArray jsonArray = jsonObject.getJSONArray("count");

                        } catch (JSONException e) {

                            System.out.println("\n TovarFromSQL ERR sendSQLTovarVibran = " + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("\n TovarFromSQL ERR sendSQLTovarVibran - " + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("tovar", tovarId_V_Baze_tovarov_SQL);
                parameters.put("kolvo", kolvo);
                parameters.put("pokupatel", MainActivity.pokupatelStatic.getSqlId());
                System.out.println("TovarFromSQL sendSQLTovarVibran  SQL* parametrs = " + parameters);



                return parameters;
            }
        };

        BokovoeMenu.requestQueue.add(stringRequest);
    }

}
