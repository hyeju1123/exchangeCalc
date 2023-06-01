import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ExchangeController {
    private ExchangeModel model;
    private ExchangeView view;

    public ExchangeController(ExchangeModel model, ExchangeView view) {
        this.model = model;
        this.view = view;
        this.view.addExchangeListener(new CalculateListener());
    }

    class CalculateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String expression = view.getResultFieldText();

            CurrencySelectionDialog dialog = view.getDialog();
            String selectedCountryCode = dialog.getSelectedCountryCode();

            double result = model.calculate(Double.parseDouble(expression), selectedCountryCode);
            view.setResultFieldText(result);
        }
    }
}
