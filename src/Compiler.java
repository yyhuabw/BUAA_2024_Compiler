import frontend.lexer.Lexer;
import frontend.parser.Parser;
import middle.error.ErrorTable;
import middle.symbol.SymbolManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

public class Compiler {
    public static void main(String[] args) throws Exception {
        String inputFileName = "testfile.txt";
        String outputFileName = "llvm_ir.txt";
        String errorFileName = "error.txt";

        PushbackInputStream inputStream =
                new PushbackInputStream(new FileInputStream(inputFileName));
        ErrorTable errorTable = new ErrorTable();
        Lexer lexer = new Lexer(inputStream, errorTable);
        Parser parser = new Parser(lexer.getTokenStream(), errorTable);
        parser.parseCompUnit();

        try (OutputStream outputStream = new FileOutputStream(outputFileName)) {
            try (OutputStream errStream = new FileOutputStream(errorFileName)) {
                if (errorTable.isEmpty()) {
                    outputStream.write(SymbolManager.getInstance().symbolInfoOutput().getBytes());
                } else {
                    errStream.write(errorTable.toString().getBytes());
                }
            }
        }
    }
}
