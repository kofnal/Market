package app.fil.market.korzina;

public class KorzinaObjekt {
    private Double itogoKOplateDoubl=0.0;
    private Double korzinaCountIntTotal=0.0;
    void changeItogoK_OplateDouble(Double stoimost){
        itogoKOplateDoubl=itogoKOplateDoubl+stoimost;
    }
    void changeKorzinaCountInt(Double hisloTovarovIzmenilos){
        korzinaCountIntTotal=korzinaCountIntTotal+hisloTovarovIzmenilos;
    }
}
