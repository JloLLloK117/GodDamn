package Window;

import Anything.BackGround;
import Anything.DataBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

import static Anything.DataBase.hashPassword;

public class PasswordRecovery implements ActionListener {

    JFrame frame;
    BackGround bg;
    JTextField username, passwordWord;
    JButton jButton, jButton2;
    JPasswordField passwordField;

    public PasswordRecovery() {
        try {
            bg = new BackGround("Images/Gray_fon.png");
            bg.setLayout(new GridBagLayout());
        } catch (IOException e) {
            System.out.println();
        }
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            System.out.println();
        }
        frame = getjFrame();
        frame.setTitle("Восстановление пароля");
        frame.setLocationRelativeTo(null);

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 50, 10, 50);

        ImageIcon imageIcon = new ImageIcon("Images/Shesterni.png");

        JLabel jLabel = new JLabel("<html>Восстановление<br/>пароля<html>", imageIcon, JLabel.CENTER);
        jLabel.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        jLabel.setOpaque(false);
        jLabel.setBackground(Color.black);
        bg.add(jLabel, c);

        JLabel jLabel1 = new JLabel("Имя пользователя", SwingConstants.CENTER);
        jLabel1.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        jLabel1.setOpaque(false);
        bg.add(jLabel1, c);

        username = new JTextField();
        username.setOpaque(true);
        username.setBackground(Color.white);
        bg.add(username, c);

        JLabel jLabel2 = new JLabel("Кодовое слово", SwingConstants.CENTER);
        jLabel2.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        jLabel2.setOpaque(false);
        bg.add(jLabel2, c);

        passwordWord = new JTextField(25);
        passwordWord.setOpaque(true);
        passwordWord.setBackground(Color.white);
        bg.add(passwordWord, c);

        JLabel jLabel3 = new JLabel("Новый пароль", SwingConstants.CENTER);
        jLabel3.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        jLabel3.setOpaque(false);
        bg.add(jLabel3, c);

        passwordField = new JPasswordField(25);
        passwordField.setOpaque(true);
        passwordField.setBackground(Color.white);
        bg.add(passwordField, c);

        jButton = new JButton("Изменить");
        jButton.setFocusPainted(false);
        jButton.addActionListener(this);
        bg.add(jButton, c);

        jButton2 = new JButton("Назад");
        jButton2.setFocusPainted(false);
        jButton2.addActionListener(this);
        bg.add(jButton2, c);

        frame.add(bg);
        frame.setVisible(true);
    }

    static JFrame getjFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.setTitle("Восстановление пароля");
        return frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == jButton2) {
            new Entrance();
            frame.dispose();
            return;
        }

        if (e.getSource() == jButton) {
            String usernameRecovery = username.getText().trim();
            String passwordWordRecovery = passwordWord.getText().trim();
            String passwordCreate = new String(passwordField.getPassword()).trim();

            // 1. Проверка на заполненность
            if (usernameRecovery.isEmpty() || passwordWordRecovery.isEmpty() || passwordCreate.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Заполните все поля.");
                return;
            }

            // 2. Проверка длины пароля
            if (passwordCreate.length() <= 6) {
                JOptionPane.showMessageDialog(frame, "Пароль должен быть больше 6 символов.");
                return;
            }

            // 3. Проверка существования пользователя
            if (!DataBase.isExist(usernameRecovery)) {
                JOptionPane.showMessageDialog(frame, "Пользователь не найден.");
                return;
            }

            // 4. Проверка кодового слова
            String storedPasswordWord = DataBase.getPasswordWord(usernameRecovery);
            if (storedPasswordWord == null ||
                    !storedPasswordWord.equals(hashPassword(passwordWordRecovery))) {
                JOptionPane.showMessageDialog(frame, "Неверное кодовое слово.");
                return;
            }

            // 5. Теперь можно сбросить пароль
            boolean success = DataBase.resetPassword(usernameRecovery, passwordWordRecovery, passwordCreate);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Пароль успешно изменён!");
                new Entrance();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Не удалось изменить пароль. Попробуйте позже.");
            }
        }
    }
}
