
import java.util.Arrays;
import java.util.LinkedList;


public class Main {
    public static void main(String[] args) {

        TripletDeque arr = new TripletDeque();

        arr.addLast("d");
        arr.addLast("a");
        arr.addLast("a");
        arr.addLast("a");




        System.out.println(Arrays.deepToString(arr.getContainerByIndex(0)));
        //System.out.println(Arrays.deepToString(arr.getContainerByIndex(1)));

    }

}
