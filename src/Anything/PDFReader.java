package Anything;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import javax.swing.*;
import java.awt.*;

public class PDFReader {

    public static JPanel getPDFReader() {
        SwingController swingController = new SwingController();
        SwingViewBuilder swingViewBuilder = new SwingViewBuilder(swingController);

        return swingViewBuilder.buildViewerPanel();
    }
}
