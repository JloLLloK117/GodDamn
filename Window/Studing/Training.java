package Window.Studing;

import Anything.BackGround;
import Anything.UserSession;
import Window.SrcWindow;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Training {

    JFrame frame = getjFrame();
    JPanel panel;
    BackGround bg;
    private JTextPane instructionPane;
    private final JPanel instructionPanel;

    public Training(){

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

        JButton startTraining = new JButton("Начать обучение");
        JButton main = new JButton("Главная");

        String[] choice = {"Начать обучение на С++","Начать обучение на Java"};
        JComboBox<String> list = new JComboBox<>(choice);
        list.setVisible(false);
        list.setPreferredSize(new Dimension(200,30));

        instructionPanel = createInstructionPanel();
        instructionPanel.setVisible(false);

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);

        panel.add(startTraining);
        panel.add(main);
        bg.add(panel, BorderLayout.NORTH);

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comboPanel.setOpaque(false);
        comboPanel.add(list);
        bg.add(comboPanel, BorderLayout.CENTER);

        startTraining.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list.setVisible(true);
                SwingUtilities.invokeLater(() -> {
                    if(list.isShowing()){
                        list.showPopup();
                    }
                });
            }
        });



        list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) list.getSelectedItem();
                if(selected != null){
                    // Скрываем комбобокс и показываем инструкции
                    comboPanel.setVisible(false);
                    bg.add(instructionPanel, BorderLayout.CENTER);
                    instructionPanel.setVisible(true);

                    // Загружаем соответствующие инструкции
                    if(selected.equals("Начать обучение на С++")) {
                        showCppInstructions();
                    } else if(selected.equals("Начать обучение на Java")) {
                        showJavaInstructions();
                    }

                    frame.revalidate();
                    frame.repaint();
                }
            }
        });

        main.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SrcWindow();
                frame.dispose();
            }
        });

        frame.add(bg);
        frame.setVisible(true);

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

        frame.setTitle("Обучение");
        frame.setLayout(new BorderLayout());
        return frame;
    }

    private JPanel createInstructionPanel(){

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JPanel instructionContainer = new JPanel(new BorderLayout());
        instructionContainer.setBorder(BorderFactory.createTitledBorder("Подготовка к обучению"));
        instructionContainer.setBackground(Color.WHITE);

        instructionPane = new JTextPane();
        instructionPane.setEditable(false);
        instructionPane.setBackground(Color.WHITE);
        instructionPane.setFont(new Font("Bahnschrift", Font.BOLD, 25));

        StyledDocument styledDocument = instructionPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        styledDocument.setParagraphAttributes(0, styledDocument.getLength(), center, false);

        JScrollPane scrollPane = new JScrollPane(instructionPane);
        scrollPane.setPreferredSize(new Dimension(600,400));
        instructionContainer.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        JButton backButton =  new JButton("Назад");
        JButton startLearning = new JButton("Начать обучение");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructionPanel.setVisible(false);
                bg.remove(instructionPanel);
                Component[] components = bg.getComponents();
                for(Component component : components){
                    if(component instanceof JPanel){
                        JPanel panel = (JPanel) component;
                        if(panel.getComponentCount()>0 && panel.getComponent(0) instanceof JComboBox){
                            panel.setVisible(true);
                            break;
                        }
                    }
                }
                frame.revalidate();
                frame.repaint();
            }
        });

        startLearning.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "Обучение начинается! 🎉\n\n" +
                                "Следуйте инструкциям и установите необходимые программы.\n" +
                                "После установки возвращайтесь для продолжения обучения.",
                        "Начало обучения",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        buttonPanel.add(startLearning);
        buttonPanel.add(backButton);

        mainPanel.add(instructionContainer, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void showCppInstructions() {
        String instructions =
                "🚀 ПОДГОТОВКА К ИЗУЧЕНИЮ C++ 🚀\n\n" +
                        "Прежде чем начать программировать на C++, вам нужно установить необходимое программное обеспечение:\n\n" +

                        "📋 ЧТО УСТАНОВИТЬ:\n\n" +

                        "1. ⚙️ КОМПИЛЯТОР C++\n" +
                        "   • Для Windows: MinGW-w64 или Visual Studio Community\n" +
                        "   • Для Linux: g++ (через терминал: sudo apt-get install g++)\n" +
                        "   • Для Mac: Xcode Command Line Tools\n\n" +

                        "2. 💻 СРЕДА РАЗРАБОТКИ (IDE)\n" +
                        "   • Visual Studio Code (рекомендуется для начинающих)\n" +
                        "   • Code::Blocks\n" +
                        "   • CLion (платная)\n\n" +

                        "3. 📚 ДОПОЛНИТЕЛЬНЫЕ ИНСТРУМЕНТЫ\n" +
                        "   • Git для контроля версий\n" +
                        "   • CMake для сборки проектов\n\n" +

                        "🔧 НАСТРОЙКА VISUAL STUDIO CODE:\n" +
                        "1. Установите расширение 'C/C++'\n" +
                        "2. Установите расширение 'C/C++ Compile Run'\n" +
                        "3. Настройте путь к компилятору в настройках\n\n" +

                        "✅ ПРОВЕРКА УСТАНОВКИ:\n" +
                        "Создайте файл test.cpp с кодом:\n" +
                        "#include <iostream>\n" +
                        "int main() {\n" +
                        "    std::cout << \"Hello, World!\" << std::endl;\n" +
                        "    return 0;\n" +
                        "}\n\n" +
                        "Запустите в терминале: g++ test.cpp -o test && ./test\n\n" +

                        "🎯 СЛЕДУЮЩИЕ ШАГИ:\n" +
                        "После установки возвращайтесь для изучения:\n" +
                        "• Основы синтаксиса\n" +
                        "• Переменные и типы данных\n" +
                        "• Функции и классы\n" +
                        "• Объектно-ориентированное программирование";

        instructionPane.setText(instructions);
    }

    private void showJavaInstructions() {
        String instructions =
                "🚀 ПОДГОТОВКА К ИЗУЧЕНИЮ JAVA 🚀\n\n" +
                        "Прежде чем начать программировать на Java, вам нужно установить необходимое программное обеспечение:\n\n" +

                        "📋 ЧТО УСТАНОВИТЬ:\n\n" +

                        "1. ⚙️ JAVA DEVELOPMENT KIT (JDK)\n" +
                        "   • Скачайте с официального сайта Oracle\n" +
                        "   • Или используйте OpenJDK\n" +
                        "   • Рекомендуемая версия: JDK 11 или новее\n\n" +

                        "2. 💻 СРЕДА РАЗРАБОТКИ (IDE)\n" +
                        "   • IntelliJ IDEA Community (рекомендуется)\n" +
                        "   • Eclipse\n" +
                        "   • NetBeans\n\n" +

                        "3. 📚 СИСТЕМА СБОРКИ (опционально)\n" +
                        "   • Maven\n" +
                        "   • Gradle\n\n" +

                        "🔧 НАСТРОЙКА INTELLIJ IDEA:\n" +
                        "1. Установите IntelliJ IDEA Community Edition\n" +
                        "2. При первом запуске выберите тему и настройте JDK\n" +
                        "3. Создайте новый Java-проект\n" +
                        "4. Начните с создания класса Main\n\n" +

                        "✅ ПРОВЕРКА УСТАНОВКИ:\n" +
                        "Откройте терминал/командную строку и выполните:\n" +
                        "java -version\n" +
                        "javac -version\n\n" +

                        "Если видите версию Java - установка прошла успешно!\n\n" +

                        "🎯 ПЕРВАЯ ПРОГРАММА:\n" +
                        "Создайте файл Main.java:\n" +
                        "public class Main {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        System.out.println(\"Hello, World!\");\n" +
                        "    }\n" +
                        "}\n\n" +
                        "Скомпилируйте: javac Main.java\n" +
                        "Запустите: java Main\n\n" +

                        "🎯 СЛЕДУЮЩИЕ ШАГИ:\n" +
                        "После установки возвращайтесь для изучения:\n" +
                        "• Основы синтаксиса Java\n" +
                        "• Классы и объекты\n" +
                        "• Наследование и полиморфизм\n" +
                        "• Коллекции и потоки ввода-вывода";

        instructionPane.setText(instructions);
    }

}
