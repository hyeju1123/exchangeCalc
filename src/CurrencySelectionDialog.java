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

        initializeCountryCodes();

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

                    ImageIcon flagIcon = createImageIcon(40, 25);
                    ExchangeView exchangeView = (ExchangeView) parent;
                    exchangeView.getFlagButton().setIcon(flagIcon);

                    dispose();
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

                // Parse JSON response
                String jsonString = response.toString();
                int flagsIndex = jsonString.indexOf("\"flags\":");
                if (flagsIndex != -1) {
                    int pngIndex = jsonString.indexOf("\"png\":\"", flagsIndex);
                    if (pngIndex != -1) {
                        int endIndex = jsonString.indexOf("\"", pngIndex + 7);
                        if (endIndex != -1) {
                            String flagUrl = jsonString.substring(pngIndex + 7, endIndex);

                            // Load and scale the image
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
