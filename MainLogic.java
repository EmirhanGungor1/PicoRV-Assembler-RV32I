import java.util.*;

public class MainLogic {
    public static HashMap<String, Integer> symbolTable = new HashMap<>();

    public static void pass1(List<String> satirlar) {
        symbolTable.clear();
        int pc = 0;
        for (String satir : satirlar) {
            String s = satir.trim();
            if (s.contains(":")) {
                String label = s.split(":")[0].trim();
                symbolTable.put(label, pc);
                if (s.split(":").length > 1 && !s.split(":")[1].trim().isEmpty()) pc += 4;
            } else if (!s.startsWith(".") && !s.isEmpty()) {
                pc += 4;
            }
        }
    }

    public static String pass2(List<String> satirlar) {
        StringBuilder sb = new StringBuilder();
        int pc = 0;
        for (String satir : satirlar) {
            String s = satir.trim();
            if (s.isEmpty() || s.startsWith(".")) continue;
            if (s.contains(":")) {
                String[] parts = s.split(":");
                if (parts.length < 2 || parts[1].trim().isEmpty()) continue;
                s = parts[1].trim();
            }

            String[] p = s.replace(",", "").split("\\s+");
            Instruction inst = OpcodeTable.table.get(p[0].toUpperCase());

            if (inst != null) {
                try {
                    String res = "";
                    if (inst.type.equals("R")) {
                        res = inst.funct7 + getBin(p[3], 5) + getBin(p[2], 5) + inst.funct3 + getBin(p[1], 5) + inst.opcode;
                    } else if (inst.type.equals("I")) {
                        res = getBin(p[3], 12) + getBin(p[2], 5) + inst.funct3 + getBin(p[1], 5) + inst.opcode;
                    } else if (inst.type.equals("S")) {
                        String imm = getBin(p[3], 12);
                        res = imm.substring(0, 7) + getBin(p[1], 5) + getBin(p[2], 5) + inst.funct3 + imm.substring(7, 12) + inst.opcode;
                    } else if (inst.type.equals("B")) {
                        int offset = symbolTable.get(p[3]) - pc;
                        String imm = getBin(String.valueOf(offset), 13);
                        res = imm.charAt(0) + imm.substring(2, 8) + getBin(p[2], 5) + getBin(p[1], 5) + inst.funct3 + imm.substring(8, 12) + imm.charAt(1) + inst.opcode;
                    }
                    String hex = Long.toHexString(Long.parseLong(res, 2)).toUpperCase();
                    while (hex.length() < 8) hex = "0" + hex;
                    sb.append(String.format("%-20s -> 0x%s\n", s, hex));
                    pc += 4;
                } catch (Exception e) { sb.append("Hata: ").append(s).append("\n"); }
            }
        }
        return sb.toString();
    }

    public static String getBin(String val, int len) {
        try {
            int n = Integer.parseInt(val.replaceAll("[^0-9-]", ""));
            int mask = (1 << len) - 1;
            String b = Integer.toBinaryString(n & mask);
            if (b.length() > len) b = b.substring(b.length() - len);
            return String.format("%" + len + "s", b).replace(' ', '0');
        } catch (Exception e) { return "0".repeat(len); }
    }
}