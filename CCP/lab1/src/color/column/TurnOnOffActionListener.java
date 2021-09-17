package color.column;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TurnOnOffActionListener implements ActionListener {

    private ColumnModel model;

    private ColorColumnSimulation frame;

    public TurnOnOffActionListener(ColorColumnSimulation frame, ColumnModel model) {
        this.frame = frame;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();
        String text = button.getText();
        if (text.equals("Turn On/Off")) {
            model.setRedLight(!model.isLightOn(Color.RED));
            frame.repaint();
        }
    }
}
