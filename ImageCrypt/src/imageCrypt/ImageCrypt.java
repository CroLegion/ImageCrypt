package imageCrypt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Scanner;

import org.hamcrest.core.Is;

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

    public ImageCrypt(String message, BufferedImage basicImage) throws StringTooBigException{
    	this.basicImage=convert(basicImage);  	
  		if(message.length()*8 > basicImage.getHeight()* basicImage.getWidth()) {
    		throw new StringTooBigException("The message "+message+" of length "+ message.length()*8 
    				+"is too large for the selected image of area " + basicImage.getWidth()* basicImage.getHeight());
    	}
        this.message = message;
        
        this.encryptedImage= convert(new BufferedImage(this.basicImage.getWidth(), this.basicImage.getHeight(), this.basicImage.getType()));
        		//deepCopy(basicImage);
    }
    public ImageCrypt(BufferedImage encryptedImage, BufferedImage basicImage) throws StringTooBigException{
    	BufferedImage rgbImage = new BufferedImage(basicImage.getWidth(), basicImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        rgbImage.getGraphics().drawImage(basicImage, 0, 0, null);
        this.basicImage=rgbImage;
        rgbImage = new BufferedImage(encryptedImage.getWidth(), encryptedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        rgbImage.getGraphics().drawImage(encryptedImage, 0, 0, null);
        if(message.length()*8 > basicImage.getHeight()* basicImage.getWidth()) {
    		throw new StringTooBigException("The message "+message+" of length "+ message.length()*8 
    				+"is too large for the selected image of area " + basicImage.getWidth()* basicImage.getHeight());
    	}
        this.encryptedImage =rgbImage;
        this.message=null;
    }
    public void encrypt() {
        String binary = getBinary();
        int binLocation = 0;
        boolean finished=false;
        for (int y = 0; y < basicImage.getHeight() && !finished; y++) {
            for (int x = 0; x < basicImage.getWidth(); x++) {
                Color pixel = new Color(basicImage.getRGB(x, y));
                if (binary.charAt(binLocation) == '1') {
                    if (pixel.getBlue() < MAX) {
                        pixel = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue() + 1);
                    } else {
                        pixel = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue() - 1);
                    }
                    //System.out.println(x);
                }
                encryptedImage.setRGB(x, y, pixel.getRGB());
                binLocation++;
                
                System.out.println(binary.length()+" "+binLocation);
                if(binLocation==binary.length()){
                    finished=true;
                    break;
                }
            }
        }
        //return encryptedImage;
    }

    public String decrypt() throws StringTooBigException, NoImageException {
        StringBuilder binary = new StringBuilder();
        StringBuilder output = new StringBuilder();
        int windowL=0;
        int windowR=8;
        boolean finished=false;
        int counter=0;
        Color encryptedPixel;
        Color basicPixel;
        for (int y = 0; y < encryptedImage.getHeight() && !finished; y++) {
            for (int x = 0; x < encryptedImage.getWidth() && !finished; x++) {
                encryptedPixel = new Color(encryptedImage.getRGB(x, y));
                basicPixel = new Color(basicImage.getRGB(x, y));
                if(encryptedPixel.getBlue()!=basicPixel.getBlue())binary.append('1');
                else binary.append('0');
                counter++;
                if(binary.length()>=8) {
                	if(counter==8 && binary.substring(windowL, windowR).compareTo(new StringBuilder("11111111").toString())==0) {
                    	finished=true;
                    }else if(counter==8) {
                    	output.append((char)Integer.parseInt(binary.substring(windowL, windowR), 2));
                    	windowL+=8;
                    	windowR+=8;
                    	counter=0;
                    }else {
                    }
                }
            }
        }
        setMessage(output.toString());
        return output.toString();
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

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    private BufferedImage convert(BufferedImage image) {
    	BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        rgbImage.getGraphics().drawImage(image, 0, 0, null);
    	return rgbImage;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) throws StringTooBigException, NoImageException{
    	if(getBasicImage()==null) {
    		throw new NoImageException("No image is found.");
    	}
    	if(message.length()*8 > basicImage.getHeight()* basicImage.getWidth()) {
    		throw new StringTooBigException("The message "+message+" of length "+ message.length()*8 
    				+"is too large for the selected image of area " + basicImage.getWidth()* basicImage.getHeight());
    	}
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
    
    
    public class StringTooBigException extends Exception{

    	public StringTooBigException(String errorMessage){
			super(errorMessage);
		}
    }
    public class NoImageException extends Exception{

    	public NoImageException(String errorMessage){
			super(errorMessage);
		}
    }
}
