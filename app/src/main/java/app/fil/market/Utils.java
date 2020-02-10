package app.fil.market;

public class Utils {
    public static final String BASE_IP = "http://34.65.239.89/googledrive/android/market/";
    public static final String SHOW_KATEGORIA = BASE_IP + "showKategoria.php";
    public static final String SHOW_PODKATEGORIA = BASE_IP + "showPodkat.php";
    public static final String SHOW_TOVAR = BASE_IP + "showTovar.php";
    public static final String SHOW_TOVAR_OPISANIE = BASE_IP + "showTovarOpisanie.php";
    public static final String SHOW_KORZINA = BASE_IP + "showKorzina.php";
    public static final String SHOW_NA_OPLATU = BASE_IP + "showNaOplatu.php";
    public static final String SHOW_NA_OPLATU_POVTORNO = BASE_IP + "showNaOplatuPovtorno.php";
    public static final String SHOW_ZAKAZI = BASE_IP + "showZakazi.php";
    public static final String SHOW_ZAKAZI_SCROL = BASE_IP + "showZakaziScrol.php";
    public static final String SEND_ZAKAZ = BASE_IP + "sendZakaz.php";
    public static final String SHOW_ADRESA = BASE_IP + "showAdresaDostavki.php";
    public static final String SHOW_ADRESA_PUNKTOV = BASE_IP + "showAdresaPunktov.php";
    public static final String GHANGE_GLAVNIY_ADRES = BASE_IP + "setGlavniyAdres.php";
    public static final String SEND_ADRES = BASE_IP + "insertAdresPokupatelya.php";
    public static final String SEND_ADRES_PO_UMOLHANIY = BASE_IP + "insertAdresPokupatelyaPoUmolhaniy.php";
    public static final String BUY_KOLVO_TOVAR = BASE_IP + "byuTovar.php";
    public static final String SEND_COUNT_TOVARA = BASE_IP + "sendCountTovara.php";
    public static final String SPOSOB_DEFAULT = BASE_IP + "sposobDefault.php";
    public static final String DEL_TOVAR = BASE_IP + "delTovar.php";
    public static final String DEL_TOVAR_ALL = BASE_IP + "dellAll.php";
    public static final String TRUE_SELECTED = BASE_IP + "trSelect.php";
    public static final String FALSE_SELECTED = BASE_IP + "falseSelected.php";
    public static final String DETECT_TYPE_DOSTAVKI = BASE_IP + "detectTypeDostavki.php";
    public static final String UPLOAD = BASE_IP + "upload.php";
    public static final String REGISTER_URL = BASE_IP + "register.php";
    public static final String insertAnonimUserWithGoogleUID = BASE_IP + "insertAnonimUserWithGoogleUID.php";



    public static final String bndlStaticidTovaraFromSql = "idTovaraFromSql";
    public static String bndlStaticCountTovaraV_Korzine="countTovaraV_Korzine";

    public static int etKolihestvoTovarovV_Korzine_ACTION_ID=6;
    public static int intentRequestCODKorzinaToZakazPosleOtpravkiZakaza=120;
    public static int intentResultCODKorzinaToZakazPosleOtpravkiZakaza_OK=1201;
    public static int intentResultCODKorzinaToZakazPosleOtpravkiZakaza_NOT=1200;

    public static int intentRequestCODKategoriiPodToTovariActivityDlia_KolihestvaV_Korzine=121;
    public static int intentRequestCODKategoriiPodToTovariActivityDlia_KolihestvaV_Korzine_OK=1211;
    public static int intentRequestCODKategoriiPodToTovariActivityDlia_KolihestvaV_Korzine_NOT=1210;
    public static int intentRequestCODTovarActivToOpisanie=122;

    public static String getStringFromDoubleFormated(Double cena){
        String cenaFormat="";
        if(cena % 1 == 0 ){ //целое число
             cenaFormat = String.format("%, .0f", cena);
            System.out.println("Doubl format if= "+cenaFormat);
        } else{
             cenaFormat = String.format("%, .2f", cena);
             System.out.println("Doubl format else="+cenaFormat);
        }
        return  cenaFormat;
    }


}