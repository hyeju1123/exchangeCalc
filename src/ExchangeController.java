import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            double expression = view.getResult();

            CurrencySelectionDialog dialog = view.getDialog();
            String selectedCountryCode = dialog.getSelectedCountryCode();

            double result = model.calculate(expression, selectedCountryCode);
            view.setResultFieldText(result);
        }
    }
}
