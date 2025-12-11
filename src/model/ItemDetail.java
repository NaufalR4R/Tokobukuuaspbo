package model;

// Class helper baru untuk menampung data join
public class ItemDetail {
    public String judulBuku;
    public int jumlah;
    public double hargaSatuan;
    public double subtotal;

    public ItemDetail(String judulBuku, int jumlah, double hargaSatuan, double subtotal) {
        this.judulBuku = judulBuku;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSatuan;
        this.subtotal = subtotal;
    }
}
