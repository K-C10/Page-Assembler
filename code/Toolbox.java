import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Toolbox {
    public static String[] addarrindex(String[] input) {
        String[] output = new String[input.length + 1];
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i];
        }
        return output;
    }

    public static int addString(String input) {
        int output = 0;
        for (int i = 0; i < input.length(); i++) {
            output += input.charAt(i);
        }
        return output;
    }

    public static int[] multiparse(String[] in) {
        int[] output = new int[in.length];
        for(int i = 0; i < in.length; i++) {
            output[i] = Integer.parseInt(in[i]);
        }
        return output;
    }

    public static String[] resizeArray(String[] input, int desiredLength) {
        String[] output = new String[desiredLength];
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i];
        }
        return output;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_AREA_AVERAGING);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static int booltoint(boolean in) {
        return in ? 1 : 0; // Lazy thing, just so that the code can be read a little bit
    }

    public static String arrtostr(String[] arr) {
        String output = "";
        for (String item : arr) {
            output += item + ",";
        }
        if (output.length() != 0)
            output = output.substring(0, output.length() - 1);
        return output;
    }

    public static BufferedImage createRotated(BufferedImage image) {
        AffineTransform at = AffineTransform.getRotateInstance(
                Math.PI, image.getWidth() / 2, image.getHeight() / 2.0);
        return createTransformed(image, at);
    }

    private static BufferedImage createTransformed(
            BufferedImage image, AffineTransform at) {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    public static BufferedImage rotateImage(BufferedImage image, int degree) {
        final double rads = Math.toRadians(degree);
        final double sin = Math.abs(Math.sin(rads));
        final double cos = Math.abs(Math.cos(rads));
        final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
        final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        final AffineTransform at = new AffineTransform();
        at.translate(w / 2, h / 2);
        at.rotate(rads,0, 0);
        at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(image,rotatedImage);

        return rotatedImage;
    }
}
