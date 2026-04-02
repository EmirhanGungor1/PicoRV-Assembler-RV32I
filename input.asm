.text
    ADDI x1, x0, 10      # Toplam limiti (n = 10)
    ADDI x2, x0, 0       # Toplam sonucu (sum = 0)
    ADDI x3, x0, 1       # Sayac (i = 1)

loop:
    ADD x2, x2, x3       # sum = sum + i
    ADDI x3, x3, 1       # i = i + 1
    BLT x3, x1, loop     # i < 10 ise loop'a dön (BLT eklemediysen BEQ ile kontrol edebilirsin)
    
    SW x2, 0(x10)        # Sonucu hafızaya kaydet
    ADDI x4, x2, -5      # Negatif sayı testi (sum - 5)
.end