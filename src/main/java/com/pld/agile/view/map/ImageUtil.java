package com.pld.agile.view.map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * provides useful functions for display
 * as generating colored marker and warehouse images
 * and generating different colors
 */
public class ImageUtil {

    /** warehouse image */
    private static BufferedImage warehouseImg;

    /** marker image */
    private static BufferedImage markerImg;

    /** predefined colors table for the routes */
    private static final Color[] colors= {Color.blue, Color.CYAN, Color.green, Color.magenta,
    Color.pink,Color.red,Color.yellow};

    /** index of last chosen color from the colors table */
    private static int colorInd=0;

    /**
     * Converts a white image to another color.
     * @param loadImg The white image.
     * @param newColor The new image color.
     * @return A new image with the new color.
     * @author Martin Steiger.
     */
    private static BufferedImage convert(BufferedImage loadImg, Color newColor) {
        int w = loadImg.getWidth();
        int h = loadImg.getHeight();
        BufferedImage imgOut = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imgColor = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = imgColor.createGraphics();
        g.setColor(newColor);
        g.fillRect(0, 0, w+1, h+1);
        g.dispose();

        Graphics2D graphics = imgOut.createGraphics();
        graphics.drawImage(loadImg, 0, 0, null);
        graphics.setComposite(MultiplyComposite.Default);
        graphics.drawImage(imgColor, 0, 0, null);
        graphics.dispose();

        return imgOut;
    }

    /**
     *
     * @param color The color of the image.
     * @return The warehouse image with the defined color.
     */
    public static BufferedImage getWarehouseImage(Color color){
        BufferedImage bfImg=null;
        try{
            String warehouseImgPath = "src/main/java/com/pld/agile/view/map/warehouse.png";
            if(warehouseImg == null) warehouseImg = ImageIO.read(new File(warehouseImgPath));
            bfImg = convert(warehouseImg, color);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bfImg;
    }

    /**
     *
     * @param color The color of the image.
     * @return The marker image with the defined color.
     */
    public static BufferedImage getMarkerImage(Color color){
        BufferedImage bfImg=null;
        try{
            String markerImgPath = "src/main/java/com/pld/agile/view/map/waypoint_white.png";
            if(markerImg == null) markerImg = ImageIO.read(new File(markerImgPath));
            bfImg = convert(markerImg, color);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bfImg;
    }

    /**
     * Chooses a new different color each time (from 7 different colors).
     * @return The chosen color.
     */
    public static Color getColor(){
        Color c = colors[colorInd%colors.length];
        colorInd++;
        return c;
    }

    /**
     *
     */
    public static void restartColorGenerator(){
        colorInd=0;
    }
}
