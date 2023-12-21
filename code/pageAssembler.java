import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class pageAssembler extends JPanel implements ActionListener{

    private static int[] pagebounds = {80, 15, 688, 499};

    private String[] VisualData = {"4!Background.png!391,282,783,564,0","4!ForeGround.png!391,282,783,564,0!{" + (pagebounds[0] - 6) + "," + (pagebounds[1] - 6) + "," + (pagebounds[2] + 12) + "," + (pagebounds[3] + 12), "1!Build Pages!0x00A8F3!0,15,12","1!1!0xFF0000!252,12,16", "1!2!0xFF0000!596,12,16", "1!Mirror!0x00A8F3!0,70,12","1!Flip Pages!0x00A8F3!0,135,12"};
    

    JFrame window;
    JPanel panel;
    JLabel displayPage = new JLabel();
    JButton[] buttons = new JButton[4];
    Background bk = new Background(VisualData);
    int page1Index = 0, page2Index = 1;

    boolean mirrored = false, page1Faceup = true, page2Faceup = false;

    

    int[][] buttonBounds = {{15,15,50,50}, {15,72,50,50}, {20,139,40,50}, {20,196 ,40,50}};
    String[] buttonIcons = {"hammer and anvil icon.png", "reflect.png", "rotate1.png", "rotate2.png"};

    /* buttons 
     * 0 = make page 
     * 1 = double up pages
     * 2 = flip page half 1470018
     * 3 = flip page half 2
     * width = 3440 
     * length = 2496
     */

    pageAssembler() {
        bk.setLayout(null);
        window = new JFrame("Page Assembler");
        window.setIconImage(jarFile.getImage("assembly.png"));
        window.setBounds(0, 0, 783, 564);
        window.setResizable(false);
        
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(new ImageIcon(Toolbox.resize(jarFile.getImage(buttonIcons[i]), buttonBounds[i][2], buttonBounds[i][3])));
            buttons[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            buttons[i].setBounds(buttonBounds[i][0], buttonBounds[i][1], buttonBounds[i][2], buttonBounds[i][3]);
            buttons[i].addActionListener(this);
            buttons[i].setOpaque(true);
            buttons[i].setContentAreaFilled(false);
            buttons[i].setBorderPainted(false);
            bk.add(buttons[i]);
        }
        displayPage.setBounds(pagebounds[0], pagebounds[1], pagebounds[2], pagebounds[3]);
        this.buildPage(page1Index, page2Index);
        bk.add(displayPage);
        bk.setOpaque(true);
        window.add(bk);

        bk.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent action) {
                
                System.out.println("Mouse Click at (" + action.getX() + ", " + action.getY() + ") in page Assembler");
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
        window.setVisible(true);


    }

    private BufferedImage buildPage(int page1, int page2) {
        BufferedImage page = new BufferedImage(3440, 2496, BufferedImage.TYPE_INT_RGB);
        Graphics gpage = page.getGraphics();
        try {
            if(page1Faceup)
                gpage.drawImage(pageBuilder.buildPagefromDatabase(page1), 0, 0, null);
            else
                gpage.drawImage(Toolbox.createRotated(pageBuilder.buildPagefromDatabase(page1)), 0, 0, null);
            
            if(page2Faceup)
                gpage.drawImage(pageBuilder.buildPagefromDatabase(page2), 1720, 0, null);
            else
                gpage.drawImage(Toolbox.createRotated(pageBuilder.buildPagefromDatabase(page2)), 1720, 0, null);
            
            
            gpage.setColor(Color.BLACK);
            gpage.drawLine(page.getWidth() / 2, 0, page.getWidth() / 2, page.getHeight());

            displayPage.setIcon(new ImageIcon(Toolbox.resize(page, (3440 / 5), (2496 / 5))));

        } catch (IOException err) {}

        return page;
    }

    @Override
    public void actionPerformed(ActionEvent act) {
        if(act.getSource() == buttons[0]) {
            buildPages();
        } else if(act.getSource() == buttons[1]) {
            mirrored = !mirrored;
            if(!mirrored)
                page2Index = page1Index + 1;
            
            if(mirrored)
                page2Index = page1Index;
            
        } else if(act.getSource() == buttons[2]) {
            page1Faceup = !page1Faceup;
        } else if(act.getSource() == buttons[3]) {
            page2Faceup = !page2Faceup;
        }
        buildPage(page1Index, page2Index);
    }

    private void buildPages() {
        try {
            if(!mirrored)
                for(int index = 0; index < Database.databaseLength; index+=2) {
                    ImageIO.write(buildPage(index, index + 1), "png", new File("Page" + ((index / 2) + 1) + ".png"));
                }
            
            if(mirrored)
                for(int index = 0; index < Database.databaseLength; index++) {
                    ImageIO.write(buildPage(index, index), "png", new File("Page" + (index + 1) + ".png"));
                }
         } catch (IOException err) {

            
        }
        page1Index = 0;
        if(mirrored)
            page2Index = 0;
        else
            page2Index = 1;
    }
}