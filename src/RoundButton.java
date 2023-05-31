import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class RoundButton extends JButton {
    private Color bgColor;
    public RoundButton(String label) {
        super(label);
        setPreferredSize(new Dimension(70, 70));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBackgroundColor(Color.WHITE); // 기본 배경색을 설정합니다.

        Font buttonFont = new Font("Arial", Font.PLAIN, 18); // 예시로 Arial 폰트를 사용하고, 굵게, 크기는 16으로 설정합니다.
        setFont(buttonFont);
    }

    public void setBackgroundColor(Color color) {
        this.bgColor = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
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

    Shape getShape() {
        return new Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1);
    }

    @Override
    public boolean contains(int x, int y) {
        if (getShape() == null || !getShape().contains(x, y)) {
            return false;
        }
        return super.contains(x, y);
    }
}