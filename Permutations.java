
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sam McAnelly
 * @param <E>
 */
public class Permutations<E> {
    
    private List<List<E>> permutations;
    private boolean next;

    
    public static void main (String[] args){
        List<Integer> test = new ArrayList<>();
        for (int i = 0; i <= 4; i++){
            test.add(i);
        }
        Permutations<Integer> wow = new Permutations<>(test);
        while (wow.hasNext()){
            List<Integer> wow2 = wow.next();
            for (int i: wow2){
                System.out.print(i + ", ");
            }
            System.out.println();
        }
    }
    
    public Permutations(List<E> list) {
        generatePermutation(list);
        next = list.isEmpty();
    }
    
    public boolean hasNext(){
        return next;
    }
    
    public List<E> next() {
        if (permutations.size() == 1) {
            next = false;
        }
        return permutations.remove(0);
    }
    
   private List<List<E>> generatePermutation(List<E> list){
       List<List<E>> toReturn = new ArrayList<>();
        if(list.isEmpty()) {
            next = false;
            List<List<E>> perm = new ArrayList<>();
            perm.add(new ArrayList<E>());
            return perm;
        } else {
            E first = list.get(0);
            list.remove(0);
            permutations = generatePermutation(list);
            for (List<E> s : permutations){
                for (int i = 0; i <= s.size(); i++){
                    List<E> temp = new ArrayList<>(s);
                    temp.add(i, first);
                    toReturn.add(temp);
                }
            }
        }
        permutations = toReturn;
        return toReturn;
    }
}
