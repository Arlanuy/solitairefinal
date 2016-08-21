package uy;
import java.awt.event.*;
import java.awt.Point;
import java.awt.Container;
import javax.swing.*;

public class MouseAdaptForCardInMainDrawPile extends MouseAdapter {

    public MouseAdaptForCardInMainDrawPile(CardField card) {
        card.addMouseListener(this);
        card.setCardListener(this);
    }
    //Since we are extending from a class and not implementing an
    //interface by which adds the burden of putting all those empty methods in below
    //just to satisfy the requirements of implementing an interface,
    //we can remove the empty methods below for this class and override only methods that we need from this class' superclass
    @Override
    public void mouseClicked(MouseEvent event) {
        SolitaireGUI.addMainToSubDrawPile();
        SolitaireGUI.setSelectedCard(null);
    }


    @Override
    public void mouseReleased(MouseEvent event) {


    }

    @Override
    public void mouseEntered(MouseEvent event) {
        //saySomething("Mouse entered", event);
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    void saySomething(String eventDescription, MouseEvent e) {
        System.out.println(eventDescription
                        + " (" + e.getX() + "," + e.getY() + ")"
                        + " detected on "
                        + e.getComponent().getClass().getName()
                        + "\n");
    }
}
