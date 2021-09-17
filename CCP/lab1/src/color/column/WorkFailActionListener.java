package color.column;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorkFailActionListener implements ActionListener {

    private ColumnModel model;

    private ColorColumnSimulation frame;

    public WorkFailActionListener(ColorColumnSimulation frame, ColumnModel model) {
        this.frame = frame;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();
        String text = button.getText();
        if (text.equals("Work")) {
            model.setGreenLight(true);
            model.setRedLight(false);
            frame.repaint();
        } else if (text.equals("Crush")) {
            model.setGreenLight(false);
            model.setRedLight(true);
            frame.repaint();
        }
    }
}