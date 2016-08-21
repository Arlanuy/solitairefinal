    package uy;

    import java.io.*;
    import java.awt.*;
    import java.awt.event.*;
    import javax.swing.*;
    import javax.swing.SwingUtilities;
    import javax.swing.filechooser.*;
    import java.nio.file.Path;
    import java.nio.file.Paths;


    public class ManualReader {

        public ManualReader() {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString() + "\\" + "manual" + "\\" + "manualdoc.txt";
            //System.out.println("Current relative path is: " + s);
            File file = new File(s);
            StringBuffer contents = null;
            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                contents = new StringBuffer();
                String line = in.readLine();
                while(line != null){
                  contents.append(line + "\n");
                  System.out.println("new text is " + line);
                  line = in.readLine();
                }
                in.close();
                System.out.println("contents is " + contents);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            JOptionPane option_pane = new JOptionPane();
            JOptionPane.showMessageDialog(null, contents, "Read Manual", JOptionPane.INFORMATION_MESSAGE);
        }
    }
