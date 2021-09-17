package color.column;

import javax.swing.*;
import java.awt.*;

public class ControlPanel {

    private JButton startButton;

    private JPanel panel;

    private ColumnModel model;

    private ColorColumnSimulation frame;

    public ControlPanel(ColorColumnSimulation frame, ColumnModel model) {
        this.frame = frame;
        this.model = model;
        createPartControl();
    }

    private void createPartControl() {
        panel = new JPanel(new FlowLayout());
        JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setBorder(BorderFactory.createEmptyBorder(
                5, 5, 5, 5));
        Font normalFont = innerPanel.getFont().deriveFont(16f);
        Font titleFont = innerPanel.getFont().deriveFont(24f)
                .deriveFont(Font.BOLD);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        createDelayFields(innerPanel, titleFont, normalFont, gbc);

        panel.add(innerPanel);
    }

    private void createDelayFields(JPanel innerPanel, Font titleFont, Font normalFont, GridBagConstraints gbc) {
        gbc.gridy++;
        JLabel defaultLabel = new JLabel("Handling colors by buttons");
        defaultLabel.setFont(titleFont);
        defaultLabel.setHorizontalAlignment(JLabel.CENTER);
        innerPanel.add(defaultLabel, gbc);

        /**
         * 1
         */
//        gbc.gridwidth = 2;
//        gbc.gridx = 0;
//        gbc.gridy++;
//        JButton changeButton = new JButton("Work");
//        changeButton.setFont(normalFont);
//        changeButton.addActionListener(new WorkFailActionListener(frame, model));
//        innerPanel.add(changeButton, gbc);
//
//        gbc.gridwidth = 2;
//        gbc.gridx = 0;
//        gbc.gridy++;
//        startButton = new JButton("Crush");
//        startButton.setFont(normalFont);
//        startButton.addActionListener(new WorkFailActionListener(frame, model));
//        innerPanel.add(startButton, gbc);

        /**
         * 2
         */
//			gbc.gridwidth = 2;
//			gbc.gridx = 0;
//			gbc.gridy++;
//			JButton changeButton = new JButton("Turn On/Off");
//			changeButton.setFont(normalFont);
//			changeButton.addActionListener(new TurnOnOffActionListener(frame, model));
//			innerPanel.add(changeButton, gbc);

        /**
         * 3
         */
//        gbc.gridwidth = 2;
//        gbc.gridx = 0;
//        gbc.gridy++;
//        startButton = new JButton(Shape.CIRCLE.getName());
//        startButton.setFont(normalFont);
//        startButton.addActionListener(new ChangeShapeActionListener(frame, model));
//        innerPanel.add(startButton, gbc);
//
//        gbc.gridwidth = 2;
//        gbc.gridx = 0;
//        gbc.gridy++;
//        startButton = new JButton(Shape.SQUARE.getName());
//        startButton.setFont(normalFont);
//        startButton.addActionListener(new ChangeShapeActionListener(frame, model));
//        innerPanel.add(startButton, gbc);
//
//        gbc.gridwidth = 2;
//        gbc.gridx = 0;
//        gbc.gridy++;
//        JButton changeButton = new JButton(Shape.TRIANGLE.getName());
//        changeButton.setFont(normalFont);
//        changeButton.addActionListener(new ChangeShapeActionListener(frame, model));
//        innerPanel.add(changeButton, gbc);

        /**
         * 4
         */
//			gbc.gridwidth = 2;
//			gbc.gridx = 0;
//			gbc.gridy++;
//			JButton changeButton = new JButton("Left");
//			changeButton.setFont(normalFont);
//			changeButton.addActionListener(new MoveActionListener(frame, model));
//			innerPanel.add(changeButton, gbc);
//
//			gbc.gridwidth = 2;
//			gbc.gridx = 0;
//			gbc.gridy++;
//			startButton = new JButton("Right");
//			startButton.setFont(normalFont);
//			startButton.addActionListener(new MoveActionListener(frame, model));
//			innerPanel.add(startButton, gbc);

        /**
         * 5
         */
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy++;
        JButton changeButton = new JButton("Up");
        changeButton.setFont(normalFont);
        changeButton.addActionListener(new ChangeSizeActionListener(frame, model));
        innerPanel.add(changeButton, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy++;
        startButton = new JButton("Down");
        startButton.setFont(normalFont);
        startButton.addActionListener(new ChangeSizeActionListener(frame, model));
        innerPanel.add(startButton, gbc);

    }

    public JPanel getPanel() {
        return panel;
    }
}