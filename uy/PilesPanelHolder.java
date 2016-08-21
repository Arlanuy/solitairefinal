package uy;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PilesPanelHolder extends JPanel {
    private static final int WIDTH  = 1920;//SolitaireGUI.frame_width;
    //BOTTOMMARGIN = 230;
    private static final int HEIGHT = 560;//SolitaireGUI.frame_height - 150 - DrawPileAndSuitPilesField.HEIGHT;
    public static final int CONTAINERGAP = 550;
    private static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);
    private static final int LOCX = 0;
    private static final int LOCY = 370;
    private static PilesField piles_field;
    private BoxLayout layout;

    public PilesPanelHolder() {
        layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
        setLayout(layout);
        setOpaque(false);
        //setOpaque(true);
        setBackground(Color.BLACK);
        setBounds(LOCX, LOCY, WIDTH, HEIGHT);
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
    }

    public void setPilesField(PilesField piles_field) {
        this.piles_field = piles_field;
        add(Box.createRigidArea(new Dimension(CONTAINERGAP, HEIGHT)));
        add(piles_field);
    }

    public Dimension getPreferredSize() {
        return SIZE;
    }
}
