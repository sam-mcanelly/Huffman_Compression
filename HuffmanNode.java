
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Sam McAnelly
 */
public class HuffmanNode implements Serializable {
    
    private HuffmanNode parent;
    private HuffmanNode zero;
    private HuffmanNode one;
    private List<Integer> code;
    private static String encodedString;
    private int frequency;
    private byte byteCode;
    private boolean isOneChild;
    private boolean isZeroChild;
    
    public HuffmanNode(){
        parent = null;
        zero = null;
        one = null;
        isOneChild = false;
        isZeroChild = false;
        frequency = 0;
    }
    
    //getters
    public HuffmanNode getParent() {
        return parent;
    }
    public HuffmanNode getZeroChild() {
        return zero;
    }
    public HuffmanNode getOneChild() {
        return one;
    }
    public int getFrequency() {
        return frequency;
    }
    public byte getByteCode() {
        return byteCode;
    }
    public boolean isZeroChild() {
        return isZeroChild;
    }
    public boolean isOneChild() {
        return isOneChild;
    }
    
    //setters
    public void setParent(HuffmanNode p){
        parent = p;
    }
    public void setZeroChild(HuffmanNode z){
        zero = z;
    }
    public void setOneChild(HuffmanNode o){
        one = o;
    }
    public void setFrequency(int f){
        frequency = f;
    }
    public void incrementFrequency() {
        frequency++;
    }
    public void setByteCode(byte b){
        byteCode = b;
    }
    public void setOneChildStatus(boolean b){
        isOneChild = b;
    }
    public void setZeroChildStatus(boolean b){
        isZeroChild = b;
    }
    
    public List<Integer> retrieveLeafEncoding(){
        code = new ArrayList<>();
        code = retrieveLeafEncoding(this);
        Collections.reverse(code);
        return code;
    }
    
    public HuffmanNode traverse(int bit){
        switch(bit){
            case 0:
                return zero;
            case 1:
                return one;
        }
        return null;
    }
    
    @SuppressWarnings("empty-statement")
    private List<Integer> retrieveLeafEncoding(HuffmanNode start){
        if (start.isZeroChild()) {
            code.add(0);
            retrieveLeafEncoding(start.getParent());
        }
        else if (start.isOneChild()) {
           code.add(1);
           retrieveLeafEncoding(start.getParent());
        }  
        return code;
    }
    
 
    @Override
    public String toString(){
        return "Byte code: " + byteCode + " ... Frequency: " + frequency;
    }
}
