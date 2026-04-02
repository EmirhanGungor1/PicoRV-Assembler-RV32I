import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class Main extends JFrame {
    private JTextArea inputArea, outputArea, symbolArea;

    public Main() {
        // Modern Look and Feel Ayarı (Parıltıları siler)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}

        // Pencere Temel Ayarları
        setTitle("SUBÜ - PicoRV32I Assembler v1.0");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Ekranın ortasında açılır
        
        // Ana Arka Plan (Deep Dark)
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new BorderLayout(15, 15));

        // 1. ÜST PANEL (BAŞLIK)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(45, 45, 45));
        JLabel headerLabel = new JLabel("RISC-V (PicoRV) Assembler Editor");
        headerLabel.setForeground(new Color(200, 200, 200));
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // 2. ORTA PANEL (EDİTÖRLER)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(new Color(30, 30, 30));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        // Giriş Alanı (ASM)
        inputArea = new JTextArea(".text\nADDI x1, x0, 5\nloop:\nADD x2, x2, x1\nBEQ x2, x1, loop");
        setupEditor(inputArea, "Assembly Input (.asm)", new Color(30, 255, 30)); // İmleç yeşil
        
        // Çıkış Alanı (HEX)
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        setupEditor(outputArea, "Machine Code (HEX Output)", Color.CYAN);

        centerPanel.add(createScrollPane(inputArea));
        centerPanel.add(createScrollPane(outputArea));
        add(centerPanel, BorderLayout.CENTER);

        // 3. ALT PANEL (BUTON VE SEMBOLLER)
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        // ASSEMBLE BUTONU (Modern Mavi)
        JButton assembleBtn = new JButton("RUN ASSEMBLER (PASS 1 & 2)");
        assembleBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        assembleBtn.setBackground(new Color(0, 120, 215));
        assembleBtn.setForeground(Color.WHITE);
        assembleBtn.setFocusPainted(false);
        assembleBtn.setPreferredSize(new Dimension(0, 55));
        assembleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // SEMBOL TABLOSU (Matrix Yeşili - Artık Beyaz Değil!)
        symbolArea = new JTextArea(4, 50);
        symbolArea.setEditable(false);
        symbolArea.setBackground(new Color(20, 20, 20));
        symbolArea.setForeground(new Color(50, 255, 50));
        symbolArea.setFont(new Font("Consolas", Font.BOLD, 15));
        
        TitledBorder symBorder = BorderFactory.createTitledBorder("Detected Labels & Memory Map");
        symBorder.setTitleColor(new Color(150, 150, 150));
        symbolArea.setBorder(BorderFactory.createCompoundBorder(symBorder, BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        bottomPanel.add(assembleBtn, BorderLayout.NORTH);
        bottomPanel.add(createScrollPane(symbolArea), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // ASSEMBLE AKSİYONU
        assembleBtn.addActionListener(e -> {
            try {
                List<String> lines = Arrays.asList(inputArea.getText().split("\n"));
                MainLogic.pass1(lines);
                symbolArea.setText("Detected Labels and Addresses:\n" + MainLogic.symbolTable);
                outputArea.setText(MainLogic.pass2(lines));
                outputArea.setForeground(new Color(100, 255, 100)); // Başarı durumunda parlak yeşil
            } catch (Exception ex) {
                outputArea.setText("Compilation Error:\n" + ex.getMessage());
                outputArea.setForeground(Color.ORANGE);
            }
        });
    }

    private void setupEditor(JTextArea area, String title, Color caretColor) {
        area.setBackground(new Color(35, 35, 35));
        area.setForeground(new Color(230, 230, 230));
        area.setCaretColor(caretColor);
        area.setFont(new Font("Consolas", Font.PLAIN, 16));
        area.setMargin(new Insets(10, 10, 10, 10));
        
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(new Color(120, 120, 120));
        area.setBorder(border);
    }

    private JScrollPane createScrollPane(JTextArea area) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
        return scroll;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}