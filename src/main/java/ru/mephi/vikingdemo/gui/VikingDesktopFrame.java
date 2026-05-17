package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;


public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingTableModel tableModel = new VikingTableModel();

    public VikingDesktopFrame(VikingService vikingService) {
        this.vikingService = vikingService;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 420));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        JTable vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JButton randomBtn = new JButton("Random viking");
        randomBtn.addActionListener(e -> onRandomViking());

        JButton addSpecificButton = new JButton("Add viking");
        addSpecificButton.addActionListener(event -> onCreateViking());

        JButton deleteButton = new JButton("Delete selected");
        deleteButton.addActionListener(event -> onDeleteViking(vikingTable));

        JButton updateButton = new JButton("Update info");
        updateButton.addActionListener(event -> onUpdateViking(vikingTable));

        JButton deleteAllButton = new JButton("Delete all");
        deleteAllButton.addActionListener(event -> {
            vikingService.deleteAll();
            tableModel.refresh(vikingService.findAll());
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(randomBtn);
        bottomPanel.add(addSpecificButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteAllButton);
        add(bottomPanel, BorderLayout.SOUTH);
        tableModel.refresh(vikingService.findAll());
    }


    private void onCreateViking() {
        Viking template = new Viking(
                null,
                "New Viking",
                25,
                180,
                HairColor.Blond,
                BeardStyle.CLEAN_SHAVEN,
                new java.util.ArrayList<>()
        );

        VikingEditDialog dialog = new VikingEditDialog(this, template);
        dialog.setTitle("Create Viking");
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Viking newViking = dialog.getUpdatedViking(null);

            Viking saved = vikingService.save(newViking);
            tableModel.addViking(saved);
        }
    }

    private void onDeleteViking(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            Viking viking = tableModel.getVikingAt(selectedRow);
            vikingService.deleteById(viking.id());
            tableModel.refresh(vikingService.findAll());
        }
    }

    private void onRandomViking() {
        Viking saved = vikingService.createRandomViking();
        tableModel.addViking(saved);
    }

    public void addNewViking(Viking viking) {
        tableModel.addViking(viking);
    }

    private void onUpdateViking(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            Viking oldViking = tableModel.getVikingAt(selectedRow);

            VikingEditDialog dialog = new VikingEditDialog(this, oldViking);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                Viking updatedViking = dialog.getUpdatedViking(oldViking.id());
                vikingService.update(updatedViking);
                tableModel.refresh(vikingService.findAll());
            }
        }
    }
}
