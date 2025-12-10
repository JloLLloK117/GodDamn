package Anything;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SimpleAutoComplete {

    private static JWindow popupWindow;

    public static void setupAutoComplete(JTextField textField) {
        List<String> usernames = DataBase.getAllUsernames();

        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                togglePopup(textField, usernames);
            }
        });

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_F4) {
                    togglePopup(textField, usernames);
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    closePopup();
                }
            }
        });
    }

    private static void togglePopup(JTextField textField, List<String> usernames) {
        if (popupWindow != null && popupWindow.isVisible()) {
            closePopup();
        } else {
            showPopupMenu(textField, usernames);
        }
    }

    private static void showPopupMenu(JTextField textField, List<String> usernames) {
        if (usernames.isEmpty()) return;

        closePopup();

        JList<String> userList = new JList<>(usernames.toArray(new String[0]));
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setVisibleRowCount(6);

        int preferredHeight = Math.min(usernames.size(), 6) * 25;

        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setPreferredSize(new Dimension(200, preferredHeight));

        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedUser = userList.getSelectedValue();
                if (selectedUser != null) {
                    textField.setText(selectedUser);
                    closePopup();
                }
            }
        });

        popupWindow = new JWindow();
        popupWindow.getContentPane().add(scrollPane);
        popupWindow.pack();

        Point location = textField.getLocationOnScreen();
        popupWindow.setLocation(location.x, location.y + textField.getHeight());
        popupWindow.setVisible(true);

        addOutsideClickListener(textField, popupWindow);
    }

    private static void addOutsideClickListener(JTextField textField, JWindow popup) {
        Container parent = SwingUtilities.getWindowAncestor(textField);
        if (parent instanceof Window window) {

            MouseListener outsideClickListener = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (!popup.getBounds().contains(popup.getLocationOnScreen().x + e.getX(), popup.getLocationOnScreen().y + e.getY())) {
                        closePopup();
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!popup.getBounds().contains(popup.getLocationOnScreen().x + e.getX(), popup.getLocationOnScreen().y + e.getY())) {
                        closePopup();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!popup.getBounds().contains(popup.getLocationOnScreen().x + e.getX(), popup.getLocationOnScreen().y + e.getY())) {
                        closePopup();
                    }
                }
            };

            window.addMouseListener(outsideClickListener);

            final MouseListener finalListener = outsideClickListener;
            popup.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    window.removeMouseListener(finalListener);
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    window.removeMouseListener(finalListener);
                }
            });
        }
    }

    private static void closePopup() {
        if (popupWindow != null) {
            popupWindow.setVisible(false);
            popupWindow.dispose();
            popupWindow = null;
        }
    }
}