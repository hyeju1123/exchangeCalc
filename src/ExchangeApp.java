import javax.swing.*;

public class ExchangeApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExchangeModel model = new ExchangeModel();

            ExchangeView view = new ExchangeView();
            ExchangeController controller = new ExchangeController(model, view);
            view.setVisible(true);
        });
    }
}