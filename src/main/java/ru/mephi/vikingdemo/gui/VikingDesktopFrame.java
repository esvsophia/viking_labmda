package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingLambdaService;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingLambdaService lambdaService;
    private final VikingTableModel tableModel = new VikingTableModel();

    public VikingDesktopFrame(VikingService vikingService, VikingLambdaService lambdaService) {
        this.vikingService = vikingService;
        this.lambdaService = lambdaService;

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

        JButton massButton = new JButton("Generation (15)");
        massButton.addActionListener(e -> {
            vikingService.generateAndSaveMassive(15);
            tableModel.refresh(vikingService.findAll());
        });

        JButton statsButton = new JButton("Statistics");
        statsButton.addActionListener(e -> showStatsDialog());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(randomBtn);
        bottomPanel.add(massButton);
        bottomPanel.add(statsButton);
        add(bottomPanel, BorderLayout.SOUTH);

        tableModel.refresh(vikingService.findAll());
    }

    private void onRandomViking() {
        Viking saved = vikingService.createRandomViking();
        tableModel.addViking(saved);
    }

    public void addNewViking(Viking viking) {
        tableModel.addViking(viking);
    }

    private void showStatsDialog() {
        JDialog dialog = new JDialog(this, "Выбор статистики", true);
        dialog.setSize(650, 480);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel gridPanel = new JPanel(new GridLayout(8, 2, 15, 10));

        ButtonGroup group = new ButtonGroup();
        JRadioButton[] buttons = {
                new JRadioButton("Возраст более 30"),
                new JRadioButton("Возраст более 20"),
                new JRadioButton("Возраст менее 20"),
                new JRadioButton("Возраст равен 33"),
                new JRadioButton("Возраст в диапазоне от 20 до 35"),
                new JRadioButton("Возраст в диапазоне от 20 до 40"),
                new JRadioButton("Возраст вне диапазона от 30 до 45"),
                new JRadioButton("Возраст вне диапазона от 20 до 40"),
                new JRadioButton("Блондин с короткой бородой"),
                new JRadioButton("Рыжие с длинной бородой"),
                new JRadioButton("С 1 или 2 топорами"),
                new JRadioButton("Случайный выше 180 см"),
                new JRadioButton("С легендарным вооружением"),
                new JRadioButton("Рыжебородые (сорт. по возр.)"),
                new JRadioButton("Максимальный ID в базе"),
                new JRadioButton("Все четные ID")
        };

        for (JRadioButton rb : buttons) {
            group.add(rb);
            gridPanel.add(rb);
        }
        buttons[0].setSelected(true);

        JButton confirm = new JButton("Показать результат");
        confirm.setPreferredSize(new Dimension(160, 30));
        confirm.addActionListener(e -> {
            int selectedIndex = -1;
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i].isSelected()) {
                    selectedIndex = i;
                    break;
                }
            }

            String result = switch (selectedIndex) {
                case 0 -> "Количество: " + lambdaService.countOlderThan(30);
                case 1 -> "Количество: " + lambdaService.countOlderThan(20);
                case 2 -> "Количество: " + lambdaService.countYoungerThan(20);
                case 3 -> "Количество: " + lambdaService.countEqualAge(33);
                case 4 -> "Количество: " + lambdaService.countInAgeRange(20, 35);
                case 5 -> "Количество: " + lambdaService.countInAgeRange(20, 40);
                case 6 -> "Количество: " + lambdaService.countOutsideAgeRange(30, 45);
                case 7 -> "Количество: " + lambdaService.countOutsideAgeRange(20, 40);
                case 8 -> "Количество: " + lambdaService.countByBeardAndHair(BeardStyle.SHORT, HairColor.Blond);
                case 9 -> "Количество: " + lambdaService.countByBeardAndHair(BeardStyle.LONG, HairColor.Red);
                case 10 -> "Количество: " + lambdaService.countWithOneOrTwoAxes();
                case 11 -> lambdaService.getRandomVikingTallerThan180()
                        .map(v -> "Викинг: " + v.name() + " (" + v.heightCm() + " см)")
                        .orElse("Не найден");
                case 12 -> {
                    List<Viking> list = lambdaService.getVikingsWithLegendaryEquipment();
                    yield list.isEmpty() ? "Не найдены" : list.stream().map(Viking::name).collect(Collectors.joining(", "));
                }
                case 13 -> {
                    List<Viking> list = lambdaService.getSortedRedBeardedVikings();
                    yield list.isEmpty() ? "Не найдены" : list.stream().map(v -> v.name() + " (" + v.age() + ")").collect(Collectors.joining(", "));
                }
                case 14 -> "ID: " + lambdaService.findMaxId(lambdaService.getAllIdsFromDb());
                case 15 -> {
                    List<Integer> list = lambdaService.findAllEvenIds(lambdaService.getAllIdsFromDb());
                    yield list.isEmpty() ? "Нет четных ID" : list.stream().map(String::valueOf).collect(Collectors.joining(", "));
                }
                default -> "Нет данных";
            };

            JOptionPane.showMessageDialog(dialog, "Результат: " + result);
            dialog.dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.add(confirm);

        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
}
