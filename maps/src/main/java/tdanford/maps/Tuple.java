package tdanford.maps;

import static java.util.stream.Collectors.joining;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class Tuple<T> {

    protected final T[] array;

    public Tuple(T... values) {
        array = values;
    }

    public int length() { return array.length; }

    public T at(final int i) { return array[i]; }

    public String toString() {
        return String.format("(%s)", Stream.of(array).map(String::valueOf).collect(joining(" ")));
    }

    public boolean equals(Object o) {
        if(!(o instanceof Tuple)) { return false; }
        Tuple t = (Tuple)o;
        return t.length() == length() &&
            Arrays.deepEquals(array, t.array);
    }

    public int hashCode() {
        return Objects.hash(array);
    }
}
