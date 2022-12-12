package com.pld.agile.view.map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageUtil {

    public static String warehouse = "src/main/java/com/pld/agile/view/map/warehouse2.png";
    public static String marker = "src/main/java/com/pld/agile/view/map/waypoint_white.png";

    public static BufferedImage warehouseImg, markerImg;

    public static Color[] colors= {Color.blue, Color.CYAN, Color.green, Color.magenta,
    Color.pink,Color.red,Color.yellow};
    private static int colorInd=0;

    public  static BufferedImage convert(BufferedImage loadImg, Color newColor) {
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

    public static BufferedImage getWarehouseImage(Color color){
        BufferedImage bfImg=null;
        try{
             if(warehouseImg == null) warehouseImg = ImageIO.read(new File(warehouse));
             bfImg = convert(warehouseImg, color);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bfImg;
    }

    public static BufferedImage getMarkerImage(Color color){
        BufferedImage bfImg=null;
        try{
            if(markerImg == null) markerImg = ImageIO.read(new File(marker));
            bfImg = convert(markerImg, color);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bfImg;
    }

    public static Color getColor(){
        Color c = colors[colorInd%colors.length];
        colorInd++;
        return c;
    }
}
