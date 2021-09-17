package color.column;

import jdk.nashorn.internal.ir.WhileNode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeSizeActionListener implements ActionListener {

    private ColumnModel model;

    private ColorColumnSimulation frame;

    private Timer timerUp = new Timer(1, new HandlerUpClass());
    private Timer timerDown = new Timer(1, new HandlerDownClass());

    public ChangeSizeActionListener(ColorColumnSimulation frame, ColumnModel model) {
        this.frame = frame;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();
        String text = button.getText();
        if (text.equals("Up")) {
            timerUp.start();
            frame.repaint();
        } else if (text.equals("Down")) {
            timerDown.start();
            frame.repaint();
        }
    }

    class HandlerUpClass implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            DrawingPanel panel = frame.getDrawingPanel();

            if (panel.radius <= DrawingPanel.MAX_RADIUS) {
                panel.radius = panel.radius + 1;
                DrawingPanel.y--;
            }   else {
                timerUp.stop();
            }
            frame.repaint();
        }
    }

    class HandlerDownClass implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            DrawingPanel panel = frame.getDrawingPanel();

            if (panel.radius >= DrawingPanel.MIN_RADIUS) {
                panel.radius = panel.radius - 1;
                DrawingPanel.y++;
            } else {
                timerDown.stop();
            }
            frame.repaint();
        }
    }
}
