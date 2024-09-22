import frontend.lexer.Lexer;
import middle.error.ErrorTable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

public class Compiler {
    public static void main(String[] args) throws Exception {
        String inputFileName = "testfile.txt";
        String outputFileName = "lexer.txt";
        String errorFileName = "error.txt";

        PushbackInputStream inputStream =
                new PushbackInputStream(new FileInputStream(inputFileName));
        ErrorTable errorTable = new ErrorTable();
        Lexer lexer = new Lexer(inputStream, errorTable);

        try (OutputStream outputStream = new FileOutputStream(outputFileName)) {
            try (OutputStream errStream = new FileOutputStream(errorFileName)) {
                if (errorTable.isEmpty()) {
                    outputStream.write(lexer.getTokens().toString().getBytes());
                } else {
                    errStream.write(errorTable.toString().getBytes());
                }
            }
        }
    }
}
