package Window.Documents;

import Anything.BackGround;
import Anything.UserSession;

import javax.swing.*;
import java.awt.*;

public class Documents {

    JFrame frame = getFrame();
    BackGround bg;


    public Documents(){
        try{
            bg = new BackGround("Images/Gray_fon.png");
            bg.setLayout(new GridBagLayout());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }



        frame.add(bg);
        frame.setVisible(true);
    }
    static JFrame getFrame(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UserSession userSession = UserSession.getInstance();
        frame.setSize(userSession.getWindowWidth(), userSession.getWindowHeight());

        if(userSession.isWindowPositionSaved()){
            frame.setLocation(userSession.getWindowX(), userSession.getWindowY());
        }else{
            frame.setLocationRelativeTo(null);
        }
        frame.setTitle("Документы");
        frame.setVisible(true);
        return frame;
    }
}
