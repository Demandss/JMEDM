package su.demands.jmedm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller();

        JFrame form = new JFrame("HalsteadMetrix");

        form.setResizable(false);

        form.setSize(220, 100);
        form.setLayout(null);
        form.setLocation(300,200);
        form.getContentPane().setBackground(Color.decode("#EEEEEE"));

        form.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(form,
                        "Close?", "Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(1);
                    form.dispose();
                }
            }
        });

        JLabel labelKey = new JLabel("Modifier:");
        labelKey.setBounds(25,2,50, 20);
        form.getContentPane().add(labelKey);

        final JTextArea calcModifier = new JTextArea(10, 1);
        calcModifier.setBounds(80,2,100,20);
        form.getContentPane().add(calcModifier);

        JButton buttonCalc = new JButton("Calculate");
        buttonCalc.setBounds(25,30,150, 20);

        buttonCalc.addActionListener(e -> {
            if (controller.isInteger(calcModifier.getText())) {
                int modifier = Integer.parseInt(calcModifier.getText());
                JOptionPane.showMessageDialog(form,
                        String.format("Время до завершения тестирования: %.2f \n Общее время тестирования: %.0f",
                                controller.getTimeUntilTestingIsCompleted(modifier),
                                Math.floor(controller.getTotalTestingTime(modifier)))
                        ,"Calculations",
                        JOptionPane.INFORMATION_MESSAGE,null);
            } else {
                JOptionPane.showMessageDialog(form,
                        "Enter the modifier!","ERROR",
                        JOptionPane.ERROR_MESSAGE,null);
            }});

        form.getContentPane().add(buttonCalc);

        form.setVisible(true);
    }
}
