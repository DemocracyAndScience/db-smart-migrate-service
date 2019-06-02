import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringTest {

    public static void main(String[] args) {


        int lessonMaxCount = 2 ;
        Set<Integer> collect = Stream.iterate(1, x -> x + 1).limit(lessonMaxCount).collect(Collectors.toSet());
        System.out.println(collect);
    }


    public static void a(HashSet<Object> objects) {


        HashSet<Object> objects1 = new HashSet<>();
        objects1.add(1);
        objects = objects1;
    }

}
