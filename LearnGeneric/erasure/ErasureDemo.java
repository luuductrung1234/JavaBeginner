package erasure;

import java.util.List;

public class ErasureDemo<T, B extends Comparable<B>> {

    public void unbounded(T param) {
    }

    public void lists(List<String> param1, List<List<T>> param2) {
        String s = param1.get(0);
    }

    public void bounded(B param) {
    }
}