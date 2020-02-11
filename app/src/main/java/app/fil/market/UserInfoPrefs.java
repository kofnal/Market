package app.fil.market;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

public class UserInfoPrefs {
    private static final String TAG = UserSession.class.getSimpleName();
    private static final String PREF_NAME_TAG = "userinfo";
    private static final String KEY_USERNAME_TAG = "username";
    private static final String KEY_EMAIL_TAG = "email";
    private static final String KEY_TEL_TAG = "tel";
    private static final String KEY_ID_SQL_TAG = "id_sql";
    private static final String KEY_ID_GOOG_TAG = "id_goog";
    private static final String KEY_FIO_TAG = "fio_string";
    private static final String KEY_EMAIL_STRING_TAG = "email_string";
    private static final String KEY_TEL_STRING_TAG = "tel_string";
    private static final String KEY_ADRES_TAG = "adres_string";
    private static final String KEY_KOMENT_TAG = "koment_string";
    private static final String KEY_KORZINA_COUNT_TAG = "korzina_kount_string";
    private static  int korzina_count_INT = 0;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public UserInfoPrefs(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(PREF_NAME_TAG, ctx.MODE_PRIVATE);
        editor = prefs.edit();
    }
    public void setIdSQL(String idSQL){
        System.out.println("UserInfoPrefs SETSQL ID = " +idSQL);
        editor.putString(KEY_ID_SQL_TAG, idSQL);
        editor.apply();
    }
    public void setIdGoog(String idGoog){
        editor.putString(KEY_ID_GOOG_TAG, idGoog);
        editor.apply();
    }
    public void setUsername(String username){
        editor.putString(KEY_USERNAME_TAG, username);
        editor.apply();
    }
    public void setEmail(String email) {
        editor.putString(KEY_EMAIL_TAG, email);
        editor.apply();
    }
    public void setTel(String tel){
        editor.putString(KEY_TEL_TAG, tel);
        editor.apply();
    }
    public void setFioStr(String fioStr){
        editor.putString(KEY_FIO_TAG, fioStr);
        editor.apply();
    }
    public void setEmailStr(String emailStr){
        editor.putString(KEY_EMAIL_STRING_TAG, emailStr);
        editor.apply();
    }
    public void setTelStr(String telStr){
        editor.putString(KEY_TEL_STRING_TAG, telStr);
        editor.apply();
    }
    public void setAdresStr(String adresStr){
        editor.putString(KEY_ADRES_TAG, adresStr);
        editor.apply();
    }
    public void setKomentStr(String komentStr){
        editor.putString(KEY_KOMENT_TAG, komentStr);
        editor.apply();
    }
    public void setKorzinaCountStr(String korzinaCountStr, TextView tvKorzinaCount){
        try {

            if(!korzinaCountStr.equals("0")&&!korzinaCountStr.equals("null")&&!korzinaCountStr.equals("")&&korzinaCountStr!=null){
                System.out.println("sendZakaz "+"setKorzinaCountStr IF "+"count="+korzinaCountStr);
                korzina_count_INT= Integer.valueOf(korzinaCountStr);
                tvKorzinaCount.setText(korzinaCountStr);
            }else if (korzinaCountStr.equals("0")){
                System.out.println("sendZakaz "+"setKorzinaCountStr ELSE IF "+"count="+korzinaCountStr);
                korzina_count_INT= 0;
                tvKorzinaCount.setVisibility(View.GONE);
            }else{
                System.out.println("sendZakaz "+"setKorzinaCountStr ELSE "+"count="+korzinaCountStr);
                tvKorzinaCount.setVisibility(View.GONE);
            }
        }catch (Exception e){
            System.out.println("sendZakaz "+"setKorzinaCountStr ERROR "+"count="+korzinaCountStr+" INT="+Integer.toString(korzina_count_INT)+" err-->"+e.toString());

        };
        editor.putString(KEY_KORZINA_COUNT_TAG, korzinaCountStr);
        editor.apply();
        System.out.println("sendZakaz "+"setKorzinaCountStr "+"count="+korzinaCountStr+" INT="+Integer.toString(korzina_count_INT));
    }
    public void setkorzina_count_INT(int korz_count, TextView tvKorzCount){
        setKorzinaCountStr(Integer.toString(korz_count), tvKorzCount);
    }
    public void clearUserInfo(){
        editor.clear();
        editor.commit();
    }

    public String getKeyUsername(){
        return prefs.getString(KEY_USERNAME_TAG, "");}


    public String getKeyEmail(){return prefs.getString(KEY_EMAIL_TAG, "");}
    public String getTel(){return prefs.getString(KEY_TEL_TAG, "");}
    public String getFioStr(){return prefs.getString(  KEY_FIO_TAG , "");}
    public String getEmailStr(){return prefs.getString( KEY_EMAIL_STRING_TAG , "");}
    public String getTelStr(){return prefs.getString(  KEY_TEL_STRING_TAG , "");}
    public String getAdresStr(){return prefs.getString(  KEY_ADRES_TAG , "");}
    public String getKomentStr(){return prefs.getString(  KEY_KOMENT_TAG , "");}
//    public String getKorzinaCountStr(){return "2";}
    public String getKorzinaCountStr(){return prefs.getString(  KEY_KORZINA_COUNT_TAG , "");}
    public String getSqlId(){
        System.out.println("UserInfoPrefs GetSQL ID = " + prefs.getString(KEY_ID_SQL_TAG, ""));
        return prefs.getString(KEY_ID_SQL_TAG, "");}
    public String getGoogId(){return prefs.getString(KEY_ID_GOOG_TAG, "");}

    public int getKorzina_count_INT() {
        return korzina_count_INT;
    }

}