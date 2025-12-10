package admin.Pengguna;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JPanel implements TableCellRenderer {

    private final JButton editButton;
    private final JButton deleteButton;

    public ButtonRenderer() {
        // Gunakan FlowLayout dengan sedikit padding untuk menampung kedua tombol
        setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));
        setOpaque(true);

        // --- Tombol Edit ---
        editButton = new JButton("Edit");
        editButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font("SansSerif", Font.BOLD, 10));
        add(editButton);

        // --- Tombol Hapus ---
        deleteButton = new JButton("Hapus");
        deleteButton.setBackground(new Color(220, 20, 60)); // Crimson
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setFont(new Font("SansSerif", Font.BOLD, 10));
        add(deleteButton);
    }

    /**
     * Mengembalikan komponen yang digunakan untuk menampilkan nilai di dalam sel JTable.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        // Atur warna latar belakang panel tombol
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            // Gunakan warna latar belakang tabel, di sini adalah warna default JPanel
            setBackground(table.getBackground());
        }

        // Kita mengembalikan JPanel ini (yang berisi dua tombol) sebagai komponen render
        return this;
    }
}
