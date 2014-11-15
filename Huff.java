
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sam McAnelly
 * 
 */
public class Huff {
    
    private static byte[] inputFileBytes;
    private static byte[] outputFileBytes;
    private static PriorityQueue<HuffmanNode> q;
    private static HashMap<Byte, HuffmanNode> nodeMap;
    private static HuffmanNode[] nodes;
    private static short pad;
    
    public static void main(String[] args){
        
        if (args.length == 0){
            System.out.println("usage: java Huff [inputfile]");
            return;
        } 
        
        readFile(args[0]);
        initiateLeaves();
        loadQueue();
        buildTree();
        encodeBytes();
        writeFile(args[0]);
        
        System.out.println("Success!");
    }
    
    private static void initiateLeaves(){
        nodes = new HuffmanNode[256];
        //Generates a node for every possible byte code value
        for (int i = 0; i < 256; i++){
            nodes[i] = new HuffmanNode();
            nodes[i].setByteCode((byte)(-128 + i));
            nodes[i].setFrequency(0);
        }
    }
    
    private static void loadQueue(){
        Comparator<HuffmanNode> comp = new NodeComparator();
        q = new PriorityQueue<>(256, comp);
        nodeMap = new HashMap<>();
        for (byte b: inputFileBytes){
            nodes[(int)b + 128].incrementFrequency();
        }
        for (HuffmanNode h: nodes){
            if (h.getFrequency() != 0){
                q.add(h);
                nodeMap.put(h.getByteCode(), h);
            }
        }
    }
    
    private static void buildTree(){
        while (q.size() != 1){
            HuffmanNode uno = q.remove();
            HuffmanNode dos = q.remove();
            HuffmanNode parent = new HuffmanNode();
            parent.setZeroChild(uno);
            parent.setOneChild(dos);
            uno.setZeroChildStatus(true);
            dos.setOneChildStatus(true);
            uno.setParent(parent);
            dos.setParent(parent);
            parent.setFrequency(uno.getFrequency() + dos.getFrequency());
            q.add(parent);
        }
    }
    
    private static void encodeBytes(){
        List<Integer> currentEncoding = new ArrayList<>();
        for (byte b: inputFileBytes){
            currentEncoding.addAll(nodeMap.get(b).retrieveLeafEncoding());
        }
        pad = 0;
        if (currentEncoding.size() % 8 != 0) {
            pad = (short)(8 - (currentEncoding.size() % 8));
            for (int i = 0; i < pad; i++)
                currentEncoding.add(0);
        }
        outputFileBytes = Twiddle.bitsToBytes(currentEncoding);
    }
    
    private static void readFile(String fileName){
        File file;
        FileInputStream reader;
        try {
            file = new File(fileName);
            reader = new FileInputStream(file);
            inputFileBytes = new byte[reader.available()];
            reader.read(inputFileBytes);
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    private static void writeFile(String fileName) {
        File file = new File(fileName + ".huff");
        ObjectOutputStream writer;
        try {
            file.createNewFile();
            writer = new ObjectOutputStream(new FileOutputStream(file));
            writer.writeObject(q.peek());
            writer.writeShort(pad);
            for (byte b: outputFileBytes){
                writer.write(b);
            }
            writer.flush();
            writer.close();
            
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(Huff.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
class NodeComparator implements Comparator<HuffmanNode> {

    @Override
    public int compare(HuffmanNode o1, HuffmanNode o2) {
        if (o1.getFrequency() > o2.getFrequency()){
            return 1;
        } else if (o1.getFrequency() < o2.getFrequency()){
            return -1;
        } else {
            return 0;
        }
    }    
}


