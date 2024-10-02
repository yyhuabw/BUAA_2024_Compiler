package frontend.parser.ast;

public enum SyntaxType {
    // opExp
    MUL_EXP("<MulExp>"),
    ADD_EXP("<AddExp>"),
    REL_EXP("<RelExp>"),
    EQ_EXP("<EqExp>"),
    LAND_EXP("<LAndExp>"),
    LOR_EXP("<LOrExp>"),

    // primaryExp
    PRIMARY_EXP("<PrimaryExp>"),
    LVAL("<LVal>"),
    NUMBER("<Number>"),
    CHARACTER("<Character>"),

    // unaryExp
    UNARY_EXP("<UnaryExp>"),
    UNARY_OP("<UnaryOp>"),

    // expression
    EXP("<Exp>"),
    COND_EXP("<Cond>"),
    FUNC_REAL_PARAMS("<FuncRParams>"),
    CONST_EXP("<ConstExp>"),

    // constant
    CONST_INITVAL("<ConstInitVal>"),
    CONST_DECL("<ConstDecl>"),
    CONST_DEF("<ConstDef>"),

    // variable
    VAR_DEF("<VarDef>"),
    INIT_VAL("<InitVal>"),
    VAR_DECL("<VarDecl>"),

    // stmt
    STMT("<Stmt>"),
    FOR_STMT("<ForStmt>"),

    // block
    BLOCK("<Block>"),

    // funcType
    FUNC_TYPE("<FuncType>"),

    // function
    FUNC_DEF("<FuncDef>"),
    MAIN_FUNC_DEF("<MainFuncDef>"),
    FUNC_FORMAL_PARAMS("<FuncFParams>"),
    FUNC_FORMAL_PARAM("<FuncFParam>"),

    // unit
    COMP_UNIT("<CompUnit>");

    private final String name;

    SyntaxType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
