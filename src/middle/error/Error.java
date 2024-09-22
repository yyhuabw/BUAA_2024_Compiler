package middle.error;

public class Error implements Comparable<Error> {
    private final ErrorType type;
    private final int lineno;

    public Error(ErrorType type, int lineno) {
        this.type = type;
        this.lineno = lineno;
    }

    public ErrorType getType() {
        return type;
    }

    public int getLineno() {
        return lineno;
    }

    @Override
    public int compareTo(Error o) {
        return Integer.compare(getLineno(), o.getLineno());
    }
}
