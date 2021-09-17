package color.column;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MoveActionListener implements ActionListener {

    private ColumnModel model;

    private ColorColumnSimulation frame;

    private Timer timerRight = new Timer(1, new HandlerRightClass());
    private Timer timerLeft = new Timer(1, new HandlerLeftClass());

    public MoveActionListener(ColorColumnSimulation frame, ColumnModel model) {
        this.frame = frame;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();
        String text = button.getText();
        if (text.equals("Right")) {
            timerRight.start();
            frame.repaint();
        } else if (text.equals("Left")) {
            timerLeft.start();
            frame.repaint();
        }
    }

    class HandlerRightClass implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            DrawingPanel panel = frame.getDrawingPanel();

            if (DrawingPanel.x <= panel.getWidth() - panel.getRadius()) {
                DrawingPanel.x ++;
            }   else {
                timerRight.stop();
            }
            frame.repaint();
        }
    }

    class HandlerLeftClass implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            DrawingPanel panel = frame.getDrawingPanel();

            if (DrawingPanel.x >= panel.getRadius()) {
                DrawingPanel.x --;
            } else {
                timerLeft.stop();
            }
            frame.repaint();
        }
    }
}