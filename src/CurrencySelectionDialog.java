import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


// This dialog allows you to select the currency of a particular country, and retrieves the flag image of the selected country and displays it in the flagButton of the ExchangeView class.
public class CurrencySelectionDialog extends JDialog {
    private Map<String, String> countryCodes = new HashMap<>(); // Map to store country codes and names
    private String selectedCountryCode = "US"; // Store the currently selected country code. It is initially set to "US".

    public String getSelectedCountryCode() {
        return this.selectedCountryCode;
    }


    public CurrencySelectionDialog(Frame parent, Map<String, String> countryCodes) {
        super(parent, "Currency Selection", true);

        JPanel contentPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initializeCountryCodes(countryCodes);

        for (Map.Entry<String, String> entry : this.countryCodes.entrySet()) {
            String countryCode = entry.getKey();

            JButton countryButton = new JButton(countryCode);
            countryButton.setPreferredSize(new Dimension(50, countryButton.getPreferredSize().height));
            countryButton.addActionListener(e -> {
                this.selectedCountryCode = ExchangeModel.extractCountryCode(countryCode);

                ImageIcon flagIcon = createImageIcon(40, 25);
                ExchangeView exchangeView = (ExchangeView) parent;
                exchangeView.getFlagButton().setIcon(flagIcon);

                dispose();
            });
            contentPanel.add(countryButton);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        add(scrollPane);

        pack();
        setLocationRelativeTo(parent);
    }


    // Populates the countryCodes map with the provided countryCodes parameter.
    public void initializeCountryCodes(Map<String, String> countryCodes) {
        for (Map.Entry<String, String> entry : countryCodes.entrySet()) {

            String countryCode = entry.getKey();
            String countryName = entry.getValue();

            this.countryCodes.put(countryCode, countryName);
        }
    }

    // Fetches the flag image for the currently selected country code.
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

                // Parse JSON response.
                String jsonString = response.toString();
                int flagsIndex = jsonString.indexOf("\"flags\":");
                if (flagsIndex != -1) {
                    int pngIndex = jsonString.indexOf("\"png\":\"", flagsIndex);
                    if (pngIndex != -1) {
                        int endIndex = jsonString.indexOf("\"", pngIndex + 7);
                        if (endIndex != -1) {
                            String flagUrl = jsonString.substring(pngIndex + 7, endIndex);

                            // Load and scale the image.
                            BufferedImage originalImage = ImageIO.read(new URL(flagUrl));
                            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                            return new ImageIcon(scaledImage);
                        }
                    }
                }
            } else {
                System.out.println("Failed to fetch flag image for: " + selectedCountryCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
