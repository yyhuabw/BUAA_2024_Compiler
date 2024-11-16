package middle.llvm_ir;

import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.jump.io.*;
import middle.llvm_ir.type.IrModuleType;
import middle.llvm_ir.utils.IrGlobalVar;
import middle.llvm_ir.utils.IrStrLiteral;

import java.util.ArrayList;

public class IrModule extends IrValue {
    private final ArrayList<String> declareList;
    private final ArrayList<IrStrLiteral> strLiteralList;
    private final ArrayList<IrGlobalVar> globalVarList;
    private final ArrayList<IrFunction> funcList;

    public IrModule() {
        super(IrModuleType.MODULE, "module");
        this.declareList = new ArrayList<>();
        this.strLiteralList = new ArrayList<>();
        this.globalVarList = new ArrayList<>();
        this.funcList = new ArrayList<>();

        initDecl();
    }

    private void initDecl() {
        addDeclare(IrGetintInstr.getDeclare());
        addDeclare(IrGetcharInstr.getDeclare());
        addDeclare(IrPutintInstr.getDeclare());
        addDeclare(IrPutchInstr.getDeclare());
        addDeclare(IrPutstrInstr.getDeclare());
    }

    public void addDeclare(String declare) {
        declareList.add(declare);
    }

    public void addStrLiteral(IrStrLiteral strLiteral) {
        strLiteralList.add(strLiteral);
    }

    public void addGlobalVar(IrGlobalVar globalVar) {
        globalVarList.add(globalVar);
    }

    public void addFunc(IrFunction function) {
        funcList.add(function);
    }

    public ArrayList<IrFunction> getFuncList() {
        return funcList;
    }

    @Override
    public String irOutput() {
        StringBuilder sb = new StringBuilder();

        for (String declare : declareList) {
            sb.append(declare);
        }
        sb.append("\n");

        for (IrStrLiteral strLiteral : strLiteralList) {
            sb.append(strLiteral.irOutput());
        }
        sb.append("\n");

        for (IrGlobalVar globalVar : globalVarList) {
            sb.append(globalVar.irOutput());
        }
        sb.append("\n");

        for (IrFunction function : funcList) {
            sb.append(function.irOutput());
        }

        return sb.toString();
    }
}
