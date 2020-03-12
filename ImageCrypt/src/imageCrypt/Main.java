package imageCrypt;


import java.awt.Color;
//import com.mycompany.imagecrypt.ImageCrypt;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.imageio.ImageIO;

import imageCrypt.ImageCrypt.StringTooBigException;

/**
*
* @author jstil
*/
public class Main {
  /**
   * @param args the command line arguments
 * @throws StringTooBigException 
   */
  public static void main(String[] args) throws StringTooBigException {
      BufferedImage normImage = null;
      BufferedImage encImage =null;
      StringBuilder messageString=new StringBuilder();
      try {
    	  File imageFile = new File("G:/flag.png");
    	  normImage = ImageIO.read(imageFile);
      } catch(IOException e){
    	  e.printStackTrace();
      }
      try {
    	  File file = new File("G:/message.txt");
    	  Scanner scanner = new Scanner(file);
    	  while (scanner.hasNext()) {
    		  messageString.append(scanner.next()).append(" ");
    	  }
    	  messageString.toString().trim();
    	  scanner.close();
    	  messageString.deleteCharAt(messageString.length()-1);
      } catch (Exception e) {
    	  e.printStackTrace();
      }
      ImageCrypt imageCrypt = new ImageCrypt(messageString.toString(), normImage);
      imageCrypt.encrypt();
      try {
    	  File imageFile = new File("G:/encrypted.png");
    	  ImageIO.write(imageCrypt.getEncryptedImage(), "png", imageFile);
      } catch(IOException e){
    	  e.printStackTrace();
      }
      try {
    	  File imageFile = new File("G:/encrypted.png");
    	  encImage = ImageIO.read(imageFile);
      } catch(IOException e){
    	  e.printStackTrace();
      }
      ImageCrypt imageCrypt2 = new ImageCrypt(encImage, normImage);
      imageCrypt2.decrypt();
      String out= imageCrypt2.getMessage();
      System.out.println(out+" " + normImage.getWidth()*normImage.getHeight());
  }
}