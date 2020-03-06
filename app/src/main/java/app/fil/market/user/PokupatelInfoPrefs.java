package app.fil.market.user;

import android.content.Context;
import android.content.SharedPreferences;

public class PokupatelInfoPrefs {
    private static final String TAG = UserSession.class.getSimpleName();
    private static final String PREF_NAME_TAG = "userinfo";
    private static final String KEY_ID_SQL_TAG = "id_sql";
    private static final String KEY_ID_GOOG_TAG = "id_goog";
    private static final String KEY_FIO_TAG = "fio_string";
    private static final String KEY_EMAIL_STRING_TAG = "email_string";
    private static final String KEY_TEL_STRING_TAG = "tel_string";
    private static final String KEY_ADRES_TAG = "adres_string";
    private static final String KEY_KOMENT_TAG = "koment_string";
    private static final String KEY_KORZINA_COUNT_TAG = "korzina_kount_string";

//    static  int korzina_count_INT = 0;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public PokupatelInfoPrefs(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(PREF_NAME_TAG, ctx.MODE_PRIVATE);
        editor = prefs.edit();
    }
    public void saveIdSQL(String idSQL){
        System.out.println("PoiskObjPrefs SETSQL ID = " +idSQL);
        editor.putString(KEY_ID_SQL_TAG, idSQL);
        editor.apply();
    }
    public void saveIdGoog(String idGoog){
        editor.putString(KEY_ID_GOOG_TAG, idGoog);
        editor.apply();
    }


    public void saveFioStr(String fioStr){
        System.out.println("PoiskObjPrefs saveFioStr = " +fioStr);
        editor.putString(KEY_FIO_TAG, fioStr);
        editor.apply();
    }
    public void saveEmailStr(String emailStr){
        editor.putString(KEY_EMAIL_STRING_TAG, emailStr);
        editor.apply();
    }
    public void saveTelStr(String telStr){
        editor.putString(KEY_TEL_STRING_TAG, telStr);
        editor.apply();
    }
    public void saveAdresStr(String adresStr){
        editor.putString(KEY_ADRES_TAG, adresStr);
        editor.apply();
    }
    public void saveKomentStr(String komentStr){
        editor.putString(KEY_KOMENT_TAG, komentStr);
        editor.apply();
    }

//    public void setKorzinaCountStrWithTV(String korzinaCountStr, TextView tv){
//        saveKorzinaCountStr(korzinaCountStr);
//        if(korzina_count_INT>0){
//            tv.setVisibility(View.VISIBLE);
//            tv.setText(korzina_count_INT);
//        }else{
//            tv.setVisibility(View.GONE);
//        }
//
//    }
    public void saveKorzinaCountStr(String korzinaCountStr) {

        editor.putString(KEY_KORZINA_COUNT_TAG, korzinaCountStr);
        editor.apply();
        System.out.println("saveKorzinaCountStr "+"count="+korzinaCountStr);
    }
//    public void saveKorzinaCountStr(String korzinaCountStr){
//        try {
//            //if(korzina_sum.matches("[-+]?\\d+")){
//            if(!korzinaCountStr.equals("0")&&!korzinaCountStr.equals("null")&&!korzinaCountStr.equals("")&&korzinaCountStr!=null){
//                System.out.println("sendZakaz "+"saveKorzinaCountStr IF "+"count="+korzinaCountStr);
//                korzina_count_INT= Integer.valueOf(korzinaCountStr);
//            }else if (korzinaCountStr.equals("0")){
//                System.out.println("sendZakaz "+"saveKorzinaCountStr ELSE IF "+"count="+korzinaCountStr);
//                korzina_count_INT= 0;
//            }else{
//                System.out.println("sendZakaz "+"saveKorzinaCountStr ELSE "+"count="+korzinaCountStr);
//            }
//        }catch (Exception e){
//            System.out.println("sendZakaz "+"saveKorzinaCountStr ERRO8R "+"count="+korzinaCountStr+" INT="+Integer.toString(korzina_count_INT)+" err-->"+e.toString());
//
//        }
//        editor.putString(KEY_KORZINA_COUNT_TAG, korzinaCountStr);
//        editor.apply();
//        System.out.println("sendZakaz "+"saveKorzinaCountStr "+"count="+korzinaCountStr+" INT="+Integer.toString(korzina_count_INT));
//    }
//
//    public void setkorzina_count_INT(int korz_count){
//        saveKorzinaCountStr(Integer.toString(korz_count));
//    }
    public void clearUserInfo(){
        editor.clear();
        editor.commit();
    }
//
//    public String getKeyUsername(){
//        return prefs.getString(KEY_USERNAME_TAG, "");}


//    public String getKeyEmail(){return prefs.getString(KEY_EMAIL_TAG, "");}
    public String getFioStr(){return prefs.getString(  KEY_FIO_TAG , "");}
    public String getEmailStr(){return prefs.getString( KEY_EMAIL_STRING_TAG , "");}
    public String getTelStr(){return prefs.getString(  KEY_TEL_STRING_TAG , "");}
    public String getAdresStr(){return prefs.getString(  KEY_ADRES_TAG , "");}
    public String getKomentStr(){return prefs.getString(  KEY_KOMENT_TAG , "");}
//    public String getKorzinaCountStr(){return "2";}
    public String getKorzinaCountStr(){return prefs.getString(  KEY_KORZINA_COUNT_TAG , "");}
    public String getSqlId(){
        System.out.println("PoiskObjPrefs GetSQL ID = " + prefs.getString(KEY_ID_SQL_TAG, ""));
        return prefs.getString(KEY_ID_SQL_TAG, "");}
    public String getGoogId(){return prefs.getString(KEY_ID_GOOG_TAG, "");}

//    public int getKorzina_count_INT() {
//        return korzina_count_INT;
//    }

}