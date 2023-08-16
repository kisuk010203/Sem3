import java.util.HashMap;
import java.util.LinkedList;

public class HashMapExample {
    public static void main(String[] args) {
        // Create a HashMap with String keys and LinkedList<String> values
        HashMap<String, LinkedList<String>> hashMap = new HashMap<>();

        // Create a LinkedList of strings
        LinkedList<String> list1 = new LinkedList<>();
        list1.add("Apple");
        list1.add("Banana");
        list1.add("Cherry");

        // Add the LinkedList to the HashMap with a key
        hashMap.put("fruits", list1);

        // Retrieve the LinkedList from the HashMap using the key
        LinkedList<String> retrievedList = hashMap.get("fruits");

        // Modify the retrieved LinkedList
        retrievedList.add("Durian");
        retrievedList.remove("Apple");

        // Print the modified elements of the original LinkedList
        LinkedList<String> originalList = hashMap.get("fruits");
        for (String item : originalList) {
            System.out.println(item);
        }
    }
}
