package middle.llvm_ir.instruction;

public enum IrInstrType {
    ALU, // ADD, SUB, MUL, SDIV, SREM, AND, OR,
    ICMP,
    ALLOCA,
    LOAD,
    STORE,
    CALL,
    IO,
    BR,
    RET,
    GEP, // get element ptr
    ZEXT,
    TRUNC,
    PHI,
    PC, // parallel copy
    MOVE
}
