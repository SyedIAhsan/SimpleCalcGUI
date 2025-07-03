import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class SimpleGUICalculator extends JFrame implements ActionListener {
    private JTextField display;
    private JPanel panel;
    private StringBuilder input;
    private double num1, num2, result;
    private char operator;

    // Modern color scheme
    private static final Color DARK_BG = new Color(28, 28, 30);
    private static final Color LIGHT_BG = new Color(44, 44, 46);
    private static final Color ACCENT_COLOR = new Color(0, 122, 255);
    private static final Color OPERATOR_COLOR = new Color(255, 149, 0);
    private static final Color FUNCTION_COLOR = new Color(142, 142, 147);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color DISPLAY_BG = new Color(18, 18, 20);

    public SimpleGUICalculator() {
        input = new StringBuilder();
        num1 = num2 = result = 0;
        operator = ' ';

        setTitle("Modern Calculator");
        setSize(380, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Set dark theme
        getContentPane().setBackground(DARK_BG);

        // Create modern display
        display = new JTextField();
        display.setFont(new Font("SF Pro Display", Font.BOLD, 36));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(DISPLAY_BG);
        display.setForeground(TEXT_COLOR);
        display.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        display.setCaretColor(TEXT_COLOR);
        display.setText("0");

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(DISPLAY_BG);
        displayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        displayPanel.add(display, BorderLayout.CENTER);
        add(displayPanel, BorderLayout.NORTH);

        // Create button panel
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 12, 12));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel, BorderLayout.CENTER);

        // Button definitions with colors
        String[][] buttonData = {
                { "C", "±", "%", "÷" },
                { "7", "8", "9", "×" },
                { "4", "5", "6", "−" },
                { "1", "2", "3", "+" },
                { "0", ".", "=", "" }
        };

        for (int i = 0; i < buttonData.length; i++) {
            for (int j = 0; j < buttonData[i].length; j++) {
                String text = buttonData[i][j];
                if (!text.isEmpty()) {
                    ModernButton button = new ModernButton(text);
                    button.addActionListener(this);

                    // Set button colors based on type
                    if (text.matches("[0-9.]")) {
                        button.setColors(LIGHT_BG, TEXT_COLOR, ACCENT_COLOR);
                    } else if (text.matches("[+−×÷]")) {
                        button.setColors(OPERATOR_COLOR, TEXT_COLOR, new Color(255, 169, 20));
                    } else if (text.equals("=")) {
                        button.setColors(ACCENT_COLOR, TEXT_COLOR, new Color(0, 142, 255));
                    } else {
                        button.setColors(FUNCTION_COLOR, TEXT_COLOR, new Color(162, 162, 167));
                    }

                    panel.add(button);
                } else {
                    // Add empty space for the last button
                    JPanel empty = new JPanel();
                    empty.setBackground(DARK_BG);
                    empty.setOpaque(false);
                    panel.add(empty);
                }
            }
        }

        setVisible(true);
    }

    // Custom modern button class
    private static class ModernButton extends JButton {
        private Color bgColor, fgColor, hoverColor;
        private boolean isHovered = false;

        public ModernButton(String text) {
            super(text);
            setFont(new Font("SF Pro Display", Font.BOLD, 24));
            setForeground(TEXT_COLOR);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        public void setColors(Color bg, Color fg, Color hover) {
            this.bgColor = bg;
            this.fgColor = fg;
            this.hoverColor = hover;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color currentBg = isHovered ? hoverColor : bgColor;

            // Draw rounded rectangle background
            g2d.setColor(currentBg);
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 25, 25));

            // Draw text
            g2d.setColor(fgColor);
            g2d.setFont(getFont());
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(getText())) / 2;
            int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2d.drawString(getText(), textX, textY);

            g2d.dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.matches("[0-9]")) {
            if (input.length() == 0 && display.getText().equals("0")) {
                display.setText("");
            }
            input.append(cmd);
            display.setText(input.toString());
        } else if (cmd.equals(".")) {
            if (input.indexOf(".") == -1) {
                if (input.length() == 0) {
                    input.append("0.");
                } else {
                    input.append(".");
                }
                display.setText(input.toString());
            }
        } else if (cmd.matches("[+−×÷%]")) {
            if (input.length() > 0) {
                num1 = Double.parseDouble(input.toString());
                operator = cmd.charAt(0);
                input.setLength(0);
                display.setText(formatResult(num1));
            }
        } else if (cmd.equals("=")) {
            if (operator != ' ' && input.length() > 0) {
                num2 = Double.parseDouble(input.toString());
                calculate();
                display.setText(formatResult(result));
                input.setLength(0);
                operator = ' ';
            }
        } else if (cmd.equals("C")) {
            input.setLength(0);
            num1 = num2 = result = 0;
            operator = ' ';
            display.setText("0");
        } else if (cmd.equals("±")) {
            if (input.length() > 0) {
                if (input.charAt(0) == '-') {
                    input.deleteCharAt(0);
                } else {
                    input.insert(0, '-');
                }
                display.setText(input.toString());
            }
        }
    }

    private void calculate() {
        switch (operator) {
            case '+':
                result = num1 + num2;
                break;
            case '−':
                result = num1 - num2;
                break;
            case '×':
                result = num1 * num2;
                break;
            case '÷':
                if (num2 == 0) {
                    JOptionPane.showMessageDialog(this, "Cannot divide by zero", "Error", JOptionPane.ERROR_MESSAGE);
                    result = 0;
                    break;
                }
                result = num1 / num2;
                break;
            case '%':
                result = num1 % num2;
                break;
            default:
                result = 0;
        }
    }

    private String formatResult(double res) {
        if (res == (long) res)
            return String.format("%d", (long) res);
        else
            return String.format("%.8g", res);
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new SimpleGUICalculator());
    }
}