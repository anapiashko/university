package color.column;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeShapeActionListener implements ActionListener {

    private ColumnModel model;

    private ColorColumnSimulation frame;

    public ChangeShapeActionListener(ColorColumnSimulation frame, ColumnModel model) {
        this.frame = frame;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();
        String text = button.getText();
        if (text.equals(Shape.CIRCLE.getName())) {
            model.getLights()[0].setShape(Shape.CIRCLE);
            frame.repaint();
        }
        if (text.equals(Shape.SQUARE.getName())) {
            model.getLights()[0].setShape(Shape.SQUARE);
            frame.repaint();
        }
        if (text.equals(Shape.TRIANGLE.getName())) {
            model.getLights()[0].setShape(Shape.TRIANGLE);
            frame.repaint();
        }
    }
}
