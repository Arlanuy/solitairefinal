package uy;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SuitPilesAndPilesField extends JPanel {
    private static final int WIDTH  = 1300;
    private static final int HEIGHT = 160;
    private static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);
    private static final int LOCX = 450;
    private static final int LOCY = 150;
    private static final int GAP = 150;
    private static final int FINALGAP = 200;
    private static SuitPilesField suit_piles_field;
    private static PilesField piles_field;
    private BoxLayout layout;

    public SuitPilesAndPilesField() {
        layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(layout);
        setLocation(LOCX, LOCY);
    }

    public void setSuitPilesField(SuitPilesField suit_piles_field) {
        this.suit_piles_field = suit_piles_field;
        add(suit_piles_field);
        add(Box.createRigidArea(new Dimension(WIDTH, GAP)));
    }
    public void setPilesField(PilesField piles_field) {
        this.piles_field = piles_field;
        add(piles_field);
        add(Box.createRigidArea(new Dimension(WIDTH, GAP)));
    }

    public Dimension getPreferredSize() {
        return SIZE;
    }
}
