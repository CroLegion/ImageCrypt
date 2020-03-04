package imageCrypt;


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
      BufferedImage image= null;
      String message="";
      ImageCrypt imageCrypt;
      try{
          File imageFile = new File("G://flag.png");//picture source
          image = ImageIO.read(imageFile);
      }catch(IOException e){
          e.printStackTrace();
      }
      try{ 
          File textFile = new File("G://message.txt");//text source
          Scanner scanner = new Scanner(textFile);
          while(scanner.hasNext()){
              message+=" "+scanner.next();
          }
          scanner.close(); 
          System.out.println(message);
      }catch(InputMismatchException i){
          i.printStackTrace();
      }catch(FileNotFoundException f){
          f.printStackTrace();
      }
      imageCrypt = new ImageCrypt(message, image);
      //try{
          File output = new File("G://encryptedFlag.png");
//          imageCrypt.setEncryptedImage(imageCrypt.encrypt());
//          ImageIO.write(imageCrypt.encrypt(), "png", output);
      //}catch(IOException e){
         // e.printStackTrace();
      //}
  }
  
}
