package app.fil.market;

public class StaticMethod {

    public static String doubToString_2(Double cenaZaEdinicuOdnu) { // возвращает с двумя знаками после запятой
        String res="";
       return  res = (String.format("%(.2f", cenaZaEdinicuOdnu));
    }
}
