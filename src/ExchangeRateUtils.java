import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExchangeRateUtils {

    private static HttpURLConnection connection;
    private static double defaultExchangeRate = 1;

    public static double getExchangeRate(String selectedCode) {

        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        JSONParser parser = new JSONParser();

        String authKey = "u8akUyDASnytO1ngm0iJRfyOpV3WhTuy";
        String searchDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String dataType = "AP01";
        double exchangeRate = 0;
        String countryCode = selectedCode;

        try {
            // Request URL
            URL url = new URL("https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + authKey + "&searchdate=" + searchDate + "&data=" + dataType);
            connection = (HttpURLConnection) url.openConnection();

            // Request initialization
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            // API call
            // If failed Connection Close
            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                    System.out.println(1);
                }
                reader.close();
            } else { // Succeed
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                System.out.println(2);
                while ((line = reader.readLine()) != null) {
                    JSONArray exchangeRateInfoList = (JSONArray) parser.parse(line);

                    // KRW -> Selected Code: Exchange Rate Information Inquiry
                    for (Object o : exchangeRateInfoList) {
                        JSONObject exchangeRateInfo = (JSONObject) o;
                        if (exchangeRateInfo.get("cur_unit").equals(countryCode)) {

                            // 쉼표가 포함되어 String 형태로 들어오는 데이터를 Double로 파싱하기 위한 부분
                            // String -> Double
                            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                            exchangeRate = (format.parse(exchangeRateInfo.get("deal_bas_r").toString()).doubleValue());
                        }
                    }
                }
                reader.close();
            }
            System.out.println(responseContent);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        } finally {
            connection.disconnect();
        }

        if (exchangeRate == 0) {
            exchangeRate = defaultExchangeRate;
        }

        return exchangeRate;
    }
}
