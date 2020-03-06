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

/**
*
* @author jstil
*/
public class Main {
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
      BufferedImage normImage = null;
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
    	  messageString.deleteCharAt(messageString.length()-1);
      } catch (Exception e) {
    	  
      }
      ImageCrypt imageCrypt = new ImageCrypt(messageString.toString(), normImage);
      imageCrypt.encrypt();
      String out= imageCrypt.decrypt();
      System.out.println(out);
  }
  
}
