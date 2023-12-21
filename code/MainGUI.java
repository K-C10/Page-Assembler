import static java.lang.System.*;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.io.*;

public class MainGUI extends JPanel implements ActionListener, WindowListener {

    final static int[] pagebounds = {200,25,430,624};

    JFrame jf = new JFrame("DPB");
    Background p;
    JLabel page;
    Image img;
    JButton[] button = new JButton[7];
    String[] ButtonLabels = { "Select DirFile", "Define Pic Folder", "Edit Page", "Add Page", "Back", "Forward", "Build Pages" };
    int[][] ButtonSizes = { { 150, 50 }, { 150, 50 }, { 72, 50 }, { 72, 50 }, { 72, 50 }, { 72, 50 }, { 150, 50 } }; // spacing
                                                                                                                     // is
                                                                                                                     // 6,
                                                                                                                     // spacing
                                                                                                                     // between
                                                                                                                     // groups
                                                                                                                     // is
                                                                                                                     // 30
    static String file_Directory_Character;

    int[][] ButtonCoords = { { 25, 25 }, { 25, 81 }, { 25, 160 }, { 103, 160 }, { 25, 217 }, { 103, 217 },
            { 25, 297 } };
    String[] ButtonIcons = { "Directory icon.png", "picture icon.png", "edit icon.png", "add icon.png",
            "Left Arrow.png", "Right Arrow.png", "conveyor.png" };

static String[] VisualData = {"4!Background.png!327,350,654,700,0","4!ForeGround.png!327,350,654,700,0!{0,0,1,1:", "1!Database Selection!0x00A8F3!5,20,12", "1!Page Tools!0x00A8F3!5,155,12", "1!Build Pages!0x00A8F3!10,292,12",/* "1!Database File Not Chosen!0xFF0000!15,380,12" */}; // 1 = Visual Label, 2 = line, 3 = rectangle, 4 = Image
    static boolean[] ActiveVisualData = {true, true, true, true};

    /* 1 format -> 1!Text!xloc,yloc,xsize,ysize - 1!example!0,0,10,10 - 1!String!int,int,int,int
     * 2 format -> 2!LineThickness!Hexcolor!x1,y1,x2,y2 - 2!25!0xFFFFFF!0,0,10,10 - 2!int!Color!int,int,int,int
     * 3 format -> 3!HexColor!FilledIn=1transparent=0border=1!x1,y1,x2,y2 - 3!0xFFFFFF!0!0,0,10,10 - 3!Color!Boolean!int,int,int,int
     * 4 format -> 4!ImageNameW/extension!x1,y1,width,height,rotation - 4!dot.png!5,5,10,10 - 4!Image!int,int,int,int !NOTICE! Image must be compiled with jar file to be
     * found, and name must be correct!
     */

    static int currentindex = 0;
    public static String DatabaseFileLocation, picturefolderLocation,
            ProgramName = new java.io.File(MainGUI.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                    .getName();
    static MainGUI g;
    Graphics2D background;

    public static void main(String args[]) throws IOException {
        out.println("I am [" + ProgramName + "]");
        jarFile.loadJarFile();
        out.println("(MainGUI.main) loading Main Program");


        out.println("(MainGUI.main) ------\n| [" + Database.Primarydivider + "] = primaryDivider \n| ["
                + Database.ParentDivider + "] = parentDivier \n| [" + Database.ParentDataDivider
                + "] = ParentDataDivier");

        out.println("Operating System [" + System.getProperty("os.name") + "]");
        out.println("---------------------");
        
        file_Directory_Character = System.getProperty("file.separator");
        
        MainGUI.readConfig();
        out.println("(MainGUI.main) Database Loader returned " + Database.loadDatabase(DatabaseFileLocation));
        Database.printDatabase();

        g = new MainGUI();
    }

    MainGUI() throws IOException {
       
        

        jf.setSize(654, 700); // WINDOW SIZE

        p = new Background(VisualData);
        
        p.setLayout(null);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.addWindowListener(this);
        jf.setResizable(false);

        int count = 0;

        out.println("(MainGUI.init) Loading Buttons");

        for(int i = 0; i < ButtonLabels.length; i++) { // BUTTONS
            ImageIcon icon = new ImageIcon();

            button[count] = new JButton();
            button[count].setBounds(ButtonCoords[count][0], ButtonCoords[count][1], ButtonSizes[count][0], ButtonSizes[count][1]);
            button[count].addActionListener(this);
            button[count].setOpaque(true);
            button[count].setContentAreaFilled(false);
            button[count].setBorderPainted(false);
            button[count].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            p.add(button[count]);

            icon = new ImageIcon(Toolbox.resize(jarFile.getImage(ButtonIcons[count]), ButtonSizes[count][0], ButtonSizes[count][1]));

            button[count].setIcon(icon);

            out.println("[ " + (ButtonIcons[count].equals(ButtonIcons[count]) ? "ok" : "err") + " ] Loading "
                    + ButtonIcons[count]);

            count++;
        }
        out.println("(MainGUI.init) Buttons Loaded! ");

        p.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent action) {
                // TODO Auto-generated method stub
                
                out.println("Mouse Click at (" + action.getX() + ", " + action.getY() + ")");
    
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
               
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
              
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
            
            }
            
        });



    //    background = (Graphics2D) new BufferedImage(jf.getWidth(), jf.getHeight(), BufferedImage.TYPE_INT_ARGB).getGraphics();

        // put the image here
        page = new JLabel();
        p.add(page);
        VisualData[1] += (pagebounds[0] - 6) + "," + (pagebounds[1] - 6) + "," + (pagebounds[2] + 12) + "," + (pagebounds[3] + 12) + ":";
        p.updateElements(VisualData);
        // try{updatePage();}catch(IOException err) {out.println("Error with updating
        // page!");}

        // jar.close();

        try {
            this.updatePage();
        } catch (IOException err) {
            out.println("(MainGUI.init) Error with updating page!");
        }

        jf.setIconImage(jarFile.getImage("assembly.png"));

        jf.add(p);
        out.println(p.getComponentCount());
        // new pageAssembler(); // TODO: remove this line when not develping
        jf.setVisible(true);
        jf.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent act) { // Start Button Magic
        if (act.getSource() == button[0]) {
            try {
                JFileChooser path = new JFileChooser("C:\\");
                path.showOpenDialog(button[0]);
                out.println("(MainGUI.actionPerformed) database file -> " + path.getSelectedFile());
                if (path.getSelectedFile() != null) {
                    DatabaseFileLocation = path.getSelectedFile().toString();
                    Database.loadDatabase(DatabaseFileLocation);
                }
                try {
                    this.updatePage();
                } catch (IOException err) {
                    out.println("(MainGUI.actionPerformed) Error with updating page!");
                }
            } catch (IOException err) {
                out.println("(MainGUI.actionPerformed) IOEXCEPTION in button[0]!, File Not Readable/exists!");
            }

        } else if (act.getSource() == button[1]) {
            // define pic folder here
            JFileChooser parentpic = new JFileChooser("C:\\");
            parentpic.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            parentpic.showOpenDialog(button[1]);
            out.println("(MainGUI.actionPerformed) picture folder -> " + parentpic.getSelectedFile());
            if (parentpic.getSelectedFile() != null) {
                picturefolderLocation = parentpic.getSelectedFile().toString();
            }
            try {
                this.updatePage();
            } catch (IOException err) {
                out.println("(MainGUI.actionPerformed) Error with updating page!");
            }
        } else if (act.getSource() == button[2]) {
            // edit current page here
            new PageEditGui(currentindex, g);


        } else if (act.getSource() == button[3]) {
            // add a page
            new PageEditGui(-1, g);


        } else if (act.getSource() == button[4]) {
            // go back a index
            if (currentindex > 0) {
                currentindex--;
                try {
                    this.updatePage();
                } catch (IOException err) {
                    out.println("(MainGUI.cationPerformed) Error with updating page!");
                }
            }
        } else if (act.getSource() == button[5]) {
            // go forward an index
            if (currentindex < Database.databaseLength - 1) {
                currentindex++;
                try {
                    this.updatePage();
                } catch (IOException err) {
                    out.println("(MainGUI.actionPerformed) Error with updating page!");
                }
            }
        } else if (act.getSource() == button[6]) {
            // build the pages
            new pageAssembler();
        }
    } // end Button magic

    public void updatePage() throws IOException {
        page.setIcon(new ImageIcon(Toolbox.resize(pageBuilder.buildPagefromDatabase(currentindex), pagebounds[2], pagebounds[3])));
        page.setBounds(pagebounds[0], pagebounds[1], pagebounds[2], pagebounds[3]);
    }

    public static void setindex(int in) {
        currentindex = in;
    }

    public static boolean readConfig() {
        try {
            Scanner file = new Scanner(new File("Config.dat"));

            DatabaseFileLocation = file.nextLine();
            picturefolderLocation = file.nextLine();

            file.close();
            return true;
        } catch (IOException err) {
            return false;
        } catch (NoSuchElementException err) {
            return false;
        }
    }

    public static boolean saveConfig() {
        try {
            FileWriter config = new FileWriter(new File("Config.dat"));

            config.write(DatabaseFileLocation + "\n");
            config.write(picturefolderLocation + "\n");

            config.close();
            return true;
        } catch (IOException err) {
            return false;
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub
        out.println("(MainGUI.windowOpened)");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        // TODO Auto-generated method stub
        saveConfig();
        Database.saveDatabase();
        out.println("(MainGUI.windowClosing) Database and configs saved!");
        exit(0);

    }

    @Override
    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub
        out.println("(MainGUI.windowsClosed)");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub
        out.println("(MainGUI.windowIconified)");

    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub
        out.println("(MainGUI.windowDeiconified)");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub
        out.println("(MainGUI.windowActivated)");

    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub
        out.println("(MainGUI.windowDeactivated)");

    }
}