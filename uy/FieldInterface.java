package uy;
import java.awt.Dimension;
import java.awt.Rectangle;

public interface FieldInterface {

    /**
    *
    * @param location_x the horizontal distance of the point where the mouse is released to the border of the screen
    * @param location_y  the vertical distance of the point where the mouse is released to the border of the screen
    * @return returns the pile_panel/suit_pile panel number associated with the location where the mouse is released into
    */
    public int containsCardInChild(int location_x, int location_y);

    public Rectangle getRectangleCovered();

    public String getName();
}
