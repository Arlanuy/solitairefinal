package uy;

/**
* implements the characteristics common to both suit piled and draw pile
* The grouped_piles object in SolitaireDemo class that implements this interface
* This interface was not used to be instantiated as an object
* but I think still doing this , is useful when debugging errors common to the suit pile and  draw pile
* @author  Arlan Uy
* @version 1.8.0_60
*/
public interface SuitPileAndPileInterface{
    public boolean checkPush(Card x, Card temp);
}
