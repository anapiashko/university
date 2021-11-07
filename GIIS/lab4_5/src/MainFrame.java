import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainFrame extends JFrame {

    Integer scoreGame = 0;
    Integer scoreGameNumber = 0;
    Integer scoreGameImage = 0;
    Integer scoreGameNumberWin = 0;
    Integer scoreGameImageWin = 0;
    String imageSrc;
    String statisticFile = "statistics.txt";
    Puzzle mainPanel;
    String mode;
    boolean win;
    boolean shift;

    public MainFrame() {

        setJMenuBar(createMenu());
        loadStatistics();
        imageSrc = "src//images//nature.png";
        mode = "numbers";
        win = false;
        shift = true;
        mainPanel = new Puzzle();
        scoreGame++;
        scoreGameNumber++;
        add(mainPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                closeWindow();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (shift) {
                    checkWin();
                }
                if (shift) {
                    switch (evt.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            mainPanel.move("up");
                            break;
                        case KeyEvent.VK_DOWN:
                            mainPanel.move("down");
                            break;
                        case KeyEvent.VK_LEFT:
                            mainPanel.move("left");
                            break;
                        case KeyEvent.VK_RIGHT:
                            mainPanel.move("right");
                            break;
                    }
                    mainPanel.checkWin();
                    checkWin();
                }
            }
        });
    }

    private void checkWin() {
        if (mainPanel.win) {
            win = true;
            mainPanel.win = false;
        }

        if (win) {
            if (mode.equals("numbers")) {
                scoreGameNumberWin++;
            }
            else {
                scoreGameImageWin++;
            }
            win = false;
            shift = false;
        }
    }

    private boolean newGame(String str) {

        int res = JOptionPane.showConfirmDialog(MainFrame.this, "The game will start over" + str + ". Continue?",
                "New game", JOptionPane.YES_NO_OPTION);

        if (res == JOptionPane.YES_OPTION) {
            remove(mainPanel);
            if (mode.equals("numbers")) {
                mainPanel = new Puzzle();
                scoreGameNumber++;
            }
            if (mode.equals("image")) {
                mainPanel = new Puzzle(imageSrc);
                scoreGameImage++;
            }
            scoreGame++;
            win = false;
            shift = true;
            add(mainPanel);
            checkWin();
            pack();
        } else {
            return false;
        }
        return true;
    }

    private JMenuBar createMenu() {

        JMenuBar menu = new JMenuBar();
        JMenu game = new JMenu("Game");
        game.setFont(new Font(game.getFont().getFontName(), Font.PLAIN, 15));
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(e -> newGame(""));
        JMenuItem statistics = new JMenuItem("Statistics");

        statistics.addActionListener(e -> {
            checkWin();
            JOptionPane.showMessageDialog(MainFrame.this,
                    new String[]{"Всего игр: " + scoreGame + " -> Всего побед: " + (scoreGameNumberWin + scoreGameImageWin),
                            "Кол-во игр с цифрами: " + scoreGameNumber + " -> Побед: " + scoreGameNumberWin,
                            "Кол-во игр с картинками: " + scoreGameImage + " -> Побед: " + scoreGameImageWin
                    }, "Statistics", JOptionPane.INFORMATION_MESSAGE);
        });
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> {
            closeWindow();
        });

        game.add(newGame);
        game.add(statistics);
        game.addSeparator();
        game.add(exit);

        JMenu settings = new JMenu("Settings");
        settings.setFont(new Font(settings.getFont().getFontName(), Font.PLAIN, 15));
        JRadioButtonMenuItem numbers = new JRadioButtonMenuItem("Numbers");
        JRadioButtonMenuItem image = new JRadioButtonMenuItem("Image");
        numbers.setEnabled(false);
        numbers.setSelected(true);

        numbers.addActionListener(e -> {
            numbers.setEnabled(false);
            image.setSelected(false);
            mode = "numbers";
            if (!newGame(" with numbers")) {
                mode = "image";
                image.setSelected(true);
                numbers.setSelected(false);
                numbers.setEnabled(true);
            } else
                image.setEnabled(true);
        });

        image.addActionListener(e -> {
            image.setEnabled(false);
            numbers.setSelected(false);
            mode = "image";
            if (!newGame(" with image")) {
                mode = "numbers";
                numbers.setSelected(true);
                image.setSelected(false);
                image.setEnabled(true);
            } else
                numbers.setEnabled(true);
        });

        JMenuItem loadImage = new JMenuItem("Load Image");
        loadImage.addActionListener(e -> {
            checkWin();
            JFileChooser c = new JFileChooser();
            c.setCurrentDirectory(new File("src//images"));
            int rVal = c.showOpenDialog(MainFrame.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                if (c.getSelectedFile().toString().contains(".jpg") || c.getSelectedFile().toString().contains(".png")) {
//                    JOptionPane.showMessageDialog(MainFrame.this, "Wait");
                    remove(mainPanel);
                    mainPanel = new Puzzle(c.getSelectedFile().toString());
                    imageSrc = c.getSelectedFile().toString();
                    image.setEnabled(false);
                    numbers.setSelected(false);
                    image.setSelected(true);
                    mode = "image";
                    numbers.setEnabled(true);
                    scoreGameImage++;
                    scoreGame++;
                    add(mainPanel);
                    pack();
//                    JOptionPane.showMessageDialog(MainFrame.this, "Load success");
                }
            } else if (rVal == JFileChooser.CANCEL_OPTION) {
//                JOptionPane.showMessageDialog(MainFrame.this, "Load cancel",
//                        "", JOptionPane.OK_OPTION);
            }
            else {
                JOptionPane.showMessageDialog(MainFrame.this, "Pick image in jpg or png format",
                        "Error", JOptionPane.OK_OPTION);
            }

        });

        settings.add(numbers);
        settings.add(image);
        settings.add(loadImage);
        JMenu help = new JMenu("Help");
        help.setFont(new Font(help.getFont().getFontName(), Font.PLAIN, 15));
        JMenuItem rules = new JMenuItem("Rules");
        rules.addActionListener(e -> {
            checkWin();
            JOptionPane.showMessageDialog(MainFrame.this,
                    new String[]{"Цель игры: перемещая костяшки по коробке,",
                            "добиться упорядочивания их по номерам от 1 до 15, ",
                            "желательно, сделав как можно меньше перемещений."
                    }, "Rules", JOptionPane.INFORMATION_MESSAGE);
        });

        help.add(rules);

        menu.add(game);
        menu.add(settings);
        menu.add(help);
        menu.setPreferredSize(new Dimension(menu.getWidth(), menu.getHeight() + 30));
        return menu;
    }

    private void closeWindow() {
        checkWin();
        saveStatistics();
        setVisible(false);
        dispose();
    }

    private void loadStatistics() {
        try {
            File file = new File(statisticFile);
            if (file.exists()) {
                ArrayList<String> list = new ArrayList<>();
                Pattern p = Pattern.compile("(\\d+).+(\\d+)");
                Files.lines(Paths.get(statisticFile), StandardCharsets.UTF_8)
                        .forEach(e -> list.add(e));
                Matcher m = p.matcher(list.get(1));
                if (m.find())
                    scoreGame = Integer.parseInt(m.group(1));
                m = p.matcher(list.get(2));
                if (m.find()) {
                    scoreGameNumber = Integer.parseInt(m.group(1));
                    scoreGameNumberWin = Integer.parseInt(m.group(2));
                }
                m = p.matcher(list.get(3));
                if (m.find()) {
                    scoreGameImage = Integer.parseInt(m.group(1));
                    scoreGameImageWin = Integer.parseInt(m.group(2));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStatistics() {
        try {
            File file = new File(statisticFile);
            file.createNewFile();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Статистика:\n");
                writer.write("Всего игр: " + scoreGame + " -> Всего побед: " + (scoreGameNumberWin + scoreGameImageWin) + "\n");
                writer.write("Кол-во игр с цифрами: " + scoreGameNumber + " -> Побед: " + scoreGameNumberWin + "\n");
                writer.write("Кол-во игр с картинками: " + scoreGameImage + " -> Побед: " + scoreGameImageWin + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new MainFrame();
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Пятнашки");
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLayout(new GridBagLayout());
        frame.pack();
    }
}
