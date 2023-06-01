import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

// Custom button for rounding calculator buttons
public class RoundButton extends JButton {
    private Color bgColor;

    public RoundButton(String label) {
        super(label);
        setPreferredSize(new Dimension(70, 70));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBackgroundColor(Color.WHITE); // sets the background color of the button.

        Font buttonFont = new Font("Arial", Font.PLAIN, 18); 
        setFont(buttonFont);
    }

    // Setter for different background colors of operator buttons and numeric buttons
    public void setBackgroundColor(Color color) {
        this.bgColor = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Different background colors when clicked
        if (getModel().isArmed()) {
            g.setColor(getBackground());
        } else {
            g.setColor(bgColor);
        }
        g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getBackground());
        g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
    }

    @Override
    public boolean contains(int x, int y) {
        if (getShape() == null || !getShape().contains(x, y)) {
            return false;
        }
        return super.contains(x, y);
    }

    Shape getShape() {
        return new Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1);
    }
}