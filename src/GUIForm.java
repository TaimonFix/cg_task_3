import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.objecthunter.exp4j.*;


public class GUIForm extends JFrame {
    private JPanel panelMain;
    private JPanel buttonsPanel;
    private JComboBox comboBoxChooseCurve;
    private JPanel functionPanel;
    private JPanel curvePanel;
    private JTextField textFieldReadFunction;
    private JTextField textFieldReadX;
    private JTextField textFieldReadY;
    private JPanel pointPanel;
    private JButton buttonRead;
    private JButton buttonRemove;
    private JButton buttonFunction;
    private JPanel drawPanel;
    private JTextField textFieldReadParameter;

    Curve curve;

    private int CENTER_X;
    private int CENTER_Y;
    private int ONE_STEP;
    private List<Point> pointsFunc = new ArrayList<>();
    private List<Point> pointsCurve = new ArrayList<>();
    private List<Point> changedPoints = new ArrayList<>();

    public GUIForm() {
        this.setTitle("Моделирование кривых");
        this.setContentPane(panelMain);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.pack();
        this.setSize(900, 600);
        DrawPanel drawPanel = new DrawPanel();

        buttonRead.addActionListener(e -> {
            CENTER_X = drawPanel.getWidth() / 2;
            CENTER_Y = drawPanel.getHeight() / 2;
            ONE_STEP = 10;
            drawPanel.setPoint(new Point(Double.parseDouble(textFieldReadX.getText()), Double.parseDouble(textFieldReadY.getText())));
        });

        buttonFunction.addActionListener( k -> {
            pointsFunc.clear();
            for (double x = -CENTER_X; x <= CENTER_X; x += 0.001) {
                if (x == 0) {
                    x += 0.0000001;
                }

                Expression e = new ExpressionBuilder(textFieldReadFunction.getText())
                        .variables("x")
                        .build()
                        .setVariable("x", x);
                double y = e.evaluate();
                if (y >= -CENTER_Y && y <= CENTER_Y) pointsFunc.add(new Point(x, y));
            }
        });

        buttonRemove.addActionListener( k -> {
            pointsFunc.clear();
            pointsCurve.clear();
        });
    }

    private boolean findParameter() {
        String str= textFieldReadParameter.getText();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'a') return true;
        }
        return false;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        drawPanel = new DrawPanel();
        drawPanel.setSize(new Dimension(900, 600));
        drawPanel.setBackground(Color.darkGray);
    }

    public class DrawPanel extends JPanel {


        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            CENTER_X = drawPanel.getWidth() / 2;
            CENTER_Y = drawPanel.getHeight() / 2;
            ONE_STEP = 10;

            drawSingleSegment(g2, CENTER_X, CENTER_Y, ONE_STEP);
            g2.setStroke(new BasicStroke(2));
            drawOxAndOy(g2, CENTER_X, CENTER_Y);
            g2.setStroke(new BasicStroke(1));

            if (pointsFunc.size() > 0 ) { // для графиков
                changedPoints.clear();
                connectingPoints(g2, pointsFunc);
                g2.setStroke(new BasicStroke(1));
            } else if (pointsCurve.size() > 0) { // для кривых
                List<Point> pointsToDrawLines = new ArrayList<>();
                changedPoints.clear();
                g2.setStroke(new BasicStroke(1));
                String who = String.valueOf(comboBoxChooseCurve.getSelectedItem());
                switch (who) {
                    case "Безье" -> {
                        if (pointsCurve.size() > 2) {
                            Beziers bezier = new Beziers(pointsCurve);
                            bezier.solution();
                            pointsToDrawLines = bezier.getPutPoints();
                        }
                    }
                    case "B-сплайн" -> {
                        if (pointsCurve.size() > 3) {
                            BSpline bSpline = new BSpline(pointsCurve);
                            bSpline.solution();
                            pointsToDrawLines = bSpline.getPutPoints();
                        }
                    }
                }

                connectingPoints(g2, pointsToDrawLines);
                drawPoints(g2, pointsCurve);
            }

            g2.setColor(Color.black);
            repaint();
        }

        private void drawOxAndOy(Graphics2D g2, int CENTER_X, int CENTER_Y) {
            //рисуем систему координат (оси оХ и оУ)
            g2.setColor(Color.white);
            g2.drawLine(CENTER_X, 0, CENTER_X, drawPanel.getHeight());
            g2.drawLine(0, CENTER_Y, drawPanel.getWidth(), CENTER_Y);
        }

        private void drawSingleSegment(Graphics2D g2, int CENTER_X, int CENTER_Y, int ONE_STEP) {
            g2.setColor(Color.gray);
            int step = CENTER_X;
            while (step <= drawPanel.getWidth()) {
                g2.drawLine(step, 0, step, drawPanel.getHeight());
                step += ONE_STEP;
            }
            step = CENTER_X;
            while (step >= 0) {
                g2.drawLine(step, 0, step, drawPanel.getHeight());
                step -= ONE_STEP;
            }
            step = CENTER_Y;
            while (step >= 0) {
                g2.drawLine(0, step, drawPanel.getWidth(), step);
                step -= ONE_STEP;
            }
            step = CENTER_Y;
            while (step <= drawPanel.getHeight()) {
                g2.drawLine(0, step, drawPanel.getWidth(), step);
                step += ONE_STEP;
            }
        }

        private void drawPoints(Graphics2D g2D, List<Point> list) {
            for (Point value : list) {
                Point p = new Point(0, 0);
                p.setX(value.getX() * ONE_STEP + CENTER_X);
                p.setY(CENTER_Y - value.getY() * ONE_STEP);

                g2D.setColor(Color.red);
                g2D.fillOval((int)Math.round(p.getX()) - 2, (int)Math.round(p.getY()) - 2, 5, 5);
                changedPoints.add(p);
            }
        }

        private void connectingPoints(Graphics2D g2, List<Point> list) {
            g2.setStroke(new BasicStroke(3));

            curve = new Curve(g2, list.size(), 0);
            g2.setColor(Color.BLACK);
            for (int i = 0; i < list.size() - 1; i++) {
                curve.drawCurve((int)Math.round(CENTER_X + list.get(i).getX() * ONE_STEP), (int)Math.round(CENTER_Y - list.get(i).getY() * ONE_STEP),
                        (int)Math.round(CENTER_X + list.get(i + 1).getX() * ONE_STEP), (int)Math.round(CENTER_Y - list.get(i + 1).getY() * ONE_STEP), 1, 4);
            }
            g2.setColor(Color.black);
        }

        public void setPoint(Point coordinate) {
            coordinate.setX((float) coordinate.getX());
            coordinate.setY(Math.round((float) coordinate.getY()));
            pointsCurve.add(coordinate);
        }

    }
}
