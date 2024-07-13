import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.util.zip.InflaterInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

// I hope this works
public class Main {
  public static void main(String[] args){
    final String command = args[0];
    
    switch (command) {
       case "init" -> {
         final File root = new File(".git");
         new File(root, "objects").mkdirs();
         new File(root, "refs").mkdirs();
         final File head = new File(root, "HEAD");
    
         try {
           head.createNewFile();
           Files.write(head.toPath(), "ref: refs/heads/main\n".getBytes());
           System.out.println("Initialized git directory");
         } catch (IOException e) {
           throw new RuntimeException(e);
         }
       }
       case "cat-file" -> {
        /*
        steps:
        1. get path of object
        2. decompress file
        3. put into inputBufferStream
        4. Print
        5. CLOSE inflater and io reader
        **/

        // check to make sure an object was provided
        if (args[2] == null){
          System.out.println("Enter object to read");
          break;
        }
        
        // Get path and name of object
        String hash = args[2];
        String hash_dir = args[2].substring(0,2);
        String hash_name = args[2].substring(2);

        
        try{
          // create input stream of file
          FileInputStream input = new FileInputStream("./.git/objects/"+ hash_dir + "/" + hash_name);
          InflaterInputStream inflater = new InflaterInputStream(input);
          // reads line into Buffered reader
          BufferedReader reader = new BufferedReader(new InputStreamReader(inflater));

          // read all bytes to object and parse the content into it's own variable
          String object = reader.readLine();
          String content = object.substring(object.indexOf("\0")+ 1);
          System.out.print(content);  // Prints object's content

          // close io classes and break
          input.close();
          inflater.close();
          reader.close();
        } catch(IOException e){
          throw new RuntimeException(e);
        } //catch(FileNotFoundException ex){
          //throw new RuntimeException(ex);
        //}
        System.out.println();
      }


       default -> System.out.println("Unknown command: " + command);
     }
  }
}
