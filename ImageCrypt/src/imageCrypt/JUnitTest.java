package imageCrypt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import imageCrypt.ImageCrypt.StringTooBigException;

import org.junit.BeforeClass;

public class JUnitTest {
	static BufferedImage normImage= null;
	String message="this is a message for testing";
	String CorrectMessage="01110100"+"0110100001101001011100110010000001101001"
	     		+ "01110011001000000110000100100000011011010110010101110011"
	     		+ "01110011011000010110011101100101001000000110011001101111"
	     		+ "011100100010000001110100011001010111001101110100011010010110111001100111";
	@BeforeClass
	public static void init() {
	     try{
	         File imageFile = new File("G://flag.png");//picture source
	         normImage = ImageIO.read(imageFile);
	         BufferedImage rgbImage = new BufferedImage(normImage.getWidth(), normImage.getHeight(), BufferedImage.TYPE_INT_RGB);
	         rgbImage.getGraphics().drawImage(normImage, 0, 0, null);
	         normImage=rgbImage;
	     }catch(IOException e){
	         e.printStackTrace();
	     }
	     
	}
	@Test
	public void checkBinary() throws StringTooBigException {
		ImageCrypt imageCrypt = new ImageCrypt(message, normImage);
		assertEquals(CorrectMessage, imageCrypt.getBinary());
		
	}
	@Test
	public void checkBinaryLength() throws StringTooBigException {
		ImageCrypt imageCrypt = new ImageCrypt(message, normImage);
		System.out.println(CorrectMessage.length());
		
		assertEquals(CorrectMessage, imageCrypt.getBinary());
		
	}
	@Test
	public void checkEncrypt() {
		
	}
	@Test //TODO
	public void checkString() throws StringTooBigException {
		ImageCrypt imageCrypt = new ImageCrypt(message, normImage);
		imageCrypt.encrypt();
		imageCrypt.decrypt();
		String out= imageCrypt.getMessage();
		System.out.println(out);
		assertEquals(message+" and "+ out, message, out);		
	}
	
}