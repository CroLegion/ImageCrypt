package imageCrypt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

/**
 *
 * @author Jacob Stilwell
 */
public class ImageCrypt {

    private String message;
    private BufferedImage basicImage;
    private BufferedImage encryptedImage;
    private final int MAX = 255;
    private final int MIN = 0;

    public ImageCrypt() {
        this.message = null;
        this.basicImage = null;
    }

    public ImageCrypt(String message, BufferedImage basicImage) {
        this.message = message;
        this.basicImage = basicImage;
        this.encryptedImage= basicImage;
    }
    public ImageCrypt(BufferedImage encryptedImage, BufferedImage basicImage) {
        this.encryptedImage = encryptedImage;
        this.basicImage = basicImage;
        this.message=null;
    }
    public void encrypt() {
        String binary = getBinary();
        int binLocation = 0;
        boolean finished=false;
        System.out.println(basicImage.getHeight()+" "+basicImage.getWidth());
        for (int i = 0; i < basicImage.getHeight() && !finished; i++) {
            for (int j = 0; j < basicImage.getWidth(); j++) {
                Color pixel = new Color(basicImage.getRGB(i, j));
                if (binary.charAt(binLocation) == '1') {
                    if (pixel.getBlue() < MAX) {
                        pixel = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue() + 1);
                    } else {
                        pixel = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue() - 1);
                    }
                    //System.out.println(i+" "+j);
                    encryptedImage.setRGB(i, j, pixel.getRGB());
                }
                binLocation++;
                if(binLocation==binary.length()-1){
                    finished=true;
                    break;
                }
            }
        }
        //return encryptedImage;
    }

    public String decrypt() {//TODO
        StringBuilder binary = new StringBuilder();
        boolean finished=false;
        int counter=0;
        Color encryptedPixel;
        Color basicPixel;
        for (int i = 0; i < encryptedImage.getHeight() && !finished; i++) {
            for (int j = 0; j < encryptedImage.getWidth() && !finished; j++) {
                encryptedPixel = new Color(encryptedImage.getRGB(i, j));
                basicPixel = new Color(basicImage.getRGB(i, j));
                if(encryptedPixel.getBlue()!=basicPixel.getBlue())binary.append('1');
                else binary.append('0');
                counter++;
                if(counter==8 && binary.compareTo(new StringBuilder("00000000"))==0) {
                	finished=true;
                }else counter=0;
            }
        }
        return Integer.toString(Integer.parseInt(binary.toString(), 10));
    }

    public String getBinary() {
        byte[] bytes = message.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BufferedImage getBasicImage() {
        return basicImage;
    }

    public void setBasicImage(BufferedImage basicImage) {
        this.basicImage = basicImage;
    }

    public BufferedImage getEncryptedImage() {
        return encryptedImage;
    }

    public void setEncryptedImage(BufferedImage encryptedImage) {
        this.encryptedImage = encryptedImage;
    }
}
