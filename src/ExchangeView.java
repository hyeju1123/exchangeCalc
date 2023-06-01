import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Map;

public class ExchangeView extends JFrame {
    // For input and result display.
    private JTextField inputField;
    private JTextField resultField;
    private JTextField exchangeResultField;


    // A button that allows you to select a country to exchange money.
    private RoundButton flagButton;
    // A button to start the currency exchange calculation.
    private RoundButton exchangeButton;

    private double result;

    private CurrencySelectionDialog dialog;


    // getter setter methods
    public RoundButton getFlagButton() {
        return flagButton;
    }

    public String getResultFieldText() {
        String text = resultField.getText();
        int endIndex = text.lastIndexOf(" (KR)");
        if (endIndex != -1) {
            return text.substring(0, endIndex);
        }
        return "";
    }

    public CurrencySelectionDialog getDialog() {
        return dialog;
    }

    public void setResultFieldText(String result) {
        resultField.setText(result);
    }
    public void setExchangeResultField(String result) {
        exchangeResultField.setText(result);
    }
    public double getResult() {
        return result;
    }

    // Initializes the main frame of the application.
    public ExchangeView(Map<String, String> countryCodes) {
        setTitle("Exchange Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = createMainPanel();
        add(mainPanel);

        dialog = new CurrencySelectionDialog(this, countryCodes);

        pack();
        setLocationRelativeTo(null);
    }

    // to create the main panel of the frame.
    private JPanel createMainPanel() {
        RoundedPanel mainPanel = new RoundedPanel(25);
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.decode("#f0f1f5"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        inputField = createField(Color.decode("#f0f1f5"), Color.decode("#cccdd7"), new Font("Arial", Font.PLAIN, 25),
                JTextField.RIGHT, 10, 35, 5, 5);

        exchangeResultField = createField(Color.decode("#f0f1f5"), null, new Font("Arial", Font.PLAIN, 30),
                JTextField.RIGHT, 30, 35, 5, 5);

        resultField = createField(Color.decode("#f0f1f5"), null, new Font("Arial", Font.PLAIN, 30),
                JTextField.RIGHT, 30, 35, 5, 5);
        resultField.setText("0.0 (KR)");

        JPanel buttonPanel = createButtonPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(resultField, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.0;
        mainPanel.add(exchangeResultField, gbc);

        gbc.gridy = 2;
        mainPanel.add(inputField, gbc);

        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);

        JPanel contentPanel = createContentPanel(mainPanel);
        JPanel outerPanel = createOuterPanel(contentPanel);

        return outerPanel;
    }



    // method creates a content panel that contains the main panel.
    private JPanel createContentPanel(JPanel mainPanel) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.decode("#cccdd7"));
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        return contentPanel;
    }

    // method creates an outer panel that contains the content panel.
    private JPanel createOuterPanel(JPanel contentPanel) {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(Color.decode("#cccdd7"));
        outerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        outerPanel.add(contentPanel, BorderLayout.CENTER);
        return outerPanel;
    }

    // creates a JTextField(input, result fields)
    private JTextField createField(Color background, Color foreground, Font font, int horizontalAlignment,
                                   int top, int right, int bottom, int left) {
        JTextField field = new JTextField();
        field.setBackground(background);
        if (foreground != null) {
            field.setForeground(foreground);
        }
        field.setFont(font);
        field.setHorizontalAlignment(horizontalAlignment);
        field.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        field.setEditable(false);
        return field;
    }

    // method creates the panel containing the calculator buttons.
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(6, 4, 15, 15));
        buttonPanel.setBackground(Color.decode("#f0f1f5"));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, -60, 15));

        String[] buttonLabels = {
                "Ac", "del", "/", "*",
                "7", "8", "9", "-",
                "4", "5", "6", "+",
                "1", "2", "3", "=",
                "0", "."
        };

        for (String label : buttonLabels) {
            JButton button = createButton(label);
            buttonPanel.add(button);
        }

        flagButton = createFlagButton();
        exchangeButton = createExchangeButton();

        buttonPanel.add(flagButton);
        buttonPanel.add(exchangeButton);

        return buttonPanel;
    }

    // creates a RoundButton for number and operator.
    private RoundButton createButton(String label) {
        RoundButton button = new RoundButton(label);
        button.setPreferredSize(new Dimension(70, 70));

        if (label.equals("Ac") || label.equals("del")) {
            button.setBackgroundColor(Color.decode("#e0e2e6"));
            if (label.equals("Ac")) {
                button.addActionListener(e -> clearInputFields());
            } else {
                button.addActionListener(e -> deleteLastCharacter());
            }
        } else if (label.equals("/") || label.equals("*") || label.equals("-") || label.equals("+") || label.equals("=")) {
            button.setBackgroundColor(Color.decode("#b2b2bb"));
            if (label.equals("=")) {
                button.addActionListener(e -> calculateResult());
            } else {
                button.addActionListener(e -> appendToInputField(label));
            }
        } else {
            button.addActionListener(e -> appendToInputField(label));
        }

        return button;
    }

    // handle the button actions related to the input and result fields.
    private void clearInputFields() {
        inputField.setText("");
        resultField.setText("");
        exchangeResultField.setText("");
    }

    private void deleteLastCharacter() {
        String currentText = inputField.getText();
        if (!currentText.isEmpty()) {
            inputField.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    private void appendToInputField(String text) {
        inputField.setText(inputField.getText() + text);
    }

    private void calculateResult() {
        try {
            String expression = inputField.getText();
            ExchangeViewService service = new ExchangeViewService();
            result = service.evaluateExpression(expression);
            resultField.setText(result + " (KR)");
        } catch (NumberFormatException e) {
            resultField.setText("Error: Invalid expression");
        }
    }

    // creates the flag button.
    private RoundButton createFlagButton() {
        RoundButton flagButton = new RoundButton("");
        flagButton.setIcon(createImageIcon("https://flagcdn.com/w320/us.png", 40, 25));
        flagButton.setPreferredSize(new Dimension(70, 70));
        flagButton.setBorderPainted(false);
        flagButton.setFocusPainted(false);
        flagButton.addActionListener(e -> showCurrencySelectionDialog());
        return flagButton;
    }

    // creates the exchange button.
    private RoundButton createExchangeButton() {
        RoundButton exchangeButton = new RoundButton("");
        exchangeButton.setIcon(createImageIcon("img/exchange.png", 30, 30));
        exchangeButton.setPreferredSize(new Dimension(70, 70));
        exchangeButton.setBackgroundColor(Color.decode("#ff516b"));
        exchangeButton.setBorderPainted(false);
        exchangeButton.setFocusPainted(false);
        return exchangeButton;
    }

    // creates an ImageIcon from the provided path(restCountries api or local path)
    private ImageIcon createImageIcon(String path, int width, int height) {
        try {
            ImageIcon originalIcon;
            if (path.startsWith("http") || path.startsWith("https")) {
                URL imageUrl = new URL(path);
                originalIcon = new ImageIcon(imageUrl);
            } else {
                originalIcon = new ImageIcon(path);
            }

            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // method adds an ActionListener to the exchange button.
    public void addExchangeListener(ActionListener listener) {
        exchangeButton.addActionListener(listener);
    }

    // shows the currency selection dialog.
    private void showCurrencySelectionDialog() {
        dialog.setVisible(true);
    }

    private void fireCalculateEvent() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "calculate");
        ActionListener[] listeners = exchangeButton.getListeners(ActionListener.class);
        for (ActionListener listener : listeners) {
            listener.actionPerformed(event);
        }
    }
}