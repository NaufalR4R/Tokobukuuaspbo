package admin.kategori;

import admin.buku.KelolaBukuPanel;

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

    private final Object parentPanel;
    private int currentRow;

    public ButtonEditor(JTable table, DefaultTableModel model, Object parentPanel) {
        this.table = table;
        this.model = model;
        this.parentPanel = parentPanel;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
        panel.setOpaque(true);

        // Tombol Edit
        editButton = new JButton("Edit");
        editButton.setBackground(new Color(60, 179, 113));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font("SansSerif", Font.BOLD, 10));
        editButton.setActionCommand("Edit");
        editButton.addActionListener(this);

        // Tombol Hapus
        deleteButton = new JButton("Hapus");
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setFont(new Font("SansSerif", Font.BOLD, 10));
        deleteButton.setActionCommand("Hapus");
        deleteButton.addActionListener(this);

        panel.add(editButton);
        panel.add(deleteButton);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentRow = row;

        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    // Method yang dipanggil saat tombol diklik
    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();

        if ("Edit".equals(e.getActionCommand())) {
            if (parentPanel instanceof KelolaBukuPanel) {
                ((KelolaBukuPanel) parentPanel).handleEditAction(currentRow);
            } else if (parentPanel instanceof KelolaKategoriPanel) {
                ((KelolaKategoriPanel) parentPanel).handleEditAction(currentRow);
            }
        } else if ("Hapus".equals(e.getActionCommand())) {
            if (parentPanel instanceof KelolaBukuPanel) {
                ((KelolaBukuPanel) parentPanel).handleDeleteAction(currentRow);
            } else if (parentPanel instanceof KelolaKategoriPanel) {
                ((KelolaKategoriPanel) parentPanel).handleDeleteAction(currentRow);
            }
        }
    }
}