
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sam McAnelly
 */
public class Puff {
    
    private static byte[] inputFileBytes;
    private static short pad;
    private static HuffmanNode root;
    
    public static void main(String[] args){
        if (args.length == 0){
            System.out.println("usage: java Puff [inputfile]");
            return;
        }
        readFile(args[0]);
        decompress(args[0]);
        System.out.println("Success!");
    }
    
    private static void readFile(String fileName){
        File file;
        ObjectInputStream reader;
        try {
            file = new File(fileName);
            reader = new ObjectInputStream(new FileInputStream(file));
            root = (HuffmanNode)reader.readObject();
            pad = reader.readShort();
            inputFileBytes = new byte[reader.available()];
            reader.read(inputFileBytes);
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Puff.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void decompress(String fileName){
        if (inputFileBytes.length == 0)
            return;
        HuffmanNode trace = root;
        List<Integer> input = Twiddle.bytesToBits(inputFileBytes);
        for (int i = 1; i <= pad; i++){
            input.remove(input.size() - 1);
        }
        String file = "puff_" + fileName.replace(".huff", "");
        File output;
        FileOutputStream out;
        try {
            output = new File(file);
            out = new FileOutputStream(output);
            while (!input.isEmpty()) {
                if (trace.traverse(input.get(0)) == null){
                    out.write(trace.getByteCode());
                    trace = root;
                } else if (trace.traverse(input.get(0)) != null) {
                    trace = trace.traverse(input.remove(0));
                }
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(Puff.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
