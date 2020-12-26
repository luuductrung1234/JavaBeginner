package util.collection;

public class ArrayExtensions {
    private ArrayExtensions() {
        super();
    }

    public static <T> String toString(T[] values) {
        return toString(values, "\n");
    }

    public static <T> String toString(T[] values, String separator) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[=============================\n");

        for (int i = 0; i < values.length; i++) {
            if (i == (values.length - 1))
                stringBuilder.append(" " + values[i]);
            stringBuilder.append(" " + values[i] + separator);
        }

        stringBuilder.append("=============================]");

        return stringBuilder.toString();
    }
}
