package cs.vsu.ruKorobeynikova_A_V.gui;

import cs.vsu.ruKorobeynikova_A_V.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainForm extends JFrame {


    private JPanel panelMain;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JSpinner spinnerParameter;
    public JPanel coordinatePlanePanel;
    private JSpinner spinnerScale;
    private JTextField textFieldReadX;
    private JTextField textFieldReadY;
    private JButton buttonRead;

    private int CENTER_X;
    private int CENTER_Y;
    private int ONE_STEP;
    private List<Coordinates> points = new ArrayList<>();
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
            drawPanel.setPoint(new Coordinates(Integer.parseInt(textFieldReadX.getText()), Integer.parseInt(textFieldReadY.getText())));

        });

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        coordinatePlanePanel = new DrawPanel();
    }

    class DrawPanel extends JPanel {


        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            CENTER_X = coordinatePlanePanel.getWidth() / 2;
            CENTER_Y = coordinatePlanePanel.getHeight() / 2;
            ONE_STEP = 10 * Integer.parseInt(String.valueOf(spinnerScale.getValue()));
            Graphics2D g2 = (Graphics2D) g;

            drawSingleSegment(g2, CENTER_X, CENTER_Y, ONE_STEP);
            g2.setStroke(new BasicStroke(2));
            drawOxAndOy(g2, CENTER_X, CENTER_Y);
            g2.setStroke(new BasicStroke(1));

            if (points.size() > 0) {
                changedPoints.clear();
                g2.setStroke(new BasicStroke(5));
                drawPoints(g2);
                g2.setStroke(new BasicStroke(1));
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

        private void drawPoints(Graphics2D g2D) {
            Coordinates coordinates = new Coordinates(0, 0);
            for (int i = 0; i < points.size(); i++) {
                coordinates.setX(points.get(i).getX() * ONE_STEP);
                coordinates.setY(points.get(i).getY() * ONE_STEP);

                g2D.setColor(Color.red);
                g2D.fillOval(CENTER_X + coordinates.getX(), CENTER_Y - coordinates.getY() ,5, 5);
                changedPoints.add(coordinates);
            }
        }

        public void setPoint(Coordinates coordinate) {
            coordinate.setX(Math.round((float) coordinate.getX()));
            coordinate.setY(Math.round((float) coordinate.getY()));
            points.add(coordinate);
        }
    }


}
