package cs.vsu.ruKorobeynikova_A_V.gui;

import cs.vsu.ruKorobeynikova_A_V.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cs.vsu.ruKorobeynikova_A_V.DrawCurve;
import cs.vsu.ruKorobeynikova_A_V.Forms.BSpline;
import cs.vsu.ruKorobeynikova_A_V.Forms.Bezier;
import net.objecthunter.exp4j.*;
public class MainForm extends JFrame {


    private JPanel panelMain;
    private JComboBox comboBoxChooseCurve;
    private JTextField textFieldGetFunction;
    private JSpinner spinnerParameter;
    public JPanel coordinatePlanePanel;
    private JSpinner spinnerScale;
    private JTextField textFieldReadX;
    private JTextField textFieldReadY;
    private JButton buttonRead;
    private JButton buttonPresentFunction;
    private JButton buttonClear;

    DrawCurve drawCurve;

    private int CENTER_X;
    private int CENTER_Y;
    private int ONE_STEP;
    private List<Coordinates> pointsFunc = new ArrayList<>();
    private List<Coordinates> pointsCurve = new ArrayList<>();
    private List<Coordinates> changedPoints = new ArrayList<>();

    public MainForm() {
        this.setTitle("Task3");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.pack();
        this.setSize(new Dimension(1300, 900));
        this.spinnerScale.setValue(1);
        DrawPanel drawPanel = new DrawPanel();

        buttonRead.addActionListener(e -> {
            CENTER_X = coordinatePlanePanel.getWidth() / 2;
            CENTER_Y = coordinatePlanePanel.getHeight() / 2;
            ONE_STEP = 10 * Integer.parseInt(String.valueOf(spinnerScale.getValue()));
            drawPanel.setPoint(new Coordinates(Double.parseDouble(textFieldReadX.getText()), Double.parseDouble(textFieldReadY.getText())));
        });

        buttonPresentFunction.addActionListener( k -> {
            pointsFunc.clear();
            for (double x = -CENTER_X; x <= CENTER_X; x += 0.001) {
                if (x == 0) x += 0.0000001;
                if (findParameter()) {
                    Expression e = new ExpressionBuilder(textFieldGetFunction.getText())
                            .variables("x", "a")
                            .build()
                            .setVariable("x", x)
                            .setVariable("a", Integer.parseInt(String.valueOf(spinnerParameter.getValue())));
                    double y = e.evaluate();
                    if (y >= -CENTER_Y && y <= CENTER_Y) pointsFunc.add(new Coordinates(x, y));
                } else {
                    Expression e = new ExpressionBuilder(textFieldGetFunction.getText())
                            .variables("x")
                            .build()
                            .setVariable("x", x);
                    double y = e.evaluate();
                    if (y >= -CENTER_Y && y <= CENTER_Y) pointsFunc.add(new Coordinates(x, y));
                }
            }
        });

        buttonClear.addActionListener( k -> {
            pointsFunc.clear();
            pointsCurve.clear();
        });
    }

    private boolean findParameter() {
        String str= textFieldGetFunction.getText();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'a') return true;
        }
        return false;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        coordinatePlanePanel = new DrawPanel();
    }

    public class DrawPanel extends JPanel {


        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            CENTER_X = coordinatePlanePanel.getWidth() / 2;
            CENTER_Y = coordinatePlanePanel.getHeight() / 2;
            ONE_STEP = 10 * Integer.parseInt(String.valueOf(spinnerScale.getValue()));

            drawSingleSegment(g2, CENTER_X, CENTER_Y, ONE_STEP);
            g2.setStroke(new BasicStroke(2));
            drawOxAndOy(g2, CENTER_X, CENTER_Y);
            g2.setStroke(new BasicStroke(1));

            if (pointsFunc.size() > 0 ) { // для графиков
                changedPoints.clear();
                connectingPoints(g2, pointsFunc);
                g2.setStroke(new BasicStroke(1));
            } else if (pointsCurve.size() > 0) { // для кривых
                List<Coordinates> pointsToDrawLines = new ArrayList<>();
                changedPoints.clear();
                g2.setStroke(new BasicStroke(1));
                String who = String.valueOf(comboBoxChooseCurve.getSelectedItem());
                switch (who) {
                    case "Безье" -> {
                        if (pointsCurve.size() > 2) {
                            Bezier bezier = new Bezier(pointsCurve);
                            bezier.calculation();
                            pointsToDrawLines = bezier.getAddPoints();
                        }
                    }
                    case "Б-сплайн" -> {
                        if (pointsCurve.size() > 3) {
                            BSpline bSpline = new BSpline(pointsCurve);
                            bSpline.calculation();
                            pointsToDrawLines = bSpline.getAddPoints();
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
            g2.setColor(Color.blue);
            g2.drawLine(CENTER_X, 0, CENTER_X, coordinatePlanePanel.getHeight());
            g2.drawLine(0, CENTER_Y, coordinatePlanePanel.getWidth(), CENTER_Y);
        }

        private void drawSingleSegment(Graphics2D g2, int CENTER_X, int CENTER_Y, int ONE_STEP) {
            g2.setColor(Color.gray);
            int step = CENTER_X;
            while (step <= coordinatePlanePanel.getWidth()) {
                g2.drawLine(step, 0, step, coordinatePlanePanel.getHeight());
                step += ONE_STEP;
            }
            step = CENTER_X;
            while (step >= 0) {
                g2.drawLine(step, 0, step, coordinatePlanePanel.getHeight());
                step -= ONE_STEP;
            }
            step = CENTER_Y;
            while (step >= 0) {
                g2.drawLine(0, step, coordinatePlanePanel.getWidth(), step);
                step -= ONE_STEP;
            }
            step = CENTER_Y;
            while (step <= coordinatePlanePanel.getHeight()) {
                g2.drawLine(0, step, coordinatePlanePanel.getWidth(), step);
                step += ONE_STEP;
            }
        }

        private void drawPoints(Graphics2D g2D, List<Coordinates> list) {
            for (Coordinates value : list) {
                Coordinates coordinates = new Coordinates(0, 0);
                coordinates.setX(value.getX() * ONE_STEP + CENTER_X);
                coordinates.setY(CENTER_Y - value.getY() * ONE_STEP);

                g2D.setColor(Color.red);
                g2D.fillOval((int)Math.round(coordinates.getX()) - 2, (int)Math.round(coordinates.getY()) - 2, 5, 5);
                changedPoints.add(coordinates);
            }
        }

        private void connectingPoints(Graphics2D g2, List<Coordinates> list) {
            g2.setStroke(new BasicStroke(3));

            drawCurve = new DrawCurve(g2, list.size(), 0);
            g2.setColor(Color.darkGray);
            for (int i = 0; i < list.size() - 1; i++) {
                drawCurve.drawCurve((int)Math.round(CENTER_X + list.get(i).getX() * ONE_STEP), (int)Math.round(CENTER_Y - list.get(i).getY() * ONE_STEP),
                        (int)Math.round(CENTER_X + list.get(i + 1).getX() * ONE_STEP), (int)Math.round(CENTER_Y - list.get(i + 1).getY() * ONE_STEP), 1, 4);
            }
            g2.setColor(Color.black);
        }

        public void setPoint(Coordinates coordinate) {
            coordinate.setX((float) coordinate.getX());
            coordinate.setY(Math.round((float) coordinate.getY()));
            pointsCurve.add(coordinate);
        }

    }


}
