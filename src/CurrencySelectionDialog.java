import com.google.gson.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CurrencySelectionDialog extends JDialog {
    private Map<String, String> countryCodes = new HashMap<>(); // Map to store country codes and names
    private String selectedCountryCode = "US";

    public String getSelectedCountryCode() {
        return selectedCountryCode;
    }


    public CurrencySelectionDialog(Frame parent) {
        super(parent, "Currency Selection", true);

        initializeCountryCodes(); // Initialize country codes and names

        JPanel contentPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (Map.Entry<String, String> entry : countryCodes.entrySet()) {
            String countryCode = entry.getKey();
            String countryName = entry.getValue();

            JButton countryButton = new JButton(countryName);
            countryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCountryCode = countryCode;

                    // Update the icon of the flagButton in the parent ExchangeView
                    ImageIcon flagIcon = createImageIcon(40, 25);
                    ExchangeView exchangeView = (ExchangeView) parent;
                    exchangeView.getFlagButton().setIcon(flagIcon);

                    dispose(); // Close the dialog
                }
            });
            contentPanel.add(countryButton);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        add(scrollPane);

        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeCountryCodes() {
        countryCodes.put("AE", "United Arab Emirates");
        countryCodes.put("AT", "Austria");
        countryCodes.put("AU", "Australia");
        countryCodes.put("BE", "Belgium");
        countryCodes.put("BH", "Bahrain");
        countryCodes.put("CA", "Canada");
        countryCodes.put("CH", "Switzerland");
        countryCodes.put("CN", "China");
        countryCodes.put("DE", "Germany");
        countryCodes.put("DK", "Denmark");
        countryCodes.put("ES", "Spain");
        countryCodes.put("FI", "Finland");
        countryCodes.put("GB", "United Kingdom");
        countryCodes.put("HK", "Hong Kong");
        countryCodes.put("ID", "Indonesia");
        countryCodes.put("IT", "Italy");
        countryCodes.put("JP", "Japan");
        countryCodes.put("KR", "South Korea");
        countryCodes.put("KW", "Kuwait");
        countryCodes.put("MY", "Malaysia");
        countryCodes.put("NL", "Netherlands");
        countryCodes.put("NO", "Norway");
        countryCodes.put("NZ", "New Zealand");
        countryCodes.put("SA", "Saudi Arabia");
        countryCodes.put("SE", "Sweden");
        countryCodes.put("SG", "Singapore");
        countryCodes.put("TH", "Thailand");
        countryCodes.put("US", "United States");
    }

    public ImageIcon createImageIcon(int width, int height) {
        try {
            String apiUrl = "https://restcountries.com/v3.1/alpha/" + selectedCountryCode;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonElement jsonElement = JsonParser.parseString(response.toString());

                JsonObject jsonObject = null;
                if (jsonElement.isJsonObject()) {
                    jsonObject = jsonElement.getAsJsonObject();
                } else if (jsonElement.isJsonArray()) {
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    if (jsonArray.size() > 0) {
                        jsonObject = jsonArray.get(0).getAsJsonObject();
                    } else {
                        System.out.println("Empty JSON array response: " + response);
                        return null;
                    }
                } else {
                    System.out.println("Invalid JSON response: " + response);
                    return null;
                }

                JsonObject flagsObject = jsonObject.getAsJsonObject("flags");
                String flagUrl = flagsObject.get("png").getAsString();

                // Load and scale the image
                BufferedImage originalImage = ImageIO.read(new URL(flagUrl));
                Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            } else {
                System.out.println("Failed to fetch flag image for: " + selectedCountryCode);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
