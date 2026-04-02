public class Instruction {
    public String opcode, funct3, funct7, type;

    public Instruction(String opcode, String funct3, String funct7, String type) {
        this.opcode = opcode;
        this.funct3 = funct3;
        this.funct7 = funct7;
        this.type = type;
    }
}