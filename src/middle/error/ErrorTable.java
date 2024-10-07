package middle.error;

import java.util.TreeSet;

public class ErrorTable {
    private final TreeSet<Error> errorSet;
    private int cnt;
    private boolean isBacktracking;

    public ErrorTable() {
        this.errorSet = new TreeSet<>();
        this.cnt = 0;
        this.isBacktracking = false;
    }

    public void addError(Error error) {
        if (!isBacktracking) {
            errorSet.add(error);
            cnt++;
        }
    }

    public void setBacktrack(boolean state) {
        isBacktracking = state;
    }

    public boolean isEmpty() {
        return cnt == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Error error : errorSet) {
            sb.append(error.getLineno()).append(" ").append(error.getType().getCode()).append("\n");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
