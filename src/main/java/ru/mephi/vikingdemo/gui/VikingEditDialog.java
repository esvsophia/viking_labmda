package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.EquipmentFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VikingEditDialog extends JDialog {
    private final JTextField nameField;
    private final JSpinner ageSpinner;
    private final JSpinner heightSpinner;
    private final JComboBox<HairColor> hairColorBox;
    private final JComboBox<BeardStyle> beardStyleBox;
    private final DefaultListModel<EquipmentItem> equipmentModel;
    private final JComboBox<String> equipmentNameBox;
    private final JComboBox<String> qualityBox;
    private boolean confirmed = false;

    public VikingEditDialog(Frame owner, Viking viking) {
        super(owner, "Viking Editor", true);
        setLayout(new BorderLayout(10, 10));

        JPanel mainInfoPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        mainInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainInfoPanel.add(new JLabel("Name:"));
        nameField = new JTextField(viking.name());
        mainInfoPanel.add(nameField);

        mainInfoPanel.add(new JLabel("Age:"));
        ageSpinner = new JSpinner(new SpinnerNumberModel(viking.age(), 1, 120, 1));
        mainInfoPanel.add(ageSpinner);

        mainInfoPanel.add(new JLabel("Height (cm):"));
        heightSpinner = new JSpinner(new SpinnerNumberModel(viking.heightCm(), 100, 250, 1));
        mainInfoPanel.add(heightSpinner);

        mainInfoPanel.add(new JLabel("Hair Color:"));
        hairColorBox = new JComboBox<>(HairColor.values());
        hairColorBox.setSelectedItem(viking.hairColor());
        mainInfoPanel.add(hairColorBox);

        mainInfoPanel.add(new JLabel("Beard Style:"));
        beardStyleBox = new JComboBox<>(BeardStyle.values());
        beardStyleBox.setSelectedItem(viking.beardStyle());
        mainInfoPanel.add(beardStyleBox);

        JPanel equipmentPanel = new JPanel(new BorderLayout());
        equipmentPanel.setBorder(BorderFactory.createTitledBorder("Equipment"));

        equipmentModel = new DefaultListModel<>();
        viking.equipment().forEach(equipmentModel::addElement);
        JList<EquipmentItem> equipmentList = new JList<>(equipmentModel);
        equipmentPanel.add(new JScrollPane(equipmentList), BorderLayout.CENTER);

        JPanel addEquipmentPanel = new JPanel(new FlowLayout());
        equipmentNameBox = new JComboBox<>(EquipmentFactory.EQUIPMENT_NAMES.toArray(new String[0]));
        qualityBox = new JComboBox<>(EquipmentFactory.EQUIPMENT_QUAL.toArray(new String[0]));

        JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(e -> {
            String name = (String) equipmentNameBox.getSelectedItem();
            String quality = (String) qualityBox.getSelectedItem();
            equipmentModel.addElement(new EquipmentItem(name, quality));
        });

        JButton removeItemButton = new JButton("Remove Selected");
        removeItemButton.addActionListener(e -> {
            int selectedIndex = equipmentList.getSelectedIndex();
            if (selectedIndex != -1) {
                equipmentModel.remove(selectedIndex);
            }
        });

        addEquipmentPanel.add(equipmentNameBox);
        addEquipmentPanel.add(qualityBox);
        addEquipmentPanel.add(addItemButton);
        addEquipmentPanel.add(removeItemButton);
        equipmentPanel.add(addEquipmentPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(mainInfoPanel, BorderLayout.NORTH);
        add(equipmentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Viking getUpdatedViking(Integer id) {
        List<EquipmentItem> equipment = new ArrayList<>();
        for (int i = 0; i < equipmentModel.size(); i++) {
            equipment.add(equipmentModel.get(i));
        }

        return new Viking(
                id,
                nameField.getText(),
                (int) ageSpinner.getValue(),
                (int) heightSpinner.getValue(),
                (HairColor) hairColorBox.getSelectedItem(),
                (BeardStyle) beardStyleBox.getSelectedItem(),
                equipment
        );
    }
}