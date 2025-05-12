import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class View extends JFrame {

    private static final long serialVersionUID = 1L;
	private JTextField textField;
    private JTable table;
    private DefaultTableModel tableModel;
    private ShoppingListController controller;

    public View() {
        setTitle("Shopping List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);

        controller = new ShoppingListController(this);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(new Color(127, 96, 255));
        setContentPane(contentPane);

        contentPane.add(createTopPanel(), BorderLayout.NORTH);
        contentPane.add(createTablePanel(), BorderLayout.CENTER);
        contentPane.add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        textField = new JTextField();
        textField.addActionListener(e -> addItemFromTextField());
        panel.add(textField, BorderLayout.CENTER);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addItemFromTextField());
        panel.add(addButton, BorderLayout.EAST);

        return panel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(new Object[]{"ID", "Item"}, 0);
        table = new JTable(tableModel);
        table.setShowGrid(true);
        table.setGridColor(Color.GRAY);
        table.setFillsViewportHeight(true);

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);

        return new JScrollPane(table);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 5, 5));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) controller.deleteItemByIndex(row);
        });

        JButton cleanButton = new JButton("Clean List");
        cleanButton.addActionListener(e -> controller.cleanItems());

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> controller.saveItems());

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> {
            List<ShoppingItem> loaded = FileChooserHelper.loadItemsFromExcel(this);
            if (loaded != null) controller.setItems(loaded);
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(deleteButton);
        panel.add(cleanButton);
        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(exitButton);

        return panel;
    }

    private void addItemFromTextField() {
        String text = textField.getText().trim();
        if (!text.isEmpty()) {
            controller.addItem(text);
            textField.setText("");
            textField.requestFocusInWindow();
        }
    }

    public void updateTable(List<ShoppingItem> items) {
        tableModel.setRowCount(0);
        for (int i = 0; i < items.size(); i++) {
            tableModel.addRow(new Object[]{i + 1, items.get(i).getName()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new View().setVisible(true));
    }
}