import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleGUICalculator extends JFrame implements ActionListener {
    private JTextField display;
    private JPanel panel;
    private StringBuilder input;
    private double num1, num2, result;
    private char operator;

    public SimpleGUICalculator() {
        input = new StringBuilder();
        num1 = num2 = result = 0;
        operator = ' ';

        setTitle("Simple GUI Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panel, BorderLayout.CENTER);

        String[] buttons = {
                "C", "±", "%", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", ".", "=",
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 28));
            button.addActionListener(this);
            panel.add(button);
        }

        JButton empty = new JButton("");
        empty.setEnabled(false);
        panel.add(empty);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.matches("[0-9]")) {
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
        } else if (cmd.matches("[+\\-*/%]")) {
            if (input.length() > 0) {
                num1 = Double.parseDouble(input.toString());
                operator = cmd.charAt(0);
                input.setLength(0);
                display.setText("");
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
            display.setText("");
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
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                if (num2 == 0) {
                    JOptionPane.showMessageDialog(this, "Error: Division by zero", "Error", JOptionPane.ERROR_MESSAGE);
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
            return String.format("%s", res);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleGUICalculator());
    }
}