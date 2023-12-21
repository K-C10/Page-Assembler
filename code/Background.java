import static java.lang.System.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class Background extends JPanel{

    private static JarFile jar;
    private boolean jarisopen = false;
    private String[] elements;
    private boolean[] activatedElements;

    public Background(String[] items) {
        elements = items.clone();
        activatedElements = new boolean[elements.length];
        for(int i = 0; i < activatedElements.length; i++) {
            activatedElements[i] = true;
        }
    }

    public void disableElement(int item_Index) {
        activatedElements[item_Index] = false;
    }

    public void activateElement(int item_Index) {
        activatedElements[item_Index] = true;
    }

    public void update_element(int index, String item) {
        elements[index] = item;
        this.repaint();
    }

    public void updateElements(String[] new_elements) {
        elements = new_elements;
    }

    @Override
    public void paintComponent(Graphics g) { 

        //out.println("!Loading Visual Data ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! !");
        super.paintComponent(g);
        int count = -1;
        Color col;
        g.setColor(new Color(255,255,255,100));
        int x1, y1, x2, y2, size, rotation;
        int[] temp = new int[4];

        for(String item : this.elements) {
            count++;
            if(!activatedElements[count])
                continue;


            switch (Integer.parseInt(item.split("!")[0])) {
                case 1:
                    String Text = item.split("!")[1];
                    col = new Color(Integer.decode(item.split("!")[2]));
                    x1 = Integer.parseInt(item.split("[!,]")[3]); // 1!Database Selection!0x000000!5,20,150,25
                    y1 = Integer.parseInt(item.split("[!,]")[4]);
                    size = Integer.parseInt(item.split("[!,]")[5]);
                    //int textwidth = g.getFontMetrics().stringWidth(Text);
                    //x = x - (textwidth / 2);
        //            out.println("Loading Text [" + Text + "] at coords [" + x + "," + y + "] with size [" + sizex + "," + sizey + "]");
                    g.setFont(new Font("Times New Roman", Font.PLAIN, size));
                    g.setColor( col );
                    g.drawString(Text, x1, y1);
                break;


                case 2:                    
                    x1 = Integer.parseInt(item.split("[!,]")[3]);
                    y1 = Integer.parseInt(item.split("[!,]")[4]);
                    x2 = Integer.parseInt(item.split("[!,]")[5]);
                    y2 = Integer.parseInt(item.split("[!,]")[6]);
                    size = Integer.parseInt(item.split("!")[1]);
                    col = new Color( Integer.decode(item.split("!")[2]) );

    //                out.println("Loading Line [(" + x1 + "," + y1 + "),(" + x2 + "," + y2 + ")] with color " + col.toString() + " to size " + size);

                    
                    g.setColor(col);
                    g.drawLine(x1, y1, x2, y2);
                break;


                case 3:

                break;


                case 4:
                    // 4!ImageNameW/extension!x1,y1,x2,y2
                    
                    String imageName = item.split("!")[1];
                    BufferedImage img;

                        x1 = Integer.parseInt(item.split("[!,]")[2]);
                        y1 = Integer.parseInt(item.split("[!,]")[3]);
                        x2 = Integer.parseInt(item.split("[!,]")[4]);
                        y2 = Integer.parseInt(item.split("[!,]")[5]);
                        rotation = Integer.parseInt(item.split("[!,]")[6]);
                        img = Toolbox.resize(jarFile.getImage(imageName), x2, y2);
                        if(item.contains("{")) {
                            Graphics2D gtemp = img.createGraphics();
                            gtemp.setBackground(new Color(0, true));
                            for(int i = 0; i < item.split(Pattern.quote("{"))[1].split(":").length; i++) {
                                temp = Toolbox.multiparse(item.split(Pattern.quote("{"))[1].split(":")[i].split(","));

                                gtemp.clearRect(temp[0], temp[1], temp[2], temp[3]);
                            }
                            gtemp.dispose();
                        }

                        g.drawImage(Toolbox.rotateImage(img, rotation), (x2 / 2) - x1, (y2 / 2) - y1, null);
                    
                break;
            }

        }
        
    }
}
