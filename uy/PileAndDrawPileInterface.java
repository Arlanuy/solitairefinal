package uy;


/**
* Groups the classes, namely DrawPile and Pile classes
* by the characteristics common to both of them
* This interface was not used to correspond to an object (e.g. PileAndDrawPileInterface object)
* But I think creation of this interface , is useful when debugging error common to the pile and  draw pile
 * @author  Arlan Uy
* @version 1.8.0_60
*/
public interface PileAndDrawPileInterface {
    public void setPileNumber(int pile_number);
    public int getPileNumber();
}
