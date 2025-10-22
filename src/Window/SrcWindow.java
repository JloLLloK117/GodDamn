package Window;

import Anything.*;
import Window.*;
import Window.Documents.Documents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

public class SrcWindow{

    JFrame jFrame = getjFrame();
    JPanel jPanel;
    BackGround bg;

    public SrcWindow(){

        try{
            bg = new BackGround("Images/Gray_fon.png");
            bg.setLayout(new BorderLayout());
        }catch(IOException e){
            System.out.println();
        }
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch(Exception e){
            System.out.println();
        }

        jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        jFrame.add(jPanel,BorderLayout.NORTH);

        JButton jB1 = new JButton("Главная");
        JButton jB2 = new JButton("Документы");
        JButton jB3 = new JButton("Инструкция");
        JButton jB4 = new JButton("Ссылки");
        JButton jB5 = new JButton("Выход из аккаунта");
        JButton jB6 = new JButton("Выход из приложения");


        jPanel.setOpaque(false);
        jPanel.add(jB1);
        jPanel.add(jB2);
        jPanel.add(jB3);
        jPanel.add(jB4);
        jPanel.add(jB5);
        jPanel.add(jB6);

        jB2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                new Documents();
                jFrame.dispose();
            }
        });

        jB5.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                Anything.UserSession.getInstance().logout();
                Entrance entrance = new Entrance();
                jFrame.dispose();
            }
        });

        jB6.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });

        JLabel jLabel = new JLabel("Информационная система", SwingConstants.CENTER);
        jLabel.setFont(new Font("Bahnschrift", Font.BOLD, 25));
        jLabel.setOpaque(false);
        jLabel.setForeground(Color.black);

        JPanel centerPanel = new JPanel(new GridLayout());
        centerPanel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(20,0,20,0);

        centerPanel.add(jLabel,c);


        bg.add(jPanel,BorderLayout.NORTH);
        bg.add(centerPanel,BorderLayout.CENTER);
        jFrame.add(bg);

        jFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                saveWindowState();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                saveWindowState();
            }
        });

        jFrame.setVisible(true);
    }

    static JFrame getjFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UserSession session = UserSession.getInstance();
        frame.setSize(session.getWindowWidth(), session.getWindowHeight());

        if (session.isWindowPositionSaved()) {
            frame.setLocation(session.getWindowX(), session.getWindowY());
        } else {
            frame.setLocationRelativeTo(null);
        }

        frame.setTitle("Информационная система");
        frame.setLayout(new BorderLayout());
        return frame;
    }
    private void saveWindowState() {
        UserSession session = UserSession.getInstance();
        session.setWindowSize(jFrame.getWidth(), jFrame.getHeight());
        session.setWindowPosition(jFrame.getX(), jFrame.getY());
    }
}
