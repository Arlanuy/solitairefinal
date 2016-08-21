package uy;
import java.io.*;

/**
* Handles the occurence of popping a card from  an empty pile
* @author  Arlan Uy
* @version 1.8.0_60
*/
public class StackUnderFlowException extends Exception {
    public StackUnderFlowException(LinkedStack x) {
        System.out.println("StackUnderFlowException");
        System.out.print("The pile has the kind " + x.getKind());
        if  (x.getKind().equals("Pile"))
             System.out.println(" and its pile number is " + ((Pile)x).getPileNumber());
        if    (x.getKind().equals("Draw Pile")) {
            System.out.println(" and its pile number is " + ((DrawPile)x).getPileNumber());
        }

        else if (x.getKind().equals("Suit Pile")) {
            System.out.println(" and its pile suit is " + ((SuitPile)x).getSuitPile());
        }

    }
}
