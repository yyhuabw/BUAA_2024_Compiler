package middle.error;

public enum ErrorType {
    ILLEGAL_CHAR("a"),
    MISSING_SEMICN("i"),
    MISSING_R_PARENT("j"),
    MISSING_R_BRACK("k");

    private final String code;

    ErrorType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
