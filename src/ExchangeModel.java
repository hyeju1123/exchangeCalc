import java.math.BigDecimal;

public class ExchangeModel {
    public double calculate(double expression, String selectedCode) {
        /*System.out.println(expression);
        System.out.println(selectedCode);*/

        //Exchange rate inquiry
        double exchangeRate = ExchangeRateUtils.getExchangeRate(getISO(selectedCode));
        //EXchange KRW for currency unit of selected country
        return Math.round(expression/exchangeRate*1000)/1000.0;  //to three decimal places
    }
    //Change selected country code to currency code
    private String getISO(String selectedCode){
        String ISOCode;
        switch (selectedCode) {
            case "AE":
                ISOCode = "AED";
                break;
            case "AT":
                ISOCode = "ATS";
                break;
            case "AU":
                ISOCode = "AUD";
                break;
            case "BE":
                ISOCode = "BEF";
                break;
            case "BH":
                ISOCode = "BHD";
                break;
            case "CA":
                ISOCode = "CAD";
                break;
            case "CH":
                ISOCode = "CHF";
                break;
            case "CN":
                ISOCode = "CNH";
                break;
            case "DE":
                ISOCode = "DEM";
                break;
            case "DK":
                ISOCode = "DKK";
                break;
            case "ES":
                ISOCode = "ESP(100)";
                break;
            case "FI":
                ISOCode = "FIM";
                break;
            case "GB":
                ISOCode = "GBP";
                break;
            case "HK":
                ISOCode = "HKD";
                break;
            case "ID":
                ISOCode = "IDR(100)";
                break;
            case "IT":
                ISOCode = "ITL(100)";
                break;
            case "JP":
                ISOCode = "JPY(100)";
                break;
            case "KR":
                ISOCode = "KRW";
                break;
            case "KW":
                ISOCode = "KWD";
                break;
            case "MY":
                ISOCode = "MYR";
                break;
            case "NL":
                ISOCode = "NLG";
                break;
            case "NO":
                ISOCode = "NOK";
                break;
            case "NZ":
                ISOCode = "NZD";
                break;
            case "SA":
                ISOCode = "SAR";
                break;
            case "SE":
                ISOCode = "SEK";
                break;
            case "SG":
                ISOCode = "SGD";
                break;
            case "TH":
                ISOCode = "THB";
                break;
            case "US":
            default:
                ISOCode = "USD";
                break;
        }
        return ISOCode;
    }
}
