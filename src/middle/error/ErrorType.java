package middle.error;

public enum ErrorType {
    ILLEGAL_CHAR("a");

    private final String code;

    ErrorType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
