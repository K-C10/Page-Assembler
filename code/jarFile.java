import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;

public class jarFile {

    private static HashMap<String, BufferedImage> imageFiles = new HashMap<String, BufferedImage>();
    static HashMap<String, File> textFiles = new HashMap<String, File>();

    static String[] imageExtensions = { "png", "jpeg", "jpg", "bmp" };

    public static void loadJarFile() throws IOException {
        JarFile jar = new JarFile(MainGUI.ProgramName);
        Enumeration<JarEntry> entries = jar.entries();
        JarEntry entry = new JarEntry(entries.nextElement());
        entries = jar.entries();

        while(entries.hasMoreElements()) {
            entry = entries.nextElement();
            for(String ext : imageExtensions)
            {
                try{
                    if(entry.toString().split("[.]")[entry.toString().split("[.]").length - 1].equals(ext)) {
                        imageFiles.put(entry.toString(), ImageIO.read(jar.getInputStream(entry)));
                }
            }
                catch(Exception err) {}
            }
        }

        System.out.println("(jarFile.loadJarFile()) Printing hashmap keys [" + Arrays.toString(imageFiles.entrySet().toArray() ) + "]");

        jar.close();

    }

    public static BufferedImage getImage(String name) {
        if(imageFiles.get(name) == null) {
            return imageFiles.get("error.png");
        }
        return imageFiles.get(name);
    }
}
