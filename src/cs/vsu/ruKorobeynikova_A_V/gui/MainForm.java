package cs.vsu.ruKorobeynikova_A_V.gui;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {

    private JPanel panelMain;

    public MainForm() {
        this.setTitle("Task3");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.pack();
        this.setSize(new Dimension(1300, 900));
    }
}
