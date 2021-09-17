package color.column;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public static int MAX_RADIUS;
    public static int MIN_RADIUS;

    private int width;
    private int height;
    public int radius;
    private int margin;

    /**
     * 4
     */
    public static int x = 100;
    /**
     * 5
     */
    public static int y = 150; //150

    private ColumnModel model;

    public DrawingPanel(ColumnModel model) {
        this.model = model;
        this.width = 180;
        this.height = model.getLights().length * width;
        MAX_RADIUS = width * 9 / 20;
        MIN_RADIUS = width * 3 / 20;
        this.radius = MIN_RADIUS;
        //this.radius = MIN_RADIUS;
        this.margin = width / 10;


//			this.setBackground(Color.WHITE);
//			this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
        this.setPreferredSize(new Dimension(width + margin + margin,
                height + margin + margin));
    }


    /**
     * 5
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(5f));


        //x = getWidth() / 2;
        int yStart = width / 2 + margin;
        int index = 0;
        int diameter = radius + radius;
        //y = yStart;
        fillOval(g2d, index, x, y, diameter);
        drawOval(g2d, x, y, diameter);
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setStroke(new BasicStroke(5f));
//
////        int x = getWidth() / 2;
//        int yStart = width / 2 + margin;
//        int index = 0;
//        int diameter = radius + radius;
//
//        for (int y = yStart; y <= height; y += width) {
//            Shape modelShape = model.getLights()[index].getShape();
//            switch (modelShape) {
//                case SQUARE: {
//                    fillSquare(g2d, index, x, y, diameter);
//                    drawSquare(g2d, x, y, diameter);
//                    break;
//                }
//                case TRIANGLE: {
//                    fillTriangle(g2d, index, x, y, diameter);
//                    drawTriangle(g2d, x, y, diameter);
//                    break;
//                }
//                default: {
//                    fillOval(g2d, index, x, y, diameter);
//                    drawOval(g2d, x, y, diameter);
//                    break;
//                }
//            }
//            index++;
//        }
//    }

    private void drawOval(Graphics2D g2d, int x, int y, int diameter) {
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x - radius, y - radius, diameter, diameter);
    }

    private void fillOval(Graphics2D g2d, int index, int x, int y, int diameter) {
        if (model.isLightOn(index)) {
            g2d.setColor(model.getColor(index));
            g2d.fillOval(x - radius, y - radius, diameter, diameter);
        }
    }

    private void drawSquare(Graphics2D g2d, int x, int y, int diameter) {
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - radius, y - radius, diameter, diameter);
    }

    private void fillSquare(Graphics2D g2d, int index, int x, int y, int diameter) {
        if (model.isLightOn(index)) {
            g2d.setColor(model.getColor(index));
            g2d.fillRect(x - radius, y - radius, diameter, diameter);
        }
    }

    private void drawTriangle(Graphics2D g2d, int x, int y, int diameter) {
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(new int[]{x - radius, x - radius + diameter / 2, x - radius + diameter},
                new int[]{y - radius + diameter, y - radius, y - radius + diameter}, 3);
    }

    private void fillTriangle(Graphics2D g2d, int index, int x, int y, int diameter) {
        if (model.isLightOn(index)) {
            g2d.setColor(model.getColor(index));
            g2d.fillPolygon(new int[]{x - radius, x - radius + diameter / 2, x - radius + diameter},
                    new int[]{y - radius + diameter, y - radius, y - radius + diameter}, 3);
        }
    }

    public DrawingPanel getPanel() {
        return this;
    }

    public int getRadius() {
        return radius;
    }
}