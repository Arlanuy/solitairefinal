package uy;
import java.awt.event.*;
import java.awt.Point;
import java.awt.Container;
import javax.swing.*;

/**
* Implements the adapter needed for a card to move
* @author  Arlan Uy
* @version 1.8.0_60
*/

public class MouseAdaptForCard extends MouseAdapter {
    private boolean pressed_card = false;
    private boolean entered_card = false;
    private CardField card;
    private Container original_parent;
    private int original_x_position;
    private int original_y_position;
    public final static int CONTAINERGAP = 600;
    //for the card to directly be attached to the mouse because the menu bar is part of the whole screen and so its height must be accounted
    public static final int Y_ADJUST = 50;//80
    public static final int X_ADJUST = 100;

    public MouseAdaptForCard(CardField card) {
        card.addMouseListener(this);
        card.addMouseMotionListener(this);
        card.setCardListener(this);
        this.card = card;
    }

    @Override
    // Store the mouse location when it is pressed
    public void mousePressed(MouseEvent event) {
        //System.out.println("Mouse pressed on " + card);
        SolitaireGUI.setSelectedCard(card);
        original_x_position = card.getXOnScreen();
        original_y_position = card.getYOnScreen();
        //System.out.println("CLOSER " + original_x_position + " " + original_y_position + " CLICKED ON " + event.getXOnScreen() + " " + (event.getYOnScreen() - Y_ADJUST - 50));
        SolitaireGUI.createDragPileCards(event.getXOnScreen(), event.getYOnScreen() - Y_ADJUST + 20);
    }


    @Override
    public void mouseReleased(MouseEvent event) {
        //saySomething("Mouse released", event);
        //System.out.println("x " + event.getXOnScreen() + "y " + event.getYOnScreen());
        if (SolitaireGUI.validatePosition(original_x_position, original_y_position, event.getXOnScreen(), event.getYOnScreen() - Y_ADJUST) == true) {
            SolitaireGUI.redisplay();
            //System.out.println("Nailagay na siya");
        }

        else {
            //System.out.println("Naibalik na siya (" + card.getCardLogic() + ") sa" + + original_x_position + " " + original_y_position);
            card.setLocation(original_x_position, original_y_position);
            SolitaireGUI.redisplay();
        }
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        //saySomething("Mouse entered", event);
    }

    @Override
    public void mouseExited(MouseEvent event) {
        //saySomething("Mouse exited", event);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
            card.setLocation(event.getXOnScreen(), event.getYOnScreen() - Y_ADJUST);
            SolitaireGUI.dragPileCards(event.getXOnScreen(), event.getYOnScreen() - Y_ADJUST);
            SolitaireGUI.redisplay();
            //saySomething("Mouse dragged", event);
    }

    void saySomething(String eventDescription, MouseEvent e) {
        System.out.println(eventDescription
                        + " (" + e.getX() + "," + e.getY() + ")"
                        + " detected on "
                        + e.getComponent().getClass().getName()
                        + "\n");
    }
}
