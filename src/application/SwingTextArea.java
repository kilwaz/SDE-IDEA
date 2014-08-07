package application;

import javafx.embed.swing.SwingNode;
import org.fife.ui.rsyntaxtextarea.CodeTemplateManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.templates.CodeTemplate;
import org.fife.ui.rsyntaxtextarea.templates.StaticCodeTemplate;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SwingTextArea extends SwingNode implements Runnable {
    private RSyntaxTextArea sourceCodeTextArea;
    private DocumentListener sourceCodeTextAreaListener;
    private Source source = null;

    public SwingTextArea() {
    }

    public void setEnabled(Boolean flag) {
        if (sourceCodeTextArea != null) {
            sourceCodeTextArea.setEnabled(flag);
        }
    }

    public void setSource(Source source) {
        this.source = source;

        if (sourceCodeTextArea != null) {
            sourceCodeTextArea.getDocument().removeDocumentListener(sourceCodeTextAreaListener);
            sourceCodeTextAreaListener = null;

            if (source.getSource() != null) {
                sourceCodeTextArea.setText(source.getSource());
            }

            // Reactivates Listener
            sourceCodeTextAreaListener = new SourceCodeTextAreaDocumentListener();
            sourceCodeTextArea.getDocument().addDocumentListener(sourceCodeTextAreaListener);
        }
    }

    @Override
    public void run() {
        JPanel cp = new JPanel(new java.awt.BorderLayout());

        sourceCodeTextArea = new RSyntaxTextArea(18, 90);
        sourceCodeTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        sourceCodeTextArea.setCodeFoldingEnabled(true);
        sourceCodeTextArea.setAntiAliasingEnabled(true);
        sourceCodeTextArea.setEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(sourceCodeTextArea);
        sp.setFoldIndicatorEnabled(true);
        cp.add(sp);

        if (source != null) {
            sourceCodeTextArea.setText(source.getSource());
        }

        sourceCodeTextAreaListener = new SourceCodeTextAreaDocumentListener();
        sourceCodeTextArea.getDocument().addDocumentListener(sourceCodeTextAreaListener);

        // Whether templates are enabled is a global property affecting all
        // RSyntaxTextAreas, so this method is static.
        RSyntaxTextArea.setTemplatesEnabled(true);

        // Code templates are shared among all RSyntaxTextAreas. You add and
        // remove templates through the shared CodeTemplateManager instance.
        CodeTemplateManager ctm = RSyntaxTextArea.getCodeTemplateManager();

        // StaticCodeTemplates are templates that insert static text before and
        // after the current caret position. This template is basically shorthand
        // for "System.out.println(".
        CodeTemplate ct = new StaticCodeTemplate("sout", "System.out.println(", null);
        ctm.addTemplate(ct);

        // This template is for a for-loop. The caret is placed at the upper
        // bound of the loop.
        ct = new StaticCodeTemplate("fb", "for (int i=0; i<", "; i++) {\n\t\n}\n");
        ctm.addTemplate(ct);

        this.setContent(cp);
    }

    class SourceCodeTextAreaMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("It was clicked");
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    class SourceCodeTextAreaDocumentListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            updateSource();
        }

        public void removeUpdate(DocumentEvent e) {
            updateSource();
        }

        public void changedUpdate(DocumentEvent e) {
            //Plain text components do not fire these events
        }

        private void updateSource() {
            Program program = DataBank.currentlyEditProgram;
            if (program != null) {
                source.setSource(sourceCodeTextArea.getText());
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        buttonRunProgram.setDisable(true);
//                    }
//                });
            }
        }
    }
}
