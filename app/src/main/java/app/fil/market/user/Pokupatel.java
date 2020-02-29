package app.fil.market.user;


import android.content.Context;



public class Pokupatel {
            private String fioUser=" ";
            private String email=" ";
            private String tel=" ";
            private String id_sql=" ";
            private String id_goog=" ";
            private String adres_string=" ";
            private String koment_string=" ";
            private int korzina_kountInt=0;
            PokupatelInfoPrefs pokupatelPrefs;

    public Pokupatel(Context context) {
        pokupatelPrefs = new PokupatelInfoPrefs(context);
        try {
            this.fioUser = pokupatelPrefs.getFioStr();
            this.email = pokupatelPrefs.getEmailStr();
            this.tel = pokupatelPrefs.getTelStr();
            this.id_sql = pokupatelPrefs.getSqlId();
            this.id_goog = pokupatelPrefs.getGoogId();
            this.adres_string = pokupatelPrefs.getAdresStr();
            this.koment_string = pokupatelPrefs.getKomentStr();
            this.korzina_kountInt = Integer.valueOf(pokupatelPrefs.getKorzinaCountStr());
        }catch (Exception e){
            System.out.println("err create pokupatel "+e.toString());
        }
    }
public void wreateDataToPrefs(){
        System.out.println("wreateDataToPrefs");
        pokupatelPrefs.saveFioStr(fioUser);
        pokupatelPrefs.saveEmailStr(email);
        pokupatelPrefs.saveTelStr(tel);
        pokupatelPrefs.saveIdSQL(id_sql);
        pokupatelPrefs.saveIdGoog(id_goog);
        pokupatelPrefs.saveAdresStr(adres_string);
        pokupatelPrefs.saveKomentStr(koment_string);
        pokupatelPrefs.saveKorzinaCountStr(Integer.toString(korzina_kountInt));
}
    public String getFioUser() {
        return fioUser;
    }

    public void setFioUser(String fioUser) {
        System.out.println("setFioUser "+fioUser);
        this.fioUser = fioUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
//        pokupatelPrefs.saveEmailStr(email);
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        System.out.println("setTel "+tel);
//        pokupatelPrefs.saveTelStr(tel);
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

    public void setKorzinaCountStr(int korzina_kountInt) {
        this.korzina_kountInt = korzina_kountInt;
        updateTextViewTotalKorzinaCount();
        pokupatelPrefs.saveKorzinaCountStr(Integer.toString(korzina_kountInt));
        System.out.println("setKorzinaCount INT= "+korzina_kountInt);
    }


    public void setKorzinaCountStr(String korzina_kountStr) {
        updateTextViewTotalKorzinaCount();
        if (korzina_kountStr.matches("[-+]?\\d+")){
            setKorzinaCountStr(Integer.valueOf(korzina_kountStr));
        }else{
            setKorzinaCountStr(0);
        }
        System.out.println("setKorzinaCount = "+korzina_kountInt);
    }


    public void updateTextViewTotalKorzinaCount(){
        if (korzina_kountInt>0){
//            BokovoeMenu.tvKorzinaCount.setVisibility(View.VISIBLE);
//            BokovoeMenu.tvKorzinaCount.setText(Integer.toString(korzina_kountInt));
            System.out.println("setKorzinaCount "+"updateTextViewTotalKorzinaCount="+korzina_kountInt);
        } else{
//            BokovoeMenu.tvKorzinaCount.setVisibility(View.GONE);
        }
    }
}
