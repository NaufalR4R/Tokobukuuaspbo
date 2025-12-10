package admin.Pengguna;

import admin.Pengguna.KelolaPenggunaPanel;
import admin.buku.KelolaBukuPanel;
import admin.kategori.KelolaKategoriPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private final JPanel panel;
    private final JButton editButton;
    private final JButton deleteButton;
    private final JTable table;
    private final DefaultTableModel model;

    // parentPanel akan menyimpan instance dari KelolaBukuPanel, KelolaKategoriPanel, atau KelolaPenggunaPanel
    private final Object parentPanel;
    private int currentRow;

    /**
     * Konstruktor ButtonEditor.
     * @param table JTable tempat editor ini digunakan.
     * @param model Model tabel (DefaultTableModel).
     * @param parentPanel Instance dari Panel yang menggunakan editor ini (this dari panel tersebut).
     */
    public ButtonEditor(JTable table, DefaultTableModel model, Object parentPanel) {
        this.table = table;
        this.model = model;
        this.parentPanel = parentPanel;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
        panel.setOpaque(true);

        // --- Tombol Edit ---
        editButton = new JButton("Edit");
        editButton.setBackground(new Color(60, 179, 113));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font("SansSerif", Font.BOLD, 10));
        editButton.setActionCommand("Edit");
        editButton.addActionListener(this); // Mendaftarkan diri sebagai listener

        // --- Tombol Hapus ---
        deleteButton = new JButton("Hapus");
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setFont(new Font("SansSerif", Font.BOLD, 10));
        deleteButton.setActionCommand("Hapus");
        deleteButton.addActionListener(this); // Mendaftarkan diri sebagai listener

        panel.add(editButton);
        panel.add(deleteButton);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // Simpan indeks baris yang sedang diedit/diklik
        currentRow = row;

        // Atur warna latar belakang
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        // Setelah aksi selesai, kembalikan nilai default (String kosong)
        return "";
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        // Selalu bisa diedit (untuk memungkinkan klik tombol)
        return true;
    }

    /**
     * Dipanggil saat salah satu tombol (Edit atau Hapus) diklik.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped(); // Hentikan proses editing pada sel JTable

        String command = e.getActionCommand();

        if ("Edit".equals(command)) {
            // Panggil method handleEditAction dari panel yang sesuai
            if (parentPanel instanceof KelolaBukuPanel) {
                ((KelolaBukuPanel) parentPanel).handleEditAction(currentRow);
            } else if (parentPanel instanceof KelolaKategoriPanel) {
                ((KelolaKategoriPanel) parentPanel).handleEditAction(currentRow);
            } else if (parentPanel instanceof KelolaPenggunaPanel) {
                ((KelolaPenggunaPanel) parentPanel).handleEditAction(currentRow);
            }
        } else if ("Hapus".equals(command)) {
            // Panggil method handleDeleteAction dari panel yang sesuai
            if (parentPanel instanceof KelolaBukuPanel) {
                ((KelolaBukuPanel) parentPanel).handleDeleteAction(currentRow);
            } else if (parentPanel instanceof KelolaKategoriPanel) {
                ((KelolaKategoriPanel) parentPanel).handleDeleteAction(currentRow);
            } else if (parentPanel instanceof KelolaPenggunaPanel) {
                ((KelolaPenggunaPanel) parentPanel).handleDeleteAction(currentRow);
            }
        }
    }
}