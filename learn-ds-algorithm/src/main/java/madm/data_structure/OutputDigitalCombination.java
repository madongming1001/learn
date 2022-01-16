package madm.data_structure;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class OutputDigitalCombination {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String[] array = new String[]{
                "1", "2", "3", "4", "5", "6", "7", "8", "9"
        };
        listAll(Arrays.asList(array), " ");

    }

    public static void listAll(List<String> candidate, String prefix) {
        System.out.println(prefix);

        for (int i = 0; i < candidate.size(); i++) {
            List<String> temp = new LinkedList<String>(candidate);//new LinkedList<String>(candidate)---copy candidate
            listAll(temp, prefix + temp.remove(i));
        }
    }

}
