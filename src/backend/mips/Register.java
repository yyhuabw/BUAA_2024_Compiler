package backend.mips;

public enum Register implements MipsNode {
    ZERO("$zero"), // 0
    AT("$at"), // 1
    V0("$v0"), // 2
    V1("$v1"), // 3
    A0("$a0"), // 4
    A1("$a1"),
    A2("$a2"),
    A3("$a3"), // 7
    T0("$t0"), // 8
    T1("$t1"),
    T2("$t2"),
    T3("$t3"),
    T4("$t4"),
    T5("$t5"),
    T6("$t6"),
    T7("$t7"), // 15
    S0("$s0"), // 16
    S1("$s1"),
    S2("$s2"),
    S3("$s3"),
    S4("$s4"),
    S5("$s5"),
    S6("$s6"),
    S7("$s7"), // 23
    T8("$t8"), // 24
    T9("$t9"), // 25
    K0("$k0"), // 26
    K1("$k1"), // 27
    GP("$gp"), // 28
    SP("$sp"), // 29
    FP("$fp"), // 30
    RA("$ra"); // 31


    private final String name;

    Register(String name) {
        this.name = name;
    }

    public static Register getRegWithIndex(Register base, int index) {
        return values()[base.ordinal() + index];
    }

    @Override
    public String mipsOutput() {
        return name;
    }
}
