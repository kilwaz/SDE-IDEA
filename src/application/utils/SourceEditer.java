package application.utils;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SourceEditer extends Canvas {
    private String textToDraw = "greetings, hello this is some text, greetings";
    private HashMap<String, Color> colouredWords = new HashMap<String, Color>();
    private List<TextSection> textSections;
    private GraphicsContext gc;
    private Integer fontSize = 13;
    private Integer padding = 5;
    private Integer lineOffset = 0;
    private Integer characterPadding = 0;
    private Integer textWidth = 7;

    public SourceEditer(double xSize, double ySize) {
        super(xSize, ySize);

        textSections = new ArrayList<TextSection>();
        colouredWords.put("some", Color.RED);
        colouredWords.put("hello", Color.GREEN);
        gc = getGraphicsContext2D();
    }

    public void parseText() {
        textSections = new ArrayList<TextSection>();
        Boolean firstWord = false;
        Boolean isWord = false;

        gc.setFill(Color.BLACK);
        for (int i = 0; i < textToDraw.length(); i++) {
            Character c = textToDraw.charAt(i);

            if (Character.getType(c) == Character.LOWERCASE_LETTER) {
                if (isWord) {

                } else {
                    isWord = true;
                    if (!firstWord) {
                        firstWord = true;
                        gc.setFill(Color.GREEN);
                    }
                }
            } else if (Character.isSpaceChar(c)) {
                gc.setFill(Color.BLACK);
            }

            gc.fillText(c.toString(), padding + characterPadding, padding + fontSize + lineOffset);
            characterPadding += 1 * textWidth;
        }


//
//
//        String[] splitText = textToDraw.split("(?<= )]");
//
//        for (String s : splitText) {
//            if (colouredWords.containsKey(s)) {
//                textSections.add(new TextSection(s, colouredWords.get(s)));
//            } else {
//                if (Character.isUpperCase(s.charAt(0))) {
//                    textSections.add(new TextSection(s, Color.BLACK));
//                } else {
//                    textSections.add(new TextSection(s, Color.BLACK));
//                }
//            }
//        }
    }

    public void draw() {
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());

        characterPadding = 0;
        gc.setFont(new Font("Consolas", fontSize));

        parseText();

        for (TextSection loopTextSection : textSections) {
            gc.setFill(loopTextSection.getTextColour());
            gc.fillText(loopTextSection.getText(), padding + characterPadding, padding + fontSize + lineOffset);
            characterPadding += loopTextSection.getText().length() * textWidth;
        }
    }
}

class TextSection {
    private String text;
    private Color textColour = Color.BLACK;

    public TextSection(String text, Color textColour) {
        this.text = text;
        this.textColour = textColour;
        String variable;
        variable = "d";
    }

    public Color getTextColour() {
        return this.textColour;
    }

    public String getText() {
        return this.text;
    }
}