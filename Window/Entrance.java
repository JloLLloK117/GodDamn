package Window;

import Anything.BackGround;
import Anything.DataBase;
import Anything.SimpleAutoComplete;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class Entrance implements ActionListener {

    JFrame frame;
    JButton button;
    BackGround bg;
    JTextField jTextField;
    JPasswordField jPasswordField;
    JLabel createAccount = new JLabel();

    public Entrance(){

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
        frame.setTitle("Вход в систему");
        frame.setLocationRelativeTo(null);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10,50,10,50);

        ImageIcon imageIcon = new ImageIcon("Images/Shesterni.png");

        JLabel jLabel = new JLabel("<html>Вход<br/> в Информационную систему<html>",imageIcon,SwingConstants.CENTER);
        jLabel.setFont(new Font("Bahnschrift", Font.BOLD, 25));
        jLabel.setOpaque(false);
        jLabel.setForeground(Color.black);
        bg.add(jLabel,constraints);

        JLabel jLabel1 = new JLabel("Имя пользователя",SwingConstants.CENTER);
        jLabel1.setFont(new Font("Bahnschrift", Font.BOLD, 21));
        jLabel1.setForeground(Color.black);
        bg.add(jLabel1,constraints);

        jTextField = new JTextField(25);
        jTextField.setOpaque(true);
        jTextField.setBackground(Color.white);
        bg.add(jTextField,constraints);

        SimpleAutoComplete.setupAutoComplete(jTextField);

        bg.add(jTextField, constraints);

        JLabel jLabel2 = new JLabel("Пароль",SwingConstants.CENTER);
        jLabel2.setFont(new Font("Bahnschrift", Font.BOLD, 21));
        jLabel2.setForeground(Color.black);
        bg.add(jLabel2,constraints);

        jPasswordField = new JPasswordField(25);
        jPasswordField.setOpaque(true);
        jPasswordField.setBackground(Color.white);
        bg.add(jPasswordField,constraints);

        button = new JButton("Вход");
        button.setFocusable(false);
        button.addActionListener(this);
        bg.add(button,constraints);

        createAccount = new JLabel("Создать аккаунт",SwingConstants.CENTER);
        createAccount.setFont(new Font("Bahnschrift", Font.BOLD, 15));
        createAccount.setForeground(Color.black);
        createAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RgstrWindow rgstrWindow = new RgstrWindow();
                frame.dispose();
            }
        });
        bg.add(createAccount,constraints);

        JLabel passwordRecovery = new JLabel("Восстановление пароля",SwingConstants.CENTER);
        passwordRecovery.setFont(new Font("Bahnschrift", Font.BOLD, 15));
        passwordRecovery.setForeground(Color.black);
        passwordRecovery.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new PasswordRecovery();
                frame.dispose();
            }
        });
        bg.add(passwordRecovery,constraints);

        frame.add(bg);
        frame.setVisible(true);

    }

    static JFrame getjFrame(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.setTitle("Вход в систему");
        return frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String username = jTextField.getText().trim();
        String password = new String(jPasswordField.getPassword()).trim();

        if(e.getSource() == createAccount){
            RgstrWindow rgstrWindow = new RgstrWindow();
            frame.dispose();
        }

        if((username.isEmpty() || password.isEmpty()) && e.getSource()==button){
            JOptionPane.showMessageDialog(frame,"Пожалуйста, заполните поля.");
            return;
        }
        else if(DataBase.login(username,password)&&e.getSource()==button){

            Anything.UserSession.getInstance().login(username);

            new Thread(()->{
                JOptionPane.showMessageDialog(frame, "Пользователь успешно вошёл.");
            }).start();
            Timer timer = new Timer(2000, ev->{
                new SrcWindow();
                frame.dispose();
            });
            timer.setRepeats(false);
            timer.start();
        }
        else if(!DataBase.isExist(username)){
            JOptionPane.showMessageDialog(frame,"Пользователя не существует.");
        }
        else if(!DataBase.login(username,password) &&  e.getSource()==button){
            JOptionPane.showMessageDialog(frame, "Имя пользователя или пароль не корректны.");
        }

    }
}
