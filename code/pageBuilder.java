import static java.lang.System.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class pageBuilder {
    // w = 3440,h = 2496
    public static void buildPages() throws IOException {
        BufferedImage output = new BufferedImage(3440, 2496, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < Database.getLastIndex() + 1; i += 2) {
            BufferedImage page1, page2;
            page1 = buildPagefromDatabase(i);
            page2 = buildPagefromDatabase(i + 1);
            Graphics pa = output.getGraphics();

            pa.drawImage(page1, 0, 0, null); // here we are putting page 1 on the left, then we put page 2 rotated 180
                                             // degrees to the right, every image is writted to a graphics object based
                                             // on the top left corner
            pa.drawImage(Toolbox.createRotated(page2), 1720, 0, null);

            pa.setFont(new Font("Times New Roman", Font.PLAIN, 100)); // here we draw the line in the middle so that it
                                                                      // is easy to see where to cut the paper
            pa.setColor(new Color(0x000000));
            pa.drawLine(output.getWidth() / 2, 0, output.getWidth() / 2, output.getHeight());

            ImageIO.write(output, "png", new File("Page" + ((i / 2) + 1) + ".png"));
            pa.dispose();
        }

    }

    public static BufferedImage buildPagefromDatabase(int dbindex) throws IOException {

        final String[] extensions = { "png", "jpeg", "jpg", "bmp" };
        String[] dataorder = { "Work: ", "Work Number: ", "Cell: ", "E-Mail: ", "BirthDay: " };

        BufferedImage page = new BufferedImage(1720, 2496, BufferedImage.TYPE_INT_RGB);

        Graphics g = page.createGraphics();
        try {
            out.println("(pageBuilder.buildPagefromDatabase) Writing data [" + Database.getEntry(dbindex).gitInfo()
                    + "] to page");
        } catch (NullPointerException err) {
            out.println("(pageBuilder.buildPagefromDatabase) Error! Database is not loaded!");
            BufferedImage erroroutput = new BufferedImage(1720, 2496, BufferedImage.TYPE_INT_RGB);
            Graphics temp = erroroutput.createGraphics();

            String[] error = { "Database Is Not Loaded", "Select Database With the Database Button" };

            temp.setFont(new Font("Times New Roman", Font.BOLD, 64));
            for (int i = 0; i < error.length; i++)
                temp.drawString(error[i], 50, 75 + (i * 75));

            return erroroutput;
        } catch (ArrayIndexOutOfBoundsException err) {
            out.println("(pageBuilder.buildPagefromDatabase) Error! Database is empty!");
            BufferedImage erroroutput = new BufferedImage(1720, 2496, BufferedImage.TYPE_INT_RGB);

            Graphics temp = erroroutput.createGraphics();

            String[] error = { "Database Is empty", "Add an entry with the add page button" };

            temp.setColor(new Color(0xFFFFFF));
            temp.fillRect(0, 0, erroroutput.getWidth(), erroroutput.getHeight());

            temp.setColor(new Color(0xDC143C));
            temp.setFont(new Font("Times New Roman", Font.BOLD, 64));
            for (int i = 0; i < error.length; i++)
                temp.drawString(error[i], 50, 75 + (i * 75));

            return erroroutput;
        }

        g.fillRect(0, 0, page.getWidth(), page.getHeight());

        g.setColor(new Color(0x251607));
        g.setFont(new Font("Times New Roman", Font.BOLD, 96));

        int x, y;

        int textwidth = g.getFontMetrics().stringWidth(Database.getEntry(dbindex).getKey());

        x = (page.getWidth() / 2) - (textwidth / 2);
        y = page.getHeight() / 12;

        g.drawString(Database.getEntry(dbindex).getKey(), x, y);

        BufferedImage picture;

        for (String ext : extensions) {
            
            try {
                picture = Toolbox.resize(
                        ImageIO.read(new File(MainGUI.picturefolderLocation + MainGUI.file_Directory_Character
                                + Database.getEntry(dbindex).getKey().toLowerCase() + "." + ext)),
                        (page.getWidth() / 4 + 430), (page.getHeight() / 8 + 430));
                g.drawImage(picture, 430, 312, null);
                out.println("(pageBuilder.buildpagefromDatabase) added page from [" + MainGUI.picturefolderLocation + MainGUI.file_Directory_Character
                                + Database.getEntry(dbindex).getKey().toLowerCase() + "." + ext + "]");
            } catch (IOException err) {
                continue;
            }
        }

        g.setFont(new Font("Times New Roman", Font.PLAIN, 48));

        textwidth = g.getFontMetrics().stringWidth(Database.getEntry(dbindex).getaddress());
        x = (page.getWidth() / 2) - (textwidth / 2);
        y = (page.getHeight() / 32) * 14 + 25;
        g.drawString(Database.getEntry(dbindex).getaddress(), x, y);

        textwidth = g.getFontMetrics().stringWidth(Database.getEntry(dbindex).getGeneralLocation());
        x = (page.getWidth() / 2) - (textwidth / 2);
        y += 50;
        g.drawString(Database.getEntry(dbindex).getGeneralLocation(), x, y);

        textwidth = g.getFontMetrics().stringWidth(Database.getEntry(dbindex).getHomePhoneNumber());
        x = (page.getWidth() / 2) - (textwidth / 2);
        y += 50;
        g.drawString(Database.getEntry(dbindex).getHomePhoneNumber(), x, y);

        out.println("(pageBuilder.buildPagefromDatabase) " + Database.getEntry(dbindex).getFamilyType() + " - "
                + Database.getEntry(dbindex).getKey());
        String[] personData;
        switch (Database.getEntry(dbindex).getFamilyType()) {

            case 2: // single no children
                x = page.getWidth() / 4;
                y = (page.getHeight() / 12) * 7;

                personData = Database.getEntry(dbindex).getFatherData();

                g.setFont(new Font("Times new Roman", Font.BOLD, 48));

                try {
                    g.drawString(personData[0], x, y);
                } catch (NullPointerException err) {
                } // drawing name
                y += 100;

                g.setFont(new Font("Times new Roman", Font.PLAIN, 40));

                for (int i = 1; i < personData.length; i++) {
                    g.drawString(dataorder[i - 1] + personData[i], x, y);
                    y += 100;
                }

                break;

            case 3: // single with children
                x = page.getWidth() / 4;
                y = (page.getHeight() / 12) * 7;
                personData = Database.getEntry(dbindex).getFatherData();

                g.setFont(new Font("Times new Roman", Font.BOLD, 48));

                g.drawString(personData[0], x, y);
                y += 100;

                g.setFont(new Font("Times new Roman", Font.PLAIN, 40));

                for (int i = 1; i < personData.length; i++) {
                    g.drawString(dataorder[i - 1] + personData[i], x, y);
                    y += 100;
                }

                x = page.getWidth() / 2 - 150;
                y += 150;
                g.setFont(new Font("Times new Roman", Font.BOLD, 48));
                g.drawString("Children", x, y);
                g.setFont(new Font("Times new Roman", Font.PLAIN, 40));

                for (String item : Database.getEntry(dbindex).getChildren()) {
                    y += 50;
                    g.drawString(item, x, y);
                }

                break;

            case 4: // couple no children

                x = (page.getWidth() / 8);
                y = (page.getHeight() / 12) * 7;
                personData = Database.getEntry(dbindex).getFatherData();

                g.setFont(new Font("Times new Roman", Font.BOLD, 48));

                g.drawString(personData[0], x, y);
                y += 100;

                g.setFont(new Font("Times new Roman", Font.PLAIN, 40));

                for (int i = 1; i < personData.length; i++) {
                    g.drawString(dataorder[i - 1] + personData[i], x, y);
                    y += 75;
                }

                x = (page.getWidth() / 8) * 5;
                y = (page.getHeight() / 12) * 7;
                personData = Database.getEntry(dbindex).getMotherData();

                g.setFont(new Font("Times new Roman", Font.BOLD, 48));

                g.drawString(personData[0], x, y);
                y += 100;

                g.setFont(new Font("Times new Roman", Font.PLAIN, 40));

                for (int i = 1; i < personData.length; i++) {
                    g.drawString(dataorder[i - 1] + personData[i], x, y);
                    y += 75;
                }

                textwidth = g.getFontMetrics()
                        .stringWidth("Anniversary : " + Database.getEntry(dbindex).getAnniversary());
                x = (page.getWidth() / 2) - (textwidth / 2);
                g.drawString("Anniversary : " + Database.getEntry(dbindex).getAnniversary(), x, y);

                break;

            case 5: // couple with children
                x = (page.getWidth() / 8);
                y = (page.getHeight() / 12) * 7;
                personData = Database.getEntry(dbindex).getFatherData();

                g.setFont(new Font("Times new Roman", Font.BOLD, 48));

                g.drawString(personData[0], x, y);
                y += 100;

                g.setFont(new Font("Times new Roman", Font.PLAIN, 40));

                for (int i = 1; i < personData.length; i++) {
                    g.drawString(dataorder[i - 1] + personData[i], x, y);
                    y += 75;
                }

                x = (page.getWidth() / 8) * 5;
                y = (page.getHeight() / 12) * 7;
                personData = Database.getEntry(dbindex).getMotherData();

                g.setFont(new Font("Times new Roman", Font.BOLD, 48));

                g.drawString(personData[0], x, y);
                y += 100;

                g.setFont(new Font("Times new Roman", Font.PLAIN, 40));

                for (int i = 1; i < personData.length; i++) {
                    g.drawString(dataorder[i - 1] + personData[i], x, y);
                    y += 75;
                }

                textwidth = g.getFontMetrics()
                        .stringWidth("Anniversary : " + Database.getEntry(dbindex).getAnniversary());
                x = (page.getWidth() / 2) - (textwidth / 2);
                g.drawString("Anniversary : " + Database.getEntry(dbindex).getAnniversary(), x, y);

                x = page.getWidth() / 2 - 150;
                y += 150;
                g.setFont(new Font("Times new Roman", Font.BOLD, 48));
                g.drawString("Children", x, y);
                g.setFont(new Font("Times new Roman", Font.PLAIN, 40));

                for (String item : Database.getEntry(dbindex).getChildren()) {
                    y += 50;
                    g.drawString(item, x, y);
                }
                break;
        }
        /*
         * } catch(NullPointerException err) {
         * out.println("Error! Database is not loaded!");
         * BufferedImage erroroutput = new BufferedImage(1720, 2496,
         * BufferedImage.TYPE_INT_RGB);
         * Graphics temp = erroroutput.createGraphics();
         * 
         * String[] error = {"Database Is Not Loaded",
         * "Select Database With the Database Button"};
         * 
         * 
         * temp.setFont(new Font("Times New Roman", Font.BOLD, 64));
         * for(int i = 0; i < error.length; i++)
         * temp.drawString(error[i], 50, 75 + (i * 75));
         * 
         * return erroroutput;
         * } catch (ArrayIndexOutOfBoundsException err) {
         * out.println("Error! Database is empty!");
         * BufferedImage erroroutput = new BufferedImage(1720, 2496,
         * BufferedImage.TYPE_INT_RGB);
         * 
         * Graphics temp = erroroutput.createGraphics();
         * 
         * String[] error = {"Database Is empty",
         * "Add an entry with the add page button"};
         * 
         * 
         * temp.setColor(new Color(0xFFFFFF));
         * temp.fillRect(0,0,erroroutput.getWidth(),erroroutput.getHeight());
         * 
         * 
         * temp.setColor(new Color(0xDC143C));
         * temp.setFont(new Font("Times New Roman", Font.BOLD, 64));
         * for(int i = 0; i < error.length; i++)
         * temp.drawString(error[i], 50, 75 + (i * 75));
         * 
         * return erroroutput;
         * }
         */

        return page;
    }
}