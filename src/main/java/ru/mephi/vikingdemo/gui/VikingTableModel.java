package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.Viking;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VikingTableModel extends AbstractTableModel {

    private Viking[] data = new Viking[0];
    private final String[] columns = {"ID", "Name", "Age", "Height (cm)", "Hair color", "Beard style", "Equipment"};

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Viking viking = data[rowIndex];
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
        return data[row];
    }

    public void refresh(Viking[] newItems) {
        data = Arrays.copyOf(newItems, newItems.length);
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
        int row = data.length;
        data = Arrays.copyOf(data, data.length + 1);
        data[row] = viking;
        fireTableRowsInserted(row, row);
    }
}
