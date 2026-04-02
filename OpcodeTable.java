import java.util.HashMap;

public class OpcodeTable {
    public static HashMap<String, Instruction> table = new HashMap<>();

    static {
        table.put("ADD",  new Instruction("0110011", "000", "0000000", "R"));
        table.put("SUB",  new Instruction("0110011", "000", "0100000", "R"));
        table.put("ADDI", new Instruction("0010011", "000", null, "I"));
        table.put("LW",   new Instruction("0000011", "010", null, "I"));
        table.put("SW",   new Instruction("0100011", "010", null, "S"));
        table.put("BEQ",  new Instruction("1100011", "000", null, "B"));
    }
}