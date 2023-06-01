import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class ExchangeView extends JFrame {
    private JTextField inputField;
    private JTextField resultField;
    private RoundButton flagButton;
    private RoundButton exchangeButton;

    private double result;

    private CurrencySelectionDialog dialog;

    public RoundButton getFlagButton() {
        return flagButton;
    }

    public String getInputFieldText() {
        return inputField.getText();
    }

    public String getReulstFieldText() {
        return resultField.getText();
    }

    public CurrencySelectionDialog getDialog() {
        return dialog;
    }

    public void setResultFieldText(double result) {
        resultField.setText(Double.toString(result));
    }
    public double getResult() {
        return result;
    }

    public ExchangeView() {
        setTitle("Exchange Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = createMainPanel();
        add(mainPanel);

        dialog = new CurrencySelectionDialog(this);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createMainPanel() {
        RoundedPanel mainPanel = new RoundedPanel(25);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.decode("#f0f1f5"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        inputField = createField(Color.decode("#f0f1f5"), Color.decode("#cccdd7"), new Font("Arial", Font.PLAIN, 25),
                JTextField.RIGHT, 10, 35, 5, 5);
        mainPanel.add(inputField, BorderLayout.CENTER);

        resultField = createField(Color.decode("#f0f1f5"), null, new Font("Arial", Font.PLAIN, 50),
                JTextField.RIGHT, 30, 5, 5, 5);
        mainPanel.add(resultField, BorderLayout.NORTH);

        JPanel contentPanel = createContentPanel(mainPanel);
        JPanel outerPanel = createOuterPanel(contentPanel);

        return outerPanel;
    }

    private JPanel createContentPanel(JPanel mainPanel) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.decode("#cccdd7"));
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        return contentPanel;
    }

    private JPanel createOuterPanel(JPanel contentPanel) {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(Color.decode("#cccdd7"));
        outerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        outerPanel.add(contentPanel, BorderLayout.CENTER);
        return outerPanel;
    }

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

    private void clearInputFields() {
        inputField.setText("");
        resultField.setText("");
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
            resultField.setText(Double.toString(result));
        } catch (NumberFormatException e) {
            resultField.setText("Error: Invalid expression");
        }
    }

    private RoundButton createFlagButton() {
        RoundButton flagButton = new RoundButton("");
        flagButton.setIcon(createImageIcon("https://flagcdn.com/w320/us.png", 40, 25));
        flagButton.setPreferredSize(new Dimension(70, 70));
        flagButton.setBorderPainted(false);
        flagButton.setFocusPainted(false);
        flagButton.addActionListener(e -> showCurrencySelectionDialog());
        return flagButton;
    }

    private RoundButton createExchangeButton() {
        RoundButton exchangeButton = new RoundButton("");
        exchangeButton.setIcon(createImageIcon("img/exchange.png", 30, 30));
        exchangeButton.setPreferredSize(new Dimension(70, 70));
        exchangeButton.setBackgroundColor(Color.decode("#ff516b"));
        exchangeButton.setBorderPainted(false);
        exchangeButton.setFocusPainted(false);
        return exchangeButton;
    }

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

    public void addExchangeListener(ActionListener listener) {
        exchangeButton.addActionListener(listener);
    }

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