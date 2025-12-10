package Window.Studing;

import Anything.BackGround;
import Anything.DataBase;
import Anything.UserSession;
import Window.SrcWindow;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.List;

public class Exercise {

    JFrame frame = getFrame();
    BackGround bg;

    private JTextPane questionPane;
    private JPanel answersPanel;
    private JButton nextButton;
    private JButton prevButton;
    private JLabel resultLabel;

    private final JPanel exercisePanel;

    private List<TestQuestion> currentQuestions;

    private int currentQuestionIndex = 0;
    private int score = 0;
    private ButtonGroup answerGroup;

    private int currentExerciseId;
    private String currentLanguage;

    JButton jB3;

    private final JPanel comboPanel;

    public Exercise() {
        try {
            bg = new BackGround("Images/Gray_fon.png");
            bg.setLayout(new BorderLayout());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Инициализация двух наборов вопросов

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);

        exercisePanel = createExercisePanel();
        exercisePanel.setVisible(false);

        JButton jB1 = new JButton("Тесты");
        jB1.setOpaque(false);
        jB1.setFocusable(false);
        JButton jB2 = new JButton("Главная");
        jB2.setOpaque(false);
        jB2.setFocusable(false);
        jB3 = new JButton("Назад");
        jB3.setOpaque(false);
        jB3.setFocusable(false);
        jB3.setVisible(false);

        String[] tests = {"Тесты по C++", "Тесты по Java"};
        JComboBox<String> list = new JComboBox<>(tests);
        list.setVisible(false);
        list.setPreferredSize(new Dimension(200, 30));

        comboPanel = new JPanel();
        comboPanel.setOpaque(false);
        comboPanel.add(list);

        bg.add(comboPanel, BorderLayout.CENTER);

        jB1.addActionListener(e -> {
            list.setVisible(true);
            SwingUtilities.invokeLater(() -> {
                if (list.isShowing()) {
                    list.showPopup();
                }
            });
        });

        list.addActionListener(e -> {
            String selected = (String) list.getSelectedItem();
            if (selected != null) {
                comboPanel.setVisible(false);
                bg.add(exercisePanel, BorderLayout.CENTER);
                exercisePanel.setVisible(true);
                showExerciseList(selected);
                jB3.setVisible(true);
                frame.revalidate();
                frame.repaint();
            }
        });

        jB2.addActionListener(e -> {
            new SrcWindow();
            frame.dispose();
        });

        jB3.addActionListener(e -> {
            resetTest();
            exercisePanel.setVisible(false);
            bg.remove(exercisePanel);
            comboPanel.setVisible(true);
            list.setVisible(true);
            jB3.setVisible(false);

            bg.revalidate();
            bg.repaint();
        });

        panel.add(jB1);
        panel.add(jB2);
        panel.add(jB3);

        bg.add(panel, BorderLayout.NORTH);
        frame.add(bg);
        frame.setVisible(true);
    }

    // ----------------- СОЗДАНИЕ ПАНЕЛИ ТЕСТА -----------------
    private JPanel createExercisePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createTitledBorder("Вопрос"));
        questionPanel.setBackground(Color.WHITE);
        questionPane = new JTextPane();
        questionPane.setEditable(false);
        questionPane.setBackground(Color.WHITE);
        questionPane.setFont(new Font("Bahnschrift", Font.BOLD, 20));

        questionPanel.add(new JScrollPane(questionPane), BorderLayout.CENTER);

        JPanel answersMainPanel = new JPanel(new BorderLayout());
        answersMainPanel.setBorder(BorderFactory.createTitledBorder("Варианты ответов"));
        answersMainPanel.setBackground(Color.WHITE);

        answersPanel = new JPanel();
        answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));
        answersPanel.setBackground(Color.WHITE);
        answerGroup = new ButtonGroup();

        answersMainPanel.add(new JScrollPane(answersPanel), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setOpaque(false);

        JPanel navPanel = new JPanel(new FlowLayout());
        navPanel.setOpaque(false);

        prevButton = new JButton("Предыдущий");
        nextButton = new JButton("Следующий");
        JButton submitButton = new JButton("Завершить тест");

        navPanel.add(prevButton);
        navPanel.add(nextButton);
        navPanel.add(submitButton);

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Bahnschrift", Font.BOLD, 16));

        controlPanel.add(navPanel, BorderLayout.NORTH);
        controlPanel.add(resultLabel, BorderLayout.CENTER);

        prevButton.addActionListener(e -> showPreviousQuestion());
        nextButton.addActionListener(e -> showNextQuestion());
        submitButton.addActionListener(e -> {
            showResults();
            jB3.setVisible(false);
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, questionPanel, answersMainPanel);
        splitPane.setResizeWeight(0.3);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void startTest(String testType) {
        resetTest();

        String lang = testType.equals("Тесты по C++") ? "cpp" : "java";
        currentLanguage = lang;

        // Загружаем вопросы
        SwingUtilities.invokeLater(() -> {
            loadExerciseQuestions(lang, currentExerciseId);

            if (!currentQuestions.isEmpty()) {
                currentQuestionIndex = 0;
                updateQuestion();
                updateNavigationButtons();
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Не удалось загрузить упражнение " + currentExerciseId + " для языка " + lang,
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void resetTest() {
        currentQuestionIndex = 0;
        score = 0;
        answerGroup = new ButtonGroup();
        if (answersPanel != null) {
            answersPanel.removeAll();
        }
    }

    private void updateQuestion() {
        if (currentQuestions == null || currentQuestions.isEmpty()) return;

        SwingUtilities.invokeLater(() -> {
            TestQuestion currentQuestion = currentQuestions.get(currentQuestionIndex);

            try {
                StyledDocument doc = questionPane.getStyledDocument();
                doc.remove(0, doc.getLength());

                Style headingStyle = questionPane.addStyle("Heading", null);
                StyleConstants.setForeground(headingStyle, Color.BLUE);
                StyleConstants.setFontSize(headingStyle, 18);
                StyleConstants.setBold(headingStyle, true);

                doc.insertString(doc.getLength(), "Вопрос " + (currentQuestionIndex + 1) + "\n\n", headingStyle);
                doc.insertString(doc.getLength(), currentQuestion.question, null);

            } catch (BadLocationException e) {
                e.printStackTrace();
            }

            // Обновление панели с ответами
            answersPanel.removeAll();
            answerGroup = new ButtonGroup();

            for (int i = 0; i < currentQuestion.answers.size(); i++) {
                JRadioButton rb = new JRadioButton(currentQuestion.answers.get(i));
                rb.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
                rb.setBackground(Color.WHITE);
                rb.setActionCommand(String.valueOf(i));
                answerGroup.add(rb);
                answersPanel.add(rb);
            }

            resultLabel.setText("Вопрос " + (currentQuestionIndex + 1) + " из " + currentQuestions.size());

            answersPanel.invalidate();
            answersPanel.validate();
            answersPanel.repaint();
            questionPane.repaint();
        });
    }


    private void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            updateQuestion();
            updateNavigationButtons();
        }
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < currentQuestions.size() - 1) {
            checkAnswer();
            currentQuestionIndex++;
            updateQuestion();
            updateNavigationButtons();
        }
    }

    private void updateNavigationButtons() {
        prevButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setEnabled(currentQuestionIndex < currentQuestions.size() - 1);
    }

    private void checkAnswer() {
        ButtonModel selectedModel = answerGroup.getSelection();
        if (selectedModel != null) {
            int selectedAnswer = Integer.parseInt(selectedModel.getActionCommand());
            if (selectedAnswer == currentQuestions.get(currentQuestionIndex).correctAnswer) {
                score++;
            }
        }
    }

    private void showResults() {
        checkAnswer();

        double percentage = (double) score / currentQuestions.size() * 100;
        String message = String.format(
                "Тест завершен!\n\nПравильных ответов: %d из %d\nРезультат: %.1f%%\n\n%s",
                score, currentQuestions.size(), percentage,
                percentage >= 70 ? "Отлично! 🎉" : "Попробуйте еще раз! 💪"
        );

        JOptionPane.showMessageDialog(frame, message, "Результаты теста", JOptionPane.INFORMATION_MESSAGE);

        String username = UserSession.getInstance().getUsername();
        DataBase.saveExerciseResult(username, currentLanguage, currentExerciseId);

        String options[] = {"Повторить тест","Следующее упражнение","Назад"};
        int choice = JOptionPane.showOptionDialog(frame,
                "Что хотите сделать дальше?",
                "Завершение теста",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch(choice) {
            case 0:
                startTest(currentLanguage.equals("cpp")?"Тесты по C++":"Тесты по Java");
                break;
            case 1:
                int nextId = currentExerciseId + 1;
                if((currentLanguage.equals("cpp")&&nextId<=50)||(currentLanguage.equals("java")&&nextId<=50)) {
                    currentExerciseId = nextId;
                    startTest(currentLanguage.equals("cpp")?"Тесты по C++":"Тесты по Java");
                }else{
                    JOptionPane.showMessageDialog(frame,"Это последнее упражнение.", "Информация", JOptionPane.INFORMATION_MESSAGE);
                    resetTest();
                    exercisePanel.setVisible(false);
                    showMainMenu();
                }
                break;
            case 2:
            default:
                exercisePanel.setVisible(false);
                showMainMenu();
                break;
        }

    }

    private void showMainMenu() {

        for(Component component : bg.getComponents()){
            if(component instanceof JPanel panel && panel.getComponentCount()>0){
                Component first = panel.getComponent(0);
                if(first instanceof JComboBox) {
                    panel.setVisible(true);
                    jB3.setVisible(false);
                    break;
                }
            }
        }

        frame.revalidate();
        frame.repaint();
    }

    static JFrame getFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UserSession session = UserSession.getInstance();
        frame.setSize(session.getWindowWidth(), session.getWindowHeight());

        if (session.isWindowPositionSaved()) {
            frame.setLocation(session.getWindowX(), session.getWindowY());
        } else {
            frame.setLocationRelativeTo(null);
        }

        frame.setTitle("Тесты по программированию");
        return frame;
    }

    private void showExerciseList(String testType) {
        JDialog dialogFrame = new JDialog(frame, "Выбор упражнения", true);
        dialogFrame.setSize(350, 600);
        dialogFrame.setLocationRelativeTo(frame);
        dialogFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);


        // Панель для кнопок с вертикальным расположением
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Создаём 50 кнопок
        for (int i = 1; i <= 50; i++) {
            final int exerciseId = i; // необходимо для лямбды
            JButton button = new JButton("Упражнение " + i);
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setMaximumSize(new Dimension(300, 30));
            button.addActionListener(e -> {
                checkAndStartExercise(testType, exerciseId, dialogFrame, jB3);
            });
            buttonPanel.add(button);
            buttonPanel.add(Box.createVerticalStrut(5)); // небольшой отступ
        }

        // Оборачиваем в JScrollPane для прокрутки
        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        dialogFrame.add(scrollPane);
        dialogFrame.setVisible(true);
    }

    private void checkAndStartExercise(String testType, int exerciseId, JDialog dialog, JButton backButton) {
        String lang = testType.equals("Тесты по C++") ? "cpp" : "java";
        String username = UserSession.getInstance().getUsername();

        if (DataBase.isExerciseCompleted(username, lang, exerciseId)) {
            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Это упражнение уже пройдено.\nХотите пройти его ещё раз?",
                    "Упражнение пройдено",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                dialog.dispose();
                exercisePanel.setVisible(false);
                showMainMenu();
                return;
            }
        }

        dialog.dispose();
        currentExerciseId = exerciseId;

        // Загружаем вопросы и запускаем тест на EDT
        SwingUtilities.invokeLater(() -> startTest(testType));
    }


    private void loadExerciseQuestions(String lang, int exerciseId) {
        currentQuestions = ExerciseLoader.load(lang, exerciseId);
        if (currentQuestions.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Не удалось загрузить упражнение " + exerciseId + " для языка " + lang,
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
