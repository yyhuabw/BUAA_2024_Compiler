import frontend.lexer.Lexer;
import frontend.parser.Parser;
import middle.error.ErrorTable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

public class Compiler {
    public static void main(String[] args) throws Exception {
        String inputFileName = "testfile.txt";
        String outputFileName = "parser.txt";
        String errorFileName = "error.txt";

        PushbackInputStream inputStream =
                new PushbackInputStream(new FileInputStream(inputFileName));
        ErrorTable errorTable = new ErrorTable();
        Lexer lexer = new Lexer(inputStream, errorTable);
        Parser parser = new Parser(lexer.getTokenStream());

        try (OutputStream outputStream = new FileOutputStream(outputFileName)) {
            outputStream.write(parser.parseCompUnit().syntaxInfoOutput().getBytes());
        }
    }
}
