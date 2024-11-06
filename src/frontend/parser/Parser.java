package frontend.parser;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenStream;
import frontend.lexer.token.TokenType;
import frontend.parser.ast.CompUnit;
import frontend.parser.ast.declaration.constant.ConstDecl;
import frontend.parser.ast.declaration.constant.ConstDef;
import frontend.parser.ast.declaration.constant.constInitVal.ConstArrayInitVal;
import frontend.parser.ast.declaration.constant.constInitVal.ConstInitVal;
import frontend.parser.ast.declaration.constant.constInitVal.ConstInitValEle;
import frontend.parser.ast.declaration.decl.Decl;
import frontend.parser.ast.declaration.decl.DeclEle;
import frontend.parser.ast.declaration.type.*;
import frontend.parser.ast.declaration.variable.VarDecl;
import frontend.parser.ast.declaration.variable.initVal.InitArrayVal;
import frontend.parser.ast.declaration.variable.initVal.InitVal;
import frontend.parser.ast.declaration.variable.initVal.InitValEle;
import frontend.parser.ast.declaration.variable.varDef.VarDef;
import frontend.parser.ast.declaration.variable.varDef.VarDefEle;
import frontend.parser.ast.declaration.variable.varDef.VarInitDef;
import frontend.parser.ast.declaration.variable.varDef.VarNotInitDef;
import frontend.parser.ast.expression.opExp.*;
import frontend.parser.ast.expression.primaryExp.*;
import frontend.parser.ast.expression.primaryExp.Character;
import frontend.parser.ast.expression.primaryExp.Number;
import frontend.parser.ast.expression.single.Cond;
import frontend.parser.ast.expression.single.ConstExp;
import frontend.parser.ast.expression.single.Exp;
import frontend.parser.ast.expression.single.FuncRParams;
import frontend.parser.ast.expression.unaryExp.*;
import frontend.parser.ast.function.funcType.FuncType;
import frontend.parser.ast.function.funcType.FuncTypeEle;
import frontend.parser.ast.function.single.FuncDef;
import frontend.parser.ast.function.single.FuncFParam;
import frontend.parser.ast.function.single.FuncFParams;
import frontend.parser.ast.function.single.MainFuncDef;
import frontend.parser.ast.statement.block.Block;
import frontend.parser.ast.statement.block.BlockItem;
import frontend.parser.ast.statement.block.BlockItemEle;
import frontend.parser.ast.statement.stmt.*;
import frontend.parser.ast.terminal.CharConst;
import frontend.parser.ast.terminal.Ident;
import frontend.parser.ast.terminal.IntConst;
import frontend.parser.ast.terminal.StringConst;
import middle.error.Error;
import middle.error.ErrorTable;
import middle.error.ErrorType;
import middle.symbol.FuncSymbol;
import middle.symbol.Symbol;
import middle.symbol.SymbolManager;
import middle.symbol.VarSymbol;
import middle.symbol.value.ValueType;

import java.util.ArrayList;

public class Parser {
    private final TokenStream tokenStream;
    private Token curToken;
    private final ErrorTable errorTable;

    public Parser(TokenStream tokenStream, ErrorTable errorTable) {
        this.tokenStream = tokenStream;
        this.curToken = tokenStream.read();
        this.errorTable = errorTable;
    }

    private Token getCurToken() {
        return tokenStream.getCurToken();
    }

    private void read() {
        curToken = tokenStream.read();
    }

    private void setBackPoint() {
        tokenStream.setBackPoint();
        errorTable.setBacktrack(true);
    }

    private void backtrack() {
        tokenStream.backtrack();
        errorTable.setBacktrack(false);
        curToken = tokenStream.getCurToken();
    }

    private boolean curEquals(TokenType type) {
        return curToken.getType().equals(type);
    }

    private boolean movedTkEquals(int offset, TokenType type) {
        return tokenStream.moveToken(offset).getType().equals(type);
    }

    private int getPrevTkLineno() {
        return tokenStream.moveToken(-1).getLineno();
    }

    private void addError(ErrorType type, int lineno) {
        errorTable.addError(new Error(type, lineno));
    }

    private boolean curInExpFirstSet() {
        return curEquals(TokenType.PLUS) ||
                curEquals(TokenType.MINU) ||
                curEquals(TokenType.NOT) ||
                curEquals(TokenType.IDENFR) ||
                curEquals(TokenType.LPARENT) ||
                curEquals(TokenType.INTCON) ||
                curEquals(TokenType.CHRCON);
    }

    /**
     * CompUnit → {Decl} {FuncDef} MainFuncDef
     * Decl → ConstDecl | VarDecl
     * - ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
     * - VarDecl → BType VarDef { ',' VarDef } ';'
     * FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
     * MainFuncDef → 'int' 'main' '(' ')' Block
     */
    public CompUnit parseCompUnit() {
        ArrayList<Decl> decls = new ArrayList<>();
        ArrayList<FuncDef> funcDefs = new ArrayList<>();
        MainFuncDef mainFuncDef = null;

        SymbolManager.getInstance().setGlobalStatus(true);
        SymbolManager.getInstance().enterScope(); // enter Scope

        while (true) {
            if (curToken == null) {
                break;
            } else if (movedTkEquals(1, TokenType.MAINTK)) {
                mainFuncDef = parseMainFuncDef();
                break;
            } else if (movedTkEquals(2, TokenType.LPARENT)) {
                funcDefs.add(parseFuncDef());
            } else if (curEquals(TokenType.CONSTTK) ||
                    curEquals(TokenType.INTTK) ||
                    curEquals(TokenType.CHARTK)) {
                decls.add(parseDecl());
            } else {
                break;
            }
        }

        SymbolManager.getInstance().leaveScope(); // leave Scope

        return new CompUnit(decls, funcDefs, mainFuncDef);
    }

    // stmt
    public Stmt parseStmt() {
        StmtEle stmtEle;

        switch (curToken.getType()) {
            case SEMICN -> stmtEle = parseNullStmt();
            case LBRACE -> stmtEle = parseBlock(true);
            case IFTK -> stmtEle = parseIfStmt();
            case FORTK -> stmtEle = parseForLoopStmt();
            case BREAKTK -> stmtEle = parseBreakStmt();
            case CONTINUETK -> stmtEle = parseContinueStmt();
            case RETURNTK -> stmtEle = parseReturnStmt();
            case PRINTFTK -> stmtEle = parsePrintfStmt();
            case IDENFR -> stmtEle = dealIdentCase();
            case LPARENT, INTCON, CHRCON, PLUS, MINU, NOT -> stmtEle = parseExpStmt();

            default -> stmtEle = new NullStmt(handleIError()); // error
        }

        return new Stmt(stmtEle);
    }

    /**
     * ExpStmt → Exp ';'
     * AssignStmt → LVal '=' Exp ';'
     * GetintStmt → LVal '=' 'getint''('')'';'
     * GetcharStmt → LVal '=' 'getchar''('')'';'
     */
    private StmtEle dealIdentCase() {
        setBackPoint();
        parseExp();
        if (curEquals(TokenType.ASSIGN)) {
            if (movedTkEquals(1, TokenType.GETINTTK)) {
                backtrack();
                return parseGetintStmt();
            } else if (movedTkEquals(1, TokenType.GETCHARTK)) {
                backtrack();
                return parseGetcharStmt();
            } else {
                backtrack();
                return parseAssignStmt();
            }
        } else {
            backtrack();
            return parseExpStmt();
        }
    }

    public AssignStmt parseAssignStmt() {
        LVal lVal = parseLVal();
        handleHError(lVal);

        Token assign = getCurToken();
        read();

        Exp exp = parseExp();

        Token semicolon = handleIError();

        return new AssignStmt(lVal, assign, exp, semicolon);
    }

    public NullStmt parseNullStmt() {
        Token semicolon = getCurToken();
        read();
        return new NullStmt(semicolon);
    }

    public ExpStmt parseExpStmt() {
        Exp exp = parseExp();
        Token semicolon = handleIError();
        return new ExpStmt(exp, semicolon);
    }

    public IfStmt parseIfStmt() {
        Token ifTk = getCurToken();
        read();

        Token leftParent = getCurToken();
        read();

        Cond cond = parseCond();

        Token rightParent = handleJError();

        Stmt ifStmt = parseStmt();

        if (curEquals(TokenType.ELSETK)) {
            Token elseTk = getCurToken();
            read();
            Stmt elseStmt = parseStmt();
            return new IfStmt(ifTk, leftParent, cond, rightParent, ifStmt, elseTk, elseStmt);
        }
        return new IfStmt(ifTk, leftParent, cond, rightParent, ifStmt);
    }

    public ForLoopStmt parseForLoopStmt() {
        ForStmt forStmt1 = null;
        Cond cond = null;
        ForStmt forStmt2 = null;

        Token forTk = getCurToken();
        read();

        Token leftParent = getCurToken();
        read();

        if (!curEquals(TokenType.SEMICN)) {
            forStmt1 = parseForStmt();
        }

        Token semicolon1 = getCurToken();
        read();

        if (!curEquals(TokenType.SEMICN)) {
            cond = parseCond();
        }

        Token semicolon2 = getCurToken();
        read();

        if (!curEquals(TokenType.RPARENT)) {
            forStmt2 = parseForStmt();
        }

        Token rightParent = getCurToken();
        read();

        SymbolManager.getInstance().enterLoop(); // enter loop
        Stmt stmt = parseStmt();
        SymbolManager.getInstance().leaveLoop(); // leave loop

        return new ForLoopStmt(forTk, leftParent, forStmt1, semicolon1, cond, semicolon2, forStmt2, rightParent, stmt);
    }

    public BreakStmt parseBreakStmt() {
        Token breakTk = getCurToken();
        read();
        Token semicolon = handleIError();
        handleMError(breakTk);
        return new BreakStmt(breakTk, semicolon);
    }

    public ContinueStmt parseContinueStmt() {
        Token continueTk = getCurToken();
        read();
        Token semicolon = handleIError();
        handleMError(continueTk);
        return new ContinueStmt(continueTk, semicolon);
    }

    public ReturnStmt parseReturnStmt() {
        Token returnTk = getCurToken();
        read();

        if (curInExpFirstSet()) {
            Exp exp = parseExp();
            handleFError(returnTk);
            Token semicolon = handleIError();
            return new ReturnStmt(returnTk, exp, semicolon);
        }
        Token semicolon = handleIError();
        return new ReturnStmt(returnTk, semicolon);
    }

    public GetintStmt parseGetintStmt() {
        LVal lVal = parseLVal();
        handleHError(lVal);

        Token assign = getCurToken();
        read();

        Token getintTk = getCurToken();
        read();

        Token leftParent = getCurToken();
        read();

        Token rightParent = handleJError();

        Token semicolon = handleIError();

        return new GetintStmt(lVal, assign, getintTk, leftParent, rightParent, semicolon);
    }

    public GetcharStmt parseGetcharStmt() {
        LVal lVal = parseLVal();
        handleHError(lVal);

        Token assign = getCurToken();
        read();

        Token getcharTk = getCurToken();
        read();

        Token leftParent = getCurToken();
        read();

        Token rightParent = handleJError();

        Token semicolon = handleIError();

        return new GetcharStmt(lVal, assign, getcharTk, leftParent, rightParent, semicolon);
    }

    public PrintfStmt parsePrintfStmt() {
        ArrayList<Token> commas = new ArrayList<>();
        ArrayList<Exp> exps = new ArrayList<>();

        Token printfTk = getCurToken();
        read();

        Token leftParent = getCurToken();
        read();

        StringConst stringConst = parseStringConst();

        while (curEquals(TokenType.COMMA)) {
            commas.add(getCurToken());
            read();
            exps.add(parseExp());
        }

        Token rightParent = handleJError();

        Token semicolon = handleIError();

        PrintfStmt printfStmt = new PrintfStmt(printfTk, leftParent, stringConst, commas, exps, rightParent, semicolon);
        handleLError(printfStmt);

        return printfStmt;
    }

    public ForStmt parseForStmt() {
        LVal lVal = parseLVal();
        handleHError(lVal);
        Token assign = getCurToken();
        read();
        Exp exp = parseExp();
        return new ForStmt(lVal, assign, exp);
    }

    // block
    public Block parseBlock(boolean needEnterScope) {
        ArrayList<BlockItem> blockItems = new ArrayList<>();

        if (needEnterScope) {
            SymbolManager.getInstance().enterScope(); // enter block
        }

        Token leftBrace = getCurToken();
        read();

        while (!curEquals(TokenType.RBRACE)) {
            blockItems.add(parseBlockItem());
        }

        Token rightBrace = getCurToken();
        read();

        if (needEnterScope) {
            SymbolManager.getInstance().leaveScope(); // leave block
        }

        return new Block(leftBrace, blockItems, rightBrace);
    }

    public BlockItem parseBlockItem() {
        BlockItemEle blockItemEle;

        if (curEquals(TokenType.CONSTTK) ||
                curEquals(TokenType.INTTK) ||
                curEquals(TokenType.CHARTK)) {
            blockItemEle = parseDecl();
        } else {
            blockItemEle = parseStmt();
        }

        return new BlockItem(blockItemEle);
    }

    // decl
    public Decl parseDecl() {
        DeclEle declEle;

        if (curEquals(TokenType.CONSTTK)) {
            declEle = parseConstDecl();
        } else {
            declEle = parseVarDecl();
        }

        return new Decl(declEle);
    }

    // constant
    public ConstInitVal parseConstInitVal() {
        ConstInitValEle constInitValEle;
        if (curEquals(TokenType.LBRACE)) {
            constInitValEle = parseConstArrayInitVal();
        } else if (curEquals(TokenType.STRCON)) {
            constInitValEle = parseStringConst();
        } else {
            constInitValEle = parseConstExp();
        }
        return new ConstInitVal(constInitValEle);
    }

    public ConstArrayInitVal parseConstArrayInitVal() {
        ConstExp first = null;
        ArrayList<Token> commas = new ArrayList<>();
        ArrayList<ConstExp> constExps = new ArrayList<>();

        Token leftBrace = getCurToken();
        read();

        if (!curEquals(TokenType.RBRACE)) {
            first = parseConstExp();
            while (curEquals(TokenType.COMMA)) {
                commas.add(getCurToken());
                read();
                constExps.add(parseConstExp());
            }
        }

        Token rightBrace = getCurToken();
        read();

        return  new ConstArrayInitVal(leftBrace, first, commas, constExps, rightBrace);
    }

    // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';' // i
    public ConstDecl parseConstDecl() {
        ArrayList<Token> commas = new ArrayList<>();
        ArrayList<ConstDef> constDefs = new ArrayList<>();

        Token constTk = getCurToken();
        read();

        BType bType = parseBType();

        ConstDef first = parseConstDef(bType.getValueType());

        while (curEquals(TokenType.COMMA)) {
            commas.add(getCurToken());
            read();
            constDefs.add(parseConstDef(bType.getValueType()));
        }

        // handle type i error
        Token semicolon = handleIError();

        return new ConstDecl(constTk, bType, first, commas, constDefs, semicolon);
    }

    public ConstDef parseConstDef(ValueType valueType) {
        ArrayList<Token> leftBrackets = new ArrayList<>();
        ArrayList<ConstExp> constExps = new ArrayList<>();
        ArrayList<Token> rightBrackets = new ArrayList<>();

        Ident ident = parseIdent();

        while (curEquals(TokenType.LBRACK)) {
            leftBrackets.add(getCurToken());
            read();
            constExps.add(parseConstExp());
            Token rightBracket = handleKError();
            rightBrackets.add(rightBracket);
        }

        Token assign = getCurToken();
        read();

        ConstInitVal constInitVal = parseConstInitVal();

        ConstDef constDef = new ConstDef(ident, leftBrackets, constExps, rightBrackets, assign, constInitVal);
        if (constDef.addToSTAndCheck(valueType)) { // b error
            handleBError(ident.getLineno());
        }

        return constDef;
    }

    // variable
    public VarDef parseVarDef(ValueType valueType) {
        VarDefEle varDefEle;
        ArrayList<Token> leftBrackets = new ArrayList<>();
        ArrayList<ConstExp> constExps = new ArrayList<>();
        ArrayList<Token> rightBrackets = new ArrayList<>();

        Ident ident = parseIdent();

        while (curEquals(TokenType.LBRACK)) {
            leftBrackets.add(getCurToken());
            read();
            constExps.add(parseConstExp());
            Token rightBracket = handleKError();
            rightBrackets.add(rightBracket);
        }

        if (curEquals(TokenType.ASSIGN)) {
            Token assign = getCurToken();
            read();
            InitVal initVal = parseInitVal();

            VarInitDef varInitDef = new VarInitDef(ident, leftBrackets, constExps, rightBrackets, assign, initVal);
            if (varInitDef.addToSTAndCheck(valueType)) { // b error
                handleBError(ident.getLineno());
            }
            varDefEle = varInitDef;
        } else {
            VarNotInitDef varNotInitDef = new VarNotInitDef(ident, leftBrackets, constExps, rightBrackets);
            if (varNotInitDef.addToSTAndCheck(valueType)) {
                handleBError(ident.getLineno());
            }
            varDefEle = varNotInitDef;
        }

        return new VarDef(varDefEle);
    }

    public InitVal parseInitVal() {
        InitValEle initValEle;

        if (curEquals(TokenType.LBRACE)) {
            initValEle = parseInitArrayVal();
        } else if (curEquals(TokenType.STRCON)) {
            initValEle = parseStringConst();
        } else {
            initValEle = parseExp();
        }

        return new InitVal(initValEle);
    }

    public InitArrayVal parseInitArrayVal() {
        Exp first = null;
        ArrayList<Token> commas = new ArrayList<>();
        ArrayList<Exp> exps = new ArrayList<>();

        Token leftBrace = getCurToken();
        read();

        if (!curEquals(TokenType.RBRACE)) {
            first = parseExp();
            while (curEquals(TokenType.COMMA)) {
                commas.add(getCurToken());
                read();
                exps.add(parseExp());
            }
        }

        Token rightBrace = getCurToken();
        read();

        return new InitArrayVal(leftBrace, first, commas, exps, rightBrace);
    }

    // VarDecl → BType VarDef { ',' VarDef } ';' // i
    public VarDecl parseVarDecl() {
        ArrayList<Token> commas = new ArrayList<>();
        ArrayList<VarDef> varDefs = new ArrayList<>();

        BType bType = parseBType();

        VarDef first = parseVarDef(bType.getValueType());

        while (curEquals(TokenType.COMMA)) {
            commas.add(getCurToken());
            read();
            varDefs.add(parseVarDef(bType.getValueType()));
        }

        // handle type i error
        Token semicolon = handleIError();

        return new VarDecl(bType, first, commas, varDefs, semicolon);
    }

    // type
    public BType parseBType() {
        BTypeEle bTypeEle = null;
        if (curEquals(TokenType.INTTK)) {
            bTypeEle = new IntType(getCurToken());
        } else if (curEquals(TokenType.CHARTK)) {
            bTypeEle = new CharType(getCurToken());
        }
        read();
        return new BType(bTypeEle);
    }

    // funcType
    public FuncType parseFuncType() {
        FuncTypeEle funcTypeEle = null;
        if (curEquals(TokenType.VOIDTK)) {
            funcTypeEle = new VoidType(getCurToken());
        } else if (curEquals(TokenType.INTTK)) {
            funcTypeEle = new IntType(getCurToken());
        } else if (curEquals(TokenType.CHARTK)) {
            funcTypeEle = new CharType(getCurToken());
        }
        read();
        return new FuncType(funcTypeEle);
    }

    // function
    // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block // j
    public FuncDef parseFuncDef() {
        FuncFParams funcFParams;

        FuncType funcType = parseFuncType();

        Ident ident = parseIdent();

        FuncDef funcDef = new FuncDef(funcType, ident);
        if (funcDef.addToSTAndCheck()) {
            handleBError(ident.getLineno());
        }

        SymbolManager.getInstance().setGlobalStatus(false);
        // add func copy to SymbolTable's curFunc
        SymbolManager.getInstance().enterFuncDef(new FuncSymbol(ident.getToken().getContent(), funcType.getReturnType())); // enter funcDef Scope

        Token leftParent = getCurToken();
        read();

        // the first set of FuncFParams is 'int' | 'char'
        if (curEquals(TokenType.INTTK) || curEquals(TokenType.CHARTK)) {
            funcFParams = parseFuncFParams();
            funcDef.setFuncFParams(funcFParams);
        }

        Token rightParent = handleJError();

        Block block = parseBlock(false);

        funcDef.setAttributes(leftParent, rightParent, block); // refill

        handleGError(funcType.getReturnType(), block);

        SymbolManager.getInstance().leaveFuncDef(); // leave funcDef Scope

        return funcDef;
    }

    public MainFuncDef parseMainFuncDef() {
        Token intTk = getCurToken();
        read();

        Token mainTk = getCurToken();
        read();

        Token leftParent = getCurToken();
        read();

        Token rightParent = handleJError();

        // add func copy to SymbolTable's curFunc
        FuncSymbol mainFuncSymbol = new FuncSymbol("main", ValueType.INT);
        SymbolManager.getInstance().setGlobalStatus(false);
        SymbolManager.getInstance().enterFuncDef(mainFuncSymbol); // enter funcDef Scope

        Block block = parseBlock(false);

        handleGError(ValueType.INT, block);

        SymbolManager.getInstance().leaveFuncDef(); // leave funcDef Scope

        MainFuncDef mainFuncDef = new MainFuncDef(intTk, mainTk, leftParent, rightParent, block);
        mainFuncDef.setMainFuncSymbol(mainFuncSymbol);
        return mainFuncDef;
    }

    public FuncFParams parseFuncFParams() {
        ArrayList<Token> commas = new ArrayList<>();
        ArrayList<FuncFParam> funcFParams = new ArrayList<>();

        FuncFParam first = parseFuncFParam();

        while (curEquals(TokenType.COMMA)) {
            commas.add(getCurToken());
            read();
            funcFParams.add(parseFuncFParam());
        }

        return new FuncFParams(first, commas, funcFParams);
    }

    public FuncFParam parseFuncFParam() {
        BType bType = parseBType();

        Ident ident = parseIdent();

        FuncFParam funcFParam;
        if (curEquals(TokenType.LBRACK)) {
            Token firstLeftBracket = getCurToken();
            read();

            Token firstRightBracket = handleKError();

            // multi-dimentional array
            ArrayList<Token> leftBrackets = new ArrayList<>();
            ArrayList<ConstExp> constExps = new ArrayList<>();
            ArrayList<Token> rightBrackets = new ArrayList<>();

            while (curEquals(TokenType.LBRACK)) {
                leftBrackets.add(getCurToken());
                read();

                constExps.add(parseConstExp());

                Token rightBracket = handleKError();
                rightBrackets.add(rightBracket);
            }

            funcFParam = new FuncFParam(bType, ident, firstLeftBracket, firstRightBracket, leftBrackets, constExps, rightBrackets);
        } else {
            funcFParam =  new FuncFParam(bType, ident);
        }

        if (funcFParam.addToSTAndCheck()) {
            handleBError(ident.getLineno());
        }
        return funcFParam;
    }

    // expression
    public Exp parseExp() {
        return new Exp(parseAddExp());
    }

    public Cond parseCond() {
        return new Cond(parseLOrExp());
    }

    public FuncRParams parseFuncRParams() {
        ArrayList<Token> commas = new ArrayList<>();
        ArrayList<Exp> exps = new ArrayList<>();

        Exp first = parseExp();

        while (curEquals(TokenType.COMMA)) {
            commas.add(getCurToken());
            read();
            exps.add(parseExp());
        }

        return new FuncRParams(first, commas, exps);
    }

    public ConstExp parseConstExp() {
        return new ConstExp(parseAddExp());
    }

    // opExp
    public MulExp parseMulExp() {
        ArrayList<Token> operators = new ArrayList<>();
        ArrayList<UnaryExp> operands = new ArrayList<>();

        UnaryExp first = parseUnaryExp();

        while (curEquals(TokenType.MULT) ||
                curEquals(TokenType.DIV) ||
                curEquals(TokenType.MOD)) {
            operators.add(getCurToken());
            read();
            operands.add(parseUnaryExp());
        }

        return new MulExp(first, operators, operands);
    }

    public AddExp parseAddExp() {
        ArrayList<Token> operators = new ArrayList<>();
        ArrayList<MulExp> operands = new ArrayList<>();

        MulExp first = parseMulExp();

        while (curEquals(TokenType.PLUS) ||
                curEquals(TokenType.MINU)) {
            operators.add(getCurToken());
            read();
            operands.add(parseMulExp());
        }

        return new AddExp(first, operators, operands);
    }

    public RelExp parseRelExp() {
        ArrayList<Token> operators = new ArrayList<>();
        ArrayList<AddExp> operands = new ArrayList<>();

        AddExp first = parseAddExp();

        while (curEquals(TokenType.LSS) ||
                curEquals(TokenType.GRE) ||
                curEquals(TokenType.LEQ) ||
                curEquals(TokenType.GEQ)) {
            operators.add(getCurToken());
            read();
            operands.add(parseAddExp());
        }

        return new RelExp(first, operators, operands);
    }

    public EqExp parseEqExp() {
        ArrayList<Token> operators = new ArrayList<>();
        ArrayList<RelExp> operands = new ArrayList<>();

        RelExp first = parseRelExp();

        while (curEquals(TokenType.EQL) ||
                curEquals(TokenType.NEQ)) {
            operators.add(getCurToken());
            read();
            operands.add(parseRelExp());
        }

        return new EqExp(first, operators, operands);
    }

    public LAndExp parseLAndExp() {
        ArrayList<Token> operators = new ArrayList<>();
        ArrayList<EqExp> operands = new ArrayList<>();

        EqExp first = parseEqExp();

        while (curEquals(TokenType.AND)) {
            operators.add(getCurToken());
            read();
            operands.add(parseEqExp());
        }

        return new LAndExp(first, operators, operands);
    }

    public LOrExp parseLOrExp() {
        ArrayList<Token> operators = new ArrayList<>();
        ArrayList<LAndExp> operands = new ArrayList<>();

        LAndExp first = parseLAndExp();

        while (curEquals(TokenType.OR)) {
            operators.add(getCurToken());
            read();
            operands.add(parseLAndExp());
        }

        return new LOrExp(first, operators, operands);
    }

    //unaryExp
    public UnaryExp parseUnaryExp() {
        UnaryExpEle unaryExpEle;

        if (curEquals(TokenType.IDENFR) &&
                movedTkEquals(1, TokenType.LPARENT)) {
            unaryExpEle = parseUnaryFuncExp();
        } else if (curEquals(TokenType.PLUS) ||
                curEquals(TokenType.MINU) ||
                curEquals(TokenType.NOT)) {
            unaryExpEle = parseUnaryOpExp();
        } else {
            unaryExpEle = parsePrimaryExp();
        }

        return new UnaryExp(unaryExpEle);
    }

    public UnaryFuncExp parseUnaryFuncExp() {
        Ident ident = parseIdent();

        handleCError(ident);

        Token leftParent = getCurToken();
        read();

        FuncRParams funcRParams = null;
        if (curInExpFirstSet()) {
            funcRParams = parseFuncRParams();
        }
        handleDError(ident, funcRParams);
        handleEError(ident, funcRParams);
        Token rightParent = handleJError();
        return new UnaryFuncExp(ident, leftParent, funcRParams, rightParent);
    }

    public UnaryOpExp parseUnaryOpExp() {
        UnaryOp unaryOp = parseUnaryOp();
        UnaryExp unaryExp = parseUnaryExp();
        return new UnaryOpExp(unaryOp, unaryExp);
    }

    public UnaryOp parseUnaryOp() {
        UnaryOp unaryOp = new UnaryOp(getCurToken());
        read();
        return unaryOp;
    }

    // primaryExp
    public PrimaryExp parsePrimaryExp() {
        PrimaryExpEle primaryExpEle = null;
        if (curEquals(TokenType.LPARENT)) {
            primaryExpEle = parseParentExp();
        } else if (curEquals(TokenType.IDENFR)) {
            primaryExpEle = parseLVal();
        } else if (curEquals(TokenType.INTCON)) {
            primaryExpEle = parseNumber();
        } else if (curEquals(TokenType.CHRCON)) {
            primaryExpEle = parseCharacter();
        }
        return new PrimaryExp(primaryExpEle);
    }

    public ParentExp parseParentExp() {
        Token leftParent = getCurToken();
        read();
        Exp exp = parseExp();
        Token rightParent = handleJError();
        return new ParentExp(leftParent, exp, rightParent);
    }

    public LVal parseLVal() {
        ArrayList<Token> leftBrackets = new ArrayList<>();
        ArrayList<Exp> exps = new ArrayList<>();
        ArrayList<Token> rightBrackets = new ArrayList<>();

        Ident ident = parseIdent();

        handleCError(ident);

        while (curEquals(TokenType.LBRACK)) {
            leftBrackets.add(getCurToken());
            read();
            exps.add(parseExp());
            Token rightBracket = handleKError();
            rightBrackets.add(rightBracket);
        }

        return new LVal(ident, leftBrackets, exps, rightBrackets);
    }

    public Number parseNumber() {
        return new Number(parseIntConst());
    }

    public Character parseCharacter() {
        return new Character(parseCharConst());
    }

    // terminal
    public Ident parseIdent() {
        Ident ident = new Ident(getCurToken());
        read();
        return ident;
    }

    public IntConst parseIntConst() {
        IntConst intConst = new IntConst(getCurToken());
        read();
        return intConst;
    }

    public CharConst parseCharConst() {
        CharConst charConst = new CharConst(getCurToken());
        read();
        return charConst;
    }

    public StringConst parseStringConst() {
        StringConst stringConst = new StringConst(getCurToken());
        read();
        return stringConst;
    }

    private Token handleIError() {
        Token semicolon;
        if (curEquals(TokenType.SEMICN)) {
            semicolon = getCurToken();
            read();
        } else {
            int lineno = getPrevTkLineno();
            addError(ErrorType.MISSING_SEMICN, lineno);
            semicolon = new Token(TokenType.SEMICN, "", lineno);
        }
        return semicolon;
    }

    private Token handleJError() {
        Token rightParent;
        if (curEquals(TokenType.RPARENT)) {
            rightParent = getCurToken();
            read();
        } else {
            int lineno = getPrevTkLineno();
            addError(ErrorType.MISSING_R_PARENT, lineno);
            rightParent = new Token(TokenType.RPARENT, "", lineno);
        }
        return rightParent;
    }

    private Token handleKError() {
        Token rightBracket;
        if (curEquals(TokenType.RBRACK)) {
            rightBracket = getCurToken();
            read();
        } else {
            int lineno = getPrevTkLineno();
            addError(ErrorType.MISSING_R_BRACK, lineno);
            rightBracket = new Token(TokenType.RBRACK, "", lineno);
        }
        return rightBracket;
    }

    private void handleBError(int lineno) {
        addError(ErrorType.REDEFINED_IDENT, lineno);
    }

    private void handleCError(Ident ident) {
        if (SymbolManager.getInstance().getSymbol(ident.getToken().getContent()) == null) {
            addError(ErrorType.UNDEFINED_IDENT, ident.getLineno());
        }
    }

    private void handleDError(Ident ident, FuncRParams funcRParams) {
        Symbol symbol = SymbolManager.getInstance().getSymbol(ident.getToken().getContent());
        if (!(symbol instanceof FuncSymbol funcSymbol)) {
            return; // undefined ident
        }
        if ((funcRParams == null && funcSymbol.getParamsSize() == 0)
                || (funcRParams != null && (funcSymbol.getParamsSize() == funcRParams.getParamsSize()))) {
            return;
        }
        addError(ErrorType.PARAM_NUM_MISMATCH, ident.getLineno());
    }

    private void handleEError(Ident ident, FuncRParams funcRParams) {
        Symbol symbol = SymbolManager.getInstance().getSymbol(ident.getToken().getContent());
        if (!(symbol instanceof FuncSymbol funcSymbol)) {
            return; // undefined ident
        }
        if (funcRParams != null && funcSymbol.getParamsSize() == funcRParams.getParamsSize()) {
            ArrayList<Exp> exps = funcRParams.getAllExps();
            ArrayList<VarSymbol> varSymbols = funcSymbol.getSymbols();
            for (int i = 0; i < funcSymbol.getParamsSize(); i++) {
                Exp exp = exps.get(i);
                int expDim = exp.getDim();
                VarSymbol varSymbol = varSymbols.get(i);

                if (expDim < 0) { // undefined ident
                    return;
                }

                if (expDim != varSymbol.getDim()) { // array <-> int
                    addError(ErrorType.PARAM_TYPE_MISMATCH, ident.getLineno());
                    return;
                }

                // exp.getDim() == varSymbol.getDim()
                if (expDim > 0
                        && !exp.getValueType().equals(varSymbol.getValueType())) { // int array <-> char array
                    addError(ErrorType.PARAM_TYPE_MISMATCH, ident.getLineno());
                    return;
                }
            }
        }
    }

    private void handleFError(Token token) {
        if (SymbolManager.getInstance().getCurFunc().getReturnType().equals(ValueType.VOID)) {
            addError(ErrorType.VOID_MISMATCH_RETURN, token.getLineno());
        }
    }

    private void handleGError(ValueType returnType, Block block) {
        ArrayList<BlockItem> blockItems = block.getBlockItems();
        if (blockItems.isEmpty()) {
            if (!returnType.equals(ValueType.VOID)) {
                addError(ErrorType.MISSING_RETURN, block.getRightBraceLineno());
            }
            return;
        }
        BlockItemEle lastBlockItemEle = blockItems.get(blockItems.size() - 1).getBlockItemEle();
        if (lastBlockItemEle instanceof Stmt stmt) {
            if (stmt.getStmtEle() instanceof ReturnStmt) {
                return;
            }
        }
        if (!returnType.equals(ValueType.VOID)) {
            addError(ErrorType.MISSING_RETURN, block.getRightBraceLineno());
        }
    }

    private void handleHError(LVal lVal) {
        if (lVal.isConst()) {
            addError(ErrorType.ALTER_CONST, lVal.getLineno());
        }
    }

    private void handleLError(PrintfStmt printfStmt) {
        if (printfStmt.isWrongFormat()) {
            addError(ErrorType.PRINTF_FORMAT_MISMATCH, printfStmt.getLineno());
        }
    }

    private void handleMError(Token token) {
        if (SymbolManager.getInstance().notInLoop()) {
            addError(ErrorType.MISUSE_BREAK_OR_CONTINUE, token.getLineno());
        }
    }
}
