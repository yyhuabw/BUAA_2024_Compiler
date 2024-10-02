package frontend.parser;

import frontend.lexer.Token;
import frontend.lexer.TokenStream;
import frontend.lexer.TokenType;
import frontend.parser.ast.CompUnit;
import frontend.parser.ast.declaration.constant.ConstDecl;
import frontend.parser.ast.declaration.constant.ConstDef;
import frontend.parser.ast.declaration.constant.constInitVal.ConstArrayInitVal;
import frontend.parser.ast.declaration.constant.constInitVal.ConstInitVal;
import frontend.parser.ast.declaration.decl.Decl;
import frontend.parser.ast.declaration.type.*;
import frontend.parser.ast.declaration.variable.VarDecl;
import frontend.parser.ast.declaration.variable.initVal.InitArrayVal;
import frontend.parser.ast.declaration.variable.initVal.InitVal;
import frontend.parser.ast.declaration.variable.varDef.VarDef;
import frontend.parser.ast.declaration.variable.varDef.VarInitDef;
import frontend.parser.ast.declaration.variable.varDef.VarNotInitDef;
import frontend.parser.ast.expression.opExp.*;
import frontend.parser.ast.expression.primaryExp.Character;
import frontend.parser.ast.expression.primaryExp.LVal;
import frontend.parser.ast.expression.primaryExp.Number;
import frontend.parser.ast.expression.primaryExp.ParentExp;
import frontend.parser.ast.expression.primaryExp.PrimaryExp;
import frontend.parser.ast.expression.single.Cond;
import frontend.parser.ast.expression.single.ConstExp;
import frontend.parser.ast.expression.single.Exp;
import frontend.parser.ast.expression.single.FuncRParams;
import frontend.parser.ast.expression.unaryExp.UnaryExp;
import frontend.parser.ast.expression.unaryExp.UnaryFuncExp;
import frontend.parser.ast.expression.unaryExp.UnaryOp;
import frontend.parser.ast.expression.unaryExp.UnaryOpExp;
import frontend.parser.ast.function.funcType.FuncType;
import frontend.parser.ast.function.funcType.FuncTypeEle;
import frontend.parser.ast.function.single.FuncDef;
import frontend.parser.ast.function.single.FuncFParam;
import frontend.parser.ast.function.single.FuncFParams;
import frontend.parser.ast.function.single.MainFuncDef;
import frontend.parser.ast.statement.block.Block;
import frontend.parser.ast.statement.block.BlockItem;
import frontend.parser.ast.statement.stmt.*;
import frontend.parser.ast.terminal.CharConst;
import frontend.parser.ast.terminal.Ident;
import frontend.parser.ast.terminal.IntConst;
import frontend.parser.ast.terminal.StringConst;

public class Parser {
    private final TokenStream tokenStream;
    private Token curToken;

    public Parser(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
        this.curToken = tokenStream.read();
    }

    private void read() {
        curToken = tokenStream.read();
    }

    private boolean curEquals(TokenType type) {
        return curToken.getType().equals(type);
    }

    public CompUnit parseCompUnit() {
    }

    // stmt
    public Stmt parseStmt() {
    }

    public AssignStmt parseAssignStmt() {
    }

    public ExpStmt parseExpStmt() {
    }

    public IfStmt parseIfStmt() {
    }

    public ForLoopStmt parseForLoopStmt() {
    }

    public BreakStmt parseBreakStmt() {
    }

    public ContinueStmt parseContinueStmt() {
    }

    public ReturnStmt parseReturnStmt() {
    }

    public GetintStmt parseGetintStmt() {
    }

    public GetcharStmt parseGetcharStmt() {
    }

    public PrintfStmt parsePrintfStmt() {
    }

    public ForStmt parseForStmt() {
    }

    // block
    public Block parseBlock() {
    }

    public BlockItem parseBlockItem() {
    }

    // decl
    public Decl parseDecl() {
    }

    // constant
    public ConstInitVal parseConstInitVal() {
    }

    public ConstArrayInitVal parseConstArrayInitVal() {
    }

    public ConstDecl parseConstDecl() {
    }

    public ConstDef parseConstDef() {
    }

    // variable
    public VarDef parseVarDef() {
    }

    public VarNotInitDef parseVarNotInitDef() {
    }

    public VarInitDef parseVarInitDef() {
    }

    public InitVal parseInitVal() {
    }

    public InitArrayVal parseInitArrayVal() {
    }

    public VarDecl parseVarDecl() {
    }

    // type
    public BType parseBType() {
        BTypeEle bTypeEle = null;
        if (curEquals(TokenType.INTTK)) {
            bTypeEle = new IntType(curToken);
        } else if (curEquals(TokenType.CHARTK)) {
            bTypeEle = new CharType(curToken);
        }
        read();
        return new BType(bTypeEle);
    }

    // funcType
    public FuncType parseFuncType() {
        FuncTypeEle funcTypeEle = null;
        if (curEquals(TokenType.VOIDTK)) {
            funcTypeEle = new VoidType(curToken);
        } else if (curEquals(TokenType.INTTK)) {
            funcTypeEle = new IntType(curToken);
        } else if (curEquals(TokenType.CHARTK)) {
            funcTypeEle = new CharType(curToken);
        }
        read();
        return new FuncType(funcTypeEle);
    }

    // function
    public FuncDef parseFuncDef() {
    }

    public MainFuncDef parseMainFuncDef() {
    }

    public FuncFParams parseFuncFParams() {
    }

    public FuncFParam parseFuncFParam() {
    }

    // expression
    public Exp parseExp() {
    }

    public Cond parseCond() {
    }

    public FuncRParams parseFuncRParams() {
    }

    public ConstExp parseConstExp() {
    }

    // opExp
    public MulExp parseMulExp() {
    }

    public AddExp parseAddExp() {
    }

    public RelExp parseRelExp() {
    }

    public EqExp parseEqExp() {
    }

    public LAndExp parseLAndExp() {
    }

    public LOrExp parseLOrExp() {
    }

    //unaryExp
    public UnaryExp parseUnaryExp() {
    }

    public UnaryFuncExp parseUnaryFuncExp() {
    }

    public UnaryOpExp parseUnaryOpExp() {
    }

    public UnaryOp parseUnaryOp() {
    }

    // primaryExp
    public PrimaryExp parsePrimaryExp() {
    }

    public ParentExp parseParentExp() {
    }

    public LVal parseLVal() {
    }

    public Number parseNumber() {
    }

    public Character parseCharacter() {
    }

    // terminal
    public Ident parseIdent() {
    }

    public IntConst parseIntConst() {
    }

    public CharConst parseCharConst() {
    }

    public StringConst parseStringConst() {
    }
}
