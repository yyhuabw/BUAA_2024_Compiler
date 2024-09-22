package middle.error;

import java.util.TreeSet;

public class ErrorTable {
    private final TreeSet<Error> errors;
    private int cnt;

    public ErrorTable() {
        this.errors = new TreeSet<>();
        this.cnt = 0;
    }

    public void addError(Error error) {
        errors.add(error);
        cnt++;
    }

    public boolean isEmpty() {
        return cnt == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Error error : errors) {
            sb.append(error.getLineno()).append(" ").append(error.getType().getCode()).append("\n");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
