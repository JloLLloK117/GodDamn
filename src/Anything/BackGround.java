package Anything;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class BackGround extends JPanel {
    private final Image image;

    public BackGround(String fileName) throws IOException {
        image = new ImageIcon(fileName).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
