package color.column;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ColorColumnSimulation implements Runnable {

    private JFrame frame;

    private ControlPanel controlPanel;

    private DrawingPanel drawingPanel;

    private ColumnModel model;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ColorColumnSimulation());
    }

    public ColorColumnSimulation() {
        this.model = new ColumnModel();
    }

    @Override
    public void run() {
        frame = new JFrame("Color Column Simulation");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                frame.dispose();
                System.exit(0);
            }
        });

        drawingPanel = new DrawingPanel(model);
        frame.add(drawingPanel, BorderLayout.CENTER);

        controlPanel = new ControlPanel(this, model);
        frame.add(controlPanel.getPanel(), BorderLayout.AFTER_LINE_ENDS);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void repaint() {
        drawingPanel.repaint();
    }

    public int getWidth() {
        return frame.getWidth();
    }

    public JPanel getControlPanel() {
        return controlPanel.getPanel();
    }

    public DrawingPanel getDrawingPanel() {
        return drawingPanel.getPanel();
    }
}
