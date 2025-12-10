package Window;

import Anything.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Helper {

    JFrame frame = getjFrame();
    BackGround bg;
    JPanel helpPanel = new JPanel();

    public Helper(){

        try{
            bg = new BackGround("Images/Gray_fon.png");
            bg.setLayout(new BorderLayout());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
        helpPanel.setOpaque(false);

        JButton main = new JButton("Главная");
        main.setOpaque(false);
        main.setFocusable(false);
        main.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SrcWindow();
                frame.dispose();
            }
        });

        helpPanel.add(main);

        addHelp("Не загружается учебник",
                "Проверьте путь к PDF, убедитесь, что файл не перемещён и не переименован.");

        addHelp("Не сохраняется прогресс",
                "Проверьте подключение к базе данных и правильность URL, логина и пароля.");

        addHelp("Окно не открывается по центру",
                "Удалите или обновите сохранённые координаты окна в UserSession.");
        addHelp("Связь с разработчиком",
                "Почта: industrik117@yandex.ru");

        JScrollPane scrollPane = new JScrollPane(helpPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        bg.add(scrollPane,BorderLayout.CENTER);

        frame.add(bg);
        frame.setVisible(true);
    }

    private void addHelp(String question, String answer){

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        JButton button = new JButton(question);
        button.setFont(new Font("Bahnschrift",Font.BOLD,25));
        button.setFocusPainted(false);

        JTextArea textArea = new JTextArea(answer);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setVisible(false);
        textArea.setBackground(Color.white);
        textArea.setFont(new Font("Bahnschrift",Font.BOLD,25));

        panel.add(button,BorderLayout.NORTH);
        panel.add(textArea,BorderLayout.CENTER);

        button.addActionListener((ActionEvent e)-> {
            textArea.setVisible(!textArea.isVisible());
            frame.revalidate();
            frame.repaint();
        });

        helpPanel.add(panel);
        helpPanel.add(Box.createVerticalStrut(10));
    }



    static JFrame getjFrame(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UserSession session = UserSession.getInstance();
        frame.setSize(session.getWindowWidth(), session.getWindowHeight());

        if (session.isWindowPositionSaved()) {
            frame.setLocation(session.getWindowX(), session.getWindowY());
        } else {
            frame.setLocationRelativeTo(null);
        }

        frame.setTitle("Поддержка");
        frame.setLayout(new BorderLayout());
        return frame;
    }
}
