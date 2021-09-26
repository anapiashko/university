import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class FilterImage extends JFrame {

    private String imageName;
    private Double percentNoise;
    private BufferedImage bufImage = null;
    private Random rnd = new Random();
    private Integer sizeX;
    private Integer sizeY;
    private BufferedImage noiseImage;

    public static String folder = "C:\\Users\\anapi\\Documents\\university\\GIIS\\lab1";

    public FilterImage(String imageName, String sizeX, String sizeY, boolean flag) {
        this.imageName = imageName;
        this.sizeX = Integer.parseInt(sizeX);
        this.sizeY = Integer.parseInt(sizeY);

        setTitle("Noise Reduction Result");
        setSize(2000, 1020);
        percentNoise = 0.1;
        this.noiseImage();

        for (int i = 0; i < 2; i++) {
            if (!flag)
                processingFilter(i);
            else {
                processingFilter(i);
                swap();
                processingFilter(i);
                swap();
            }
        }

        JScrollPane scrollPane = new JScrollPane();
        JLabel l1 = new JLabel();
        l1.setSize(800, 800);
        l1.setIcon(new ImageIcon(resize(noiseImage, l1)));

        JLabel l2 = new JLabel();
        l2.setSize(800, 800);
        l2.setIcon(new ImageIcon(resize(bufImage, l2)));

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(l1);
        p.add(l2);

        scrollPane.setViewportView(p);
        add(scrollPane);
        pack();
    }

    private void noiseImage() {
        try {
            bufImage = ImageIO.read(new File(imageName));
            int height = bufImage.getHeight();
            int width = bufImage.getWidth();
            Color white = new Color(255, 255, 255);
            noiseImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (rnd.nextDouble() < percentNoise) {
                        bufImage.setRGB(j, i, white.getRGB());
                        noiseImage.setRGB(j, i, bufImage.getRGB(j, i));
                    } else
                        noiseImage.setRGB(j, i, bufImage.getRGB(j, i));
                }
            }
            saveImage(folder + "\\generated\\noised.jpg", bufImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processingFilter(int index) {
        Integer height = bufImage.getHeight();
        Integer width = bufImage.getWidth();
        for (int i = 0; i < height - sizeY; i++) {
            for (int j = 0; j < width - sizeX; j++) {
                bufImage.setRGB(j + ((sizeX - 1) / 2), i + ((sizeY - 1) / 2), avgColor(i, j).getRGB());
            }
        }
        saveImage(folder + "\\generated\\processed" + sizeX + "x" + sizeY + "_" + index + ".jpg", bufImage);
    }

    public BufferedImage resize(BufferedImage img, JLabel panel) {
        Image tmp = img.getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_SMOOTH);
        img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return img;
    }

    private Color avgColor(int row, int col) {
        ArrayList<Integer> r = new ArrayList<>();
        ArrayList<Integer> g = new ArrayList<>();
        ArrayList<Integer> b = new ArrayList<>();
        for (int i = row; i < row + sizeY; i++)
            for (int j = col; j < col + sizeX; j++) {
                Color c = new Color(bufImage.getRGB(j, i));
                r.add(c.getRed());
                b.add(c.getBlue());
                g.add(c.getGreen());
            }
        Collections.sort(r);
        Collections.sort(g);
        Collections.sort(b);
        int size = sizeX * sizeY;
        return new Color(r.get((size - 1) / 2), g.get((size - 1) / 2), b.get((size - 1) / 2));
    }

    private void saveImage(String name, BufferedImage buf) {
        try {
            ImageIO.write(buf, "jpg", new File(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void swap() {
        int t = sizeX;
        sizeX = sizeY;
        sizeY = t;
    }
}