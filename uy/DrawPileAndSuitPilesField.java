package uy;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
* Implements the characteristics common to both the draw pile and suit piles
* @author  Arlan Uy
* @version 1.8.0_60
*/

public class DrawPileAndSuitPilesField extends JPanel {
    private static final int WIDTH  = 1920; //SolitaireGUI.frame_width; this is commented out because the output of this variable is inconsistent and may sometimes produce the value of 0 mainly because of, I think, threads issues
    public static final int HEIGHT = 240;  //(SuitPilesField.LOCY or DrawPileHolderPanel.LOCY) + CARDHEIGHT + SolitaireGUI.MIDGAP
    private static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);
    private static final int FINALGAP = 150;
    private static final int GAP = 0;
    private static SuitPilesField suit_piles_field;
    private static DrawPileHolderPanel draw_pile_holder_panel;
    private BoxLayout layout;

    public DrawPileAndSuitPilesField() {
        setOpaque(false);
        layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
        //setOpaque(true);
        setLayout(layout);
        setBackground(Color.BLUE);
    }

    public void setDrawPileHolderPanel(DrawPileHolderPanel draw_pile_holder_panel) {
        this.draw_pile_holder_panel = draw_pile_holder_panel;
        add(draw_pile_holder_panel);
        add(Box.createRigidArea(new Dimension(GAP, HEIGHT)));
    }


    public void setSuitPilesField(SuitPilesField suit_piles_field) {
        this.suit_piles_field = suit_piles_field;
        add(suit_piles_field);
        add(Box.createRigidArea(new Dimension(FINALGAP, HEIGHT)));
    }

    public Dimension getPreferredSize() {
        return SIZE;
    }

    public void draw(Graphics g) {
        suit_piles_field.draw(g);
        draw_pile_holder_panel.draw(g);
    }

}
