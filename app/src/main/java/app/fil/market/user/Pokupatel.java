package app.fil.market.user;


import android.app.Activity;
import android.view.View;
import android.widget.TextView;

public class Pokupatel {
            private String fioUser=" ";
            private String email=" ";
            private String tel=" ";
            private String id_sql=" ";
            private String id_goog=" ";
            private String adres_string=" ";
            private String koment_string=" ";
            private int korzina_kountInt=0;
            PokupatelInfoPrefs user;

    public Pokupatel(Activity context) {
        user = new PokupatelInfoPrefs(context);
        try {
            this.fioUser = user.getFioStr();
            this.email = user.getEmailStr();
            this.tel = user.getTelStr();
            this.id_sql = user.getSqlId();
            this.id_goog = user.getGoogId();
            this.adres_string = user.getAdresStr();
            this.koment_string = user.getKomentStr();
            this.korzina_kountInt = Integer.valueOf(user.getKorzinaCountStr());
        }catch (Exception e){
            System.out.println("err create pokupatel "+e.toString());
        }
    }
public void wreateDataToPrefs(){
        user.setFioStr(fioUser);
        user.setEmailStr(email);
        user.setTelStr(tel);
        user.setIdSQL(id_sql);
        user.setIdGoog(id_goog);
        user.setAdresStr(adres_string);
        user.setKomentStr(koment_string);
        user.setKorzinaCountStr(Integer.toString(korzina_kountInt));
}
    public String getFioUser() {
        return fioUser;
    }

    public void setFioUser(String fioUser) {
        this.fioUser = fioUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSqlId() {
        return id_sql;
    }

    public void setId_sql(String id_sql) {
        this.id_sql = id_sql;
    }

    public String getId_goog() {
        return id_goog;
    }

    public void setId_goog(String id_goog) {
        this.id_goog = id_goog;
    }



    public String getAdres_string() {
        return adres_string;
    }

    public void setAdres_string(String adres_string) {
        this.adres_string = adres_string;
    }

    public String getKoment_string() {
        return koment_string;
    }

    public void setKoment_string(String koment_string) {
        this.koment_string = koment_string;
    }

    public int getKorzina_kountInt() {
        return korzina_kountInt;
    }

    public void setKorzinaCountStr(int korzina_kountInt, TextView tv) {
        setKorzinaCountStr(korzina_kountInt);
        updateTextViewTotalKorzinaCount(tv);
    }
    public void setKorzinaCountStr(int korzina_kountInt) {
        this.korzina_kountInt = korzina_kountInt;
        user.setKorzinaCountStr(Integer.toString(korzina_kountInt));
        System.out.println("setKorzinaCount = "+korzina_kountInt);
    }

    public void setKorzinaCountStr(String korzina_kountStr, TextView tv) {
        setKorzinaCountStr(korzina_kountStr);
        updateTextViewTotalKorzinaCount(tv);
    }
    public void setKorzinaCountStr(String korzina_kountStr) {
        if (korzina_kountStr.matches("[-+]?\\d+")){
            setKorzinaCountStr(Integer.valueOf(korzina_kountStr));
        }else{
            setKorzinaCountStr(0);
        }
        System.out.println("setKorzinaCount = "+korzina_kountInt);
    }


    public void updateTextViewTotalKorzinaCount(TextView tv){
        if (korzina_kountInt>0){
            tv.setVisibility(View.VISIBLE);
            tv.setText(Integer.toString(korzina_kountInt));
            System.out.println("setKorzinaCount "+"updateTextViewTotalKorzinaCount="+korzina_kountInt);
        } else{
            tv.setVisibility(View.GONE);
        }
    }
}
