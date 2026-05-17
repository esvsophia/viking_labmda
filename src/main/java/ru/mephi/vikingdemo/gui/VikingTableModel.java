package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.Viking;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VikingTableModel extends AbstractTableModel {

    private final List<Viking> data = new ArrayList<>();
    private final String[] columns = {"ID", "Name", "Age", "Height (cm)", "Hair color", "Beard style", "Equipment"};

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Viking viking = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> viking.id();
            case 1 -> viking.name();
            case 2 -> viking.age();
            case 3 -> viking.heightCm();
            case 4 -> viking.hairColor();
            case 5 -> viking.beardStyle();
            case 6 -> formatEquipment(viking.equipment());
            default -> "";
        };
    }

    public Viking getVikingAt(int row) {
        return data.get(row);
    }

    public void refresh(List<Viking> newItems) {
        data.clear();
        data.addAll(newItems);
        fireTableDataChanged();
    }

    private String formatEquipment(List<EquipmentItem> equipment) {
        if (equipment == null || equipment.isEmpty()) {
            return "";
        }
        return equipment.stream()
                .map(item -> item.name() + " [" + item.quality() + "]")
                .collect(Collectors.joining(", "));
    }

    public void addViking(Viking viking) {
        int row = data.size();
        data.add(viking);
        fireTableRowsInserted(row, row);
    }
}
