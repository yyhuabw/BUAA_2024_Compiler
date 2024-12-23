import backend.mips.MipsBuilder;
import backend.mips.MipsModule;
import frontend.lexer.Lexer;
import frontend.parser.Parser;
import frontend.parser.ast.CompUnit;
import middle.error.ErrorTable;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrModule;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

public class Compiler {
    public static void main(String[] args) throws Exception {
        String inputFileName = "testfile.txt";
        String irOutputFileName = "llvm_ir.txt";
        String outputFileName = "mips.txt";
        String errorFileName = "error.txt";

        PushbackInputStream inputStream =
                new PushbackInputStream(new FileInputStream(inputFileName));
        ErrorTable errorTable = new ErrorTable();

        Lexer lexer = new Lexer(inputStream, errorTable);

        Parser parser = new Parser(lexer.getTokenStream(), errorTable);
        CompUnit compUnit = parser.parseCompUnit();

        try (OutputStream errStream = new FileOutputStream(errorFileName)) {
            if (!errorTable.isEmpty()) {
                errStream.write(errorTable.toString().getBytes());
                return;
            }
        }

        IrBuilder.getInstance().setAutoInsertMode();
        compUnit.genIR();
        IrModule irModule = IrBuilder.getInstance().getModule();


        try (OutputStream outputStream = new FileOutputStream(irOutputFileName)) {
            outputStream.write(irModule.irOutput().getBytes());
        }

        irModule.genAsm();
        MipsModule mipsModule = MipsBuilder.getInstance().getModule();

        try (OutputStream outputStream = new FileOutputStream(outputFileName)) {
            outputStream.write(mipsModule.mipsOutput().getBytes());
        }
    }
}
