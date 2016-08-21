package uy;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;
import java.nio.file.Path;
import java.nio.file.Paths;

 /**
 * having the same name, the original code of this class is found in https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html#FileChooserDemo
 */
public class FileChooserDemo extends JPanel {
    static private final String newline = "\n";
    JFileChooser fc;
    private File file;

    public FileChooserDemo() {
        //Create a file chooser
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString() + "\\" + "saved_games";
        //System.out.println("Current relative path is: " + s);
        fc = new JFileChooser(s);
    }

    public String stripExtension (String str) {
        // Handle null case specially.

        if (str == null) return null;

        // Get position of last '.'.

        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.

        if (pos == -1) return str;

        // Otherwise return the string, up to the dot.

        return str.substring(0, pos);
    }

    /**
    *
    * @param command the command to be followed by this class (can only be either load or save)
    * @return the file to be saved or loaded
    */
    public File doCommand(String command) {
        File file = null;
        if (command.equals("load")) {
            int returnVal = fc.showOpenDialog(FileChooserDemo.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
            }
        }

        //Handle save button action.
        else if (command.equals("save")) {
            int returnVal = fc.showSaveDialog(FileChooserDemo.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
            }
        }

        else {
            System.out.println("Just cancelled the operation");
        }
        file = new File(stripExtension(file.getPath()) + ".bin");
        //System.out.println("new path is " + file.getPath());
        return file;
    }
}
