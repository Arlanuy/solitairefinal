package uy;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
* Implements the characteristics of the field area where the whole suit piles field is which represents the object associated with the GUI
* @author  Arlan Uy
* @version 1.8.0_60
*/

public class SuitPilesField extends JPanel implements FieldInterface{
    private static final int WIDTH  = 1320;//SolitaireGUI.frame_width - DrawPileHolderPanel.WIDTH;
    private static final int HEIGHT = 240; //LOCY + CARDHEIGHT + SolitaireGUI.MIDGAP
    public final static int CARDWIDTH = 100;
    private static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);
    private static final int LOCX = 600;
    private static final int LOCY = 75;
    private static final int STARTGAP = 50;
    private static final int GAP = 200;
    private static SuitPile suit_piles[];
    private static SuitPilePanel suit_piles_panel[] = new SuitPilePanel[4];
    private Rectangle rectangle_covered;
    private BoxLayout layout;

    public SuitPilesField() {
        setOpaque(false);
        //setOpaque(true);
        layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
        setLayout(layout);
        setBackground(Color.ORANGE);
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        rectangle_covered = new Rectangle(LOCX, 0, WIDTH, HEIGHT);
    }


    public Rectangle getRectangleCovered() {
        return rectangle_covered;
    }

    /**
    * By using a loop, this method assign each suit_pile to its corresponding suit_piles_panel
    * @param suit_piles  the logic used by the suit_piles represented in an array
     * @param suit_piles_panel  the representation of a suit_pile in the GUI
    */
    public void assignSuitPiles(SuitPile suit_piles[], SuitPilePanel suit_piles_panel[]) {
        for (int i = 0; i < suit_piles.length; i++) {
            suit_piles_panel[i] = new SuitPilePanel();
            suit_piles[i].setSuitPilePanel(suit_piles_panel[i]);
            //System.out.println("Success naman");
        }
        this.suit_piles_panel = suit_piles_panel;
        this.suit_piles = (SuitPile[])suit_piles;
    }


    public void createSuitPilesPanel() {
        add(Box.createRigidArea(new Dimension(STARTGAP, HEIGHT)));
        for (int i = 0; i < suit_piles_panel.length; i++) {
            add(suit_piles_panel[i]);
            add(Box.createRigidArea(new Dimension(GAP, HEIGHT)));
        }
    }

    public void setSuitPilesCards() {
        createSuitPilesPanel();
        for (int i =  0; i < suit_piles_panel.length; i++) {
            suit_piles_panel[i].setSuitPileCard((LOCX + STARTGAP + (GAP + CARDWIDTH) * i), LOCY);
            //System.out.println("Placed suit cards at " + (LOCX + (GAP + CARDWIDTH) * i) + " " + (LOCY));
        }
    }

    public String getName() {
        return "SuitPilesField";
    }

    public int containsCardInChild(int location_x, int location_y) {
        int index_child = -1;
        //System.out.println("Location to be contained is " + location_x + " " + location_y);
        for (int i = 0; i < suit_piles_panel.length; i++) {
            //System.out.println("Suit Piles panel is at X: " + suit_piles_panel[i].getXOnScreen() + " AND AT Y: " + suit_piles_panel[i].getYOnScreen());
            Rectangle rect = suit_piles_panel[i].getRectangleCovered();
            //System.out.println("Rect error i" + i +" at location " + rect.getX() + " " + rect.getY() + " with dimension " + rect.getWidth() + " " + rect.getHeight());
            if (rect.contains(location_x, location_y)) {
                index_child = i;
                break;
            }
        }
        return index_child;
    }

    public void draw(Graphics g) {
        for (int i = 0; i < suit_piles_panel.length; i++) {
            suit_piles_panel[i].draw(g);
        }
    }

    public Dimension getPreferredSize() {
        return SIZE;
    }

}
