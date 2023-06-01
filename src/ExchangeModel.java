import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ExchangeModel {

    private Map<String, String> countryCodes = new HashMap<>();
    
    public double calculate(double expression, String selectedCode) {

        //Exchange rate inquiry
        double exchangeRate = ExchangeRateUtils.getExchangeRate(getISO(selectedCode));
        //EXchange KRW for currency unit of selected country
        return Math.round(expression/exchangeRate*1000)/1000.0;  //to three decimal places
    }

    //Change selected country code to currency code
    public Map<String, String> getCountryCodes() {
        try (BufferedReader reader = new BufferedReader(new FileReader("CountryCode.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String countryCode = parts[0];
                    String isoCode = parts[1];
                    this.countryCodes.put(countryCode, isoCode);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return countryCodes;
    }

    public static String extractCountryCode(String line) {
        int openParenIndex = line.indexOf('(');
        int closeParenIndex = line.indexOf(')');
        if (openParenIndex != -1 && closeParenIndex != -1 && openParenIndex < closeParenIndex) {
            return line.substring(openParenIndex + 1, closeParenIndex);
        }
        return null;
    }

     private String getISO(String selectedCode) {
         return this.countryCodes.entrySet()
                 .stream()
                 .filter(entry -> ExchangeModel.extractCountryCode(entry.getKey()).equals(selectedCode))
                 .map(Map.Entry::getValue)
                 .findFirst()
                 .orElse("USD"); // Default to USD if the ISO code is not found
     }
}
