package Window.Studing;

import Anything.BackGround;
import Anything.DataBase;
import Anything.UserSession;
import Window.SrcWindow;

import org.icepdf.ri.common.*;
import org.icepdf.ri.common.SwingViewBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Textbook extends JFrame {

    JFrame frame;
    BackGround bg;
    JPanel centerPanel;
    JPanel topPanel;
    JPanel pdfPanel;
    SwingController controller;

    public Textbook(){
        try{
            bg = new BackGround("Images/Gray_fon.png");
            bg.setLayout(new BorderLayout());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        frame = getJFrame();

        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        topPanel.setOpaque(false);


        JButton jB1 = new JButton("Учебники");
        jB1.setOpaque(false);
        jB1.setFocusable(false);
        JButton jB2 = new JButton("Продолжить");
        jB2.setOpaque(false);
        jB2.setFocusable(false);
        JButton jB3 = new JButton("Главная");
        jB3.setOpaque(false);
        jB3.setFocusable(false);
        JButton backButton = new JButton("Назад");
        backButton.setOpaque(false);
        backButton.setFocusable(false);
        backButton.setVisible(false);

        topPanel.add(jB1);
        topPanel.add(jB2);
        topPanel.add(jB3);
        topPanel.add(backButton);
        bg.add(topPanel,BorderLayout.NORTH);

        jB3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentProgress();
                new SrcWindow();
                frame.dispose();
            }
        });

        String[] textBook = {"Java","С++_rukovodstvo"};
        JComboBox<String> list =  new JComboBox<>(textBook);
        list.setVisible(false);
        list.setPreferredSize(new Dimension(200,30));

        centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(list);

        bg.add(centerPanel,BorderLayout.CENTER);

        jB1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                list.setVisible(true);
                list.showPopup();
                list.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selected =  (String) list.getSelectedItem();
                        if(selected != null){
                            openSelectedTextbook(selected,backButton);
                        }
                    }
                });
            }
        });
        jB2.addActionListener(e -> {
            UserSession session = UserSession.getInstance();
            String lastBook = session.getLastOpenedBook();
            int lastPage = session.getLastPage();

            if (lastBook == null) {
                JOptionPane.showMessageDialog(frame, "Нет последнего открытого учебника!");
                return;
            }

            openSelectedTextbook(lastBook, backButton);

            if (controller != null && lastPage > 0) {
                try {
                    controller.showPage(lastPage);
                } catch (Exception ex) {
                    System.out.println("Ошибка при переходе на страницу: " + ex.getMessage());
                }
            }
        });
        backButton.addActionListener(e -> {
            saveCurrentProgress();
            bg.removeAll();
            bg.add(topPanel,BorderLayout.NORTH);
            bg.add(centerPanel,BorderLayout.CENTER);
            backButton.setVisible(false);

            bg.revalidate();
            bg.repaint();
        });


        frame.add(bg);
        frame.setVisible(true);
    }
    static JFrame getJFrame(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UserSession session = UserSession.getInstance();
        frame.setSize(session.getWindowWidth(), session.getWindowHeight());

        if (session.isWindowPositionSaved()) {
            frame.setLocation(session.getWindowX(), session.getWindowY());
        } else {
            frame.setLocationRelativeTo(null);
        }

        frame.setTitle("Учебники");
        frame.setLayout(new BorderLayout());
        return frame;
    }
    private void openSelectedTextbook(String bookName, JButton backButton){
        String pdfFile = new File("books", bookName + ".pdf").getAbsolutePath();
        File file = new File(pdfFile);

        if(!file.exists()){
            JOptionPane.showMessageDialog(frame,"Не найден.");
            return;
        }

        try {
            controller = new SwingController();
            SwingViewBuilder factory = new SwingViewBuilder(controller);
            pdfPanel = factory.buildViewerPanel();

            controller.openDocument(pdfFile);

            UserSession session = UserSession.getInstance();
            session.setLastOpenedBook(bookName);

            // Загружаем последнюю страницу из базы
            int lastPage = DataBase.getReadingProgress(session.getUsername(), bookName.hashCode());
            if (lastPage > 0) {
                controller.showPage(lastPage);
            }

            // Добавляем слушатель закрытия окна → сохраняем последнюю страницу
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    saveCurrentProgress();
                }
            });

            bg.remove(centerPanel);
            bg.add(pdfPanel, BorderLayout.CENTER);
            backButton.setVisible(true);

            bg.revalidate();
            bg.repaint();
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                    "Не удалось открыть учебник: " + ex.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void saveCurrentProgress() {
        if (controller != null) {
            UserSession session = UserSession.getInstance();
            String bookName = session.getLastOpenedBook();
            if (bookName != null) {
                int currentPage = controller.getCurrentPageNumber();
                DataBase.saveReadingProgress(session.getUsername(), bookName.hashCode(), currentPage);
                System.out.println("💾 Сохранена страница " + currentPage + " книги " + bookName);
            }
        }
    }
}
