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
    private int type;
    public ImageCrypt() {
        this.message = null;
        this.basicImage = null;
    }
    
    /**
     * This is the main constructor for the class. Takes in a message and image.
     * 
     * @param message	The message to be encrypted.
     * @param basicImage	The unencrypted message.
     * @throws StringTooBigException
     */
    public ImageCrypt(String message, BufferedImage basicImage) throws StringTooBigException{
    	type=basicImage.getType();
    	this.basicImage=convert(basicImage);
  		if(message.length()*8 > basicImage.getHeight()* basicImage.getWidth()) {
    		throw new StringTooBigException("The message "+message+" of length "+ message.length()*8 
    				+"is too large for the selected image of area " + basicImage.getWidth()* basicImage.getHeight());
    	}else {
    		this.message = message;
    	} 
       
        this.encryptedImage= convert(basicImage);
    }
    
    /**
     * This is the secondary constructor for decrypting the encrypted image with the original image.
     * 
     * @param message	The message to be encrypted.
     * @param basicImage	The unencrypted message.
     * @throws StringTooBigException
     */
    public ImageCrypt(BufferedImage encryptedImage, BufferedImage basicImage){
        this.basicImage=convert(basicImage);      
        this.encryptedImage =convert(encryptedImage);
        
    }
    
    /*
     * Encrypts the message into the image and creates a new encrypted image.
     */
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

    /*
     * Decrypts the message given the original and encrypted images.
     */
    public void decrypt() throws StringTooBigException {
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
                	if(counter==8 && binary.substring(windowL, windowR).compareTo(new StringBuilder("00000000").toString())==0) {
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
    }

    /*
     * Gets the binary representation of the string.
     */
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
    
    /*
     * Converts the image into a viable image for encryption. 
     */
    private BufferedImage convert(BufferedImage image) {
    	BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        rgbImage.getGraphics().drawImage(image, 0, 0, null);
    	return rgbImage;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) throws StringTooBigException{   	
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
}
