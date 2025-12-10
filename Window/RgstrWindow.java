package Window;

import Anything.BackGround;
import Anything.DataBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RgstrWindow implements ActionListener {

    JFrame frame;
    BackGround bg;
    JButton button, button2;
    JTextField jTextField;
    JTextField jTextField1;
    JPasswordField jPasswordField;

    public RgstrWindow() {

        try{
            bg = new BackGround("Images/Gray_fon.png");
            bg.setLayout(new GridBagLayout());
        }catch(IOException e){
            System.out.println();
        }
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch(Exception e){
            System.out.println();
        }

        frame = getjFrame();
        frame.setTitle("Информационная система");
        frame.setLocationRelativeTo(null);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5,50,10,50);

        ImageIcon imageIcon = new ImageIcon("Images/Shesterni.png");

        JLabel jLabel = new JLabel("<html>Информационная<br/>система<html>",imageIcon,SwingConstants.CENTER);
        jLabel.setFont(new Font("Bahnschrift", Font.BOLD, 25));
        jLabel.setOpaque(false);
        jLabel.setForeground(Color.black);
        bg.add(jLabel,constraints);

        JLabel jLabel3 = new JLabel("Регистрация",SwingConstants.CENTER);
        jLabel3.setFont(new Font("Bahnschrift", Font.BOLD, 30));
        jLabel3.setOpaque(false);
        jLabel3.setForeground(Color.black);
        bg.add(jLabel3,constraints);

        JLabel jLabel1 = new JLabel("Имя пользователя",SwingConstants.CENTER);
        jLabel1.setFont(new Font("Bahnschrift", Font.BOLD, 21));
        jLabel1.setOpaque(false);
        jLabel1.setForeground(Color.black);
        bg.add(jLabel1,constraints);

        jTextField = new JTextField(25);
        jTextField.setBackground(Color.white);
        bg.add(jTextField,constraints);

        JLabel jLabel2 = new JLabel("Пароль",SwingConstants.CENTER);
        jLabel2.setFont(new Font("Bahnschrift", Font.BOLD, 21));
        jLabel2.setOpaque(false);
        jLabel2.setForeground(Color.black);
        bg.add(jLabel2,constraints);

        jPasswordField = new JPasswordField(25);
        jPasswordField.setBackground(Color.white);
        bg.add(jPasswordField,constraints);

        JLabel jLabel4 = new JLabel("Кодовое слово",SwingConstants.CENTER);
        jLabel4.setFont(new Font("Bahnschrift", Font.BOLD, 21));
        jLabel4.setOpaque(false);
        jLabel4.setForeground(Color.black);
        bg.add(jLabel4,constraints);

        jTextField1 = new JTextField(25);
        jTextField1.setBackground(Color.white);
        bg.add(jTextField1,constraints);

        button = new JButton("Создать");
        button.setFocusable(false);
        button.addActionListener(this);
        bg.add(button,constraints);

        button2 = new JButton("Назад");
        button2.setFocusable(false);
        button2.addActionListener(this);
        bg.add(button2,constraints);

        frame.add(bg);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        String username = jTextField.getText().trim();
        String passwordWord = jTextField1.getText().trim();
        String password = new String(jPasswordField.getPassword()).trim();

        if(e.getSource() == button2){
            Entrance entrance = new Entrance();
            frame.dispose();
            return;
        }

        if((username.isEmpty() || password.isEmpty() || passwordWord.isEmpty())){
            JOptionPane.showMessageDialog(frame, "Пожалуйста, заполните поля.");
            return;
        }

        if(e.getSource()==button){
            if(username.length()<=4){
                JOptionPane.showMessageDialog(frame,"Имя пользователя должно быть более 4 символов.");
                return;
            }
            if(!username.matches("^[a-zA-Zа-яА-Я0-9\\s\\-_.]+$")){
                JOptionPane.showMessageDialog(frame,"Имя должно содержать только русские, английские символы, цифры, пробелы, дефисы, подчеркивания и точки.");
                return;
            }

            if(password.length()<=6){
                JOptionPane.showMessageDialog(frame,"Пароль должен быть больше 6 символов.");
                return;
            }
            if(password.contains(" ")){
                JOptionPane.showMessageDialog(frame,"Пароль не может содержать пробелы.");
                return;
            }
            if(passwordWord.length()<=6){
                JOptionPane.showMessageDialog(frame,"Слово для восстановления пароля не может быть меньше 6 символов.");
                return;
            }
            if(passwordWord.contains(" ")){
                JOptionPane.showMessageDialog(frame,"Слово для восстановления пароля не может содержать пробелы.");
                return;
            }

            if(!DataBase.isExist(username)){
                if(DataBase.registrationUser(username,password,passwordWord)){

                    Anything.UserSession.getInstance().login(username);

                    new Thread(() -> {
                        JOptionPane.showMessageDialog(frame,
                                "Пользователь был успешно создан.\nВыполняется вход.");
                    }).start();
                    Timer timer = new Timer(2000, ev -> {
                        new SrcWindow();
                        frame.dispose();
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
                else{

                    JOptionPane.showMessageDialog(frame, "Имя пользователя или пароль неверные.");

                }
            }
            else if(DataBase.isExist(username)){

                JOptionPane.showMessageDialog(frame, "Пользователь уже существует.");

            }
        }
    }

    static JFrame getjFrame(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.setTitle("Регистрация");
        return frame;
    }
}
