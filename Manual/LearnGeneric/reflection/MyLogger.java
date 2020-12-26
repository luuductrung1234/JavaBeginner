package reflection;

public class MyLogger {
    private String value;

    public MyLogger(String value) {
        super();
        this.value = value;
    }

    public void log() {
        System.out.println(value);
    }
}