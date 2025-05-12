import java.util.ArrayList;
import java.util.List;

public class ShoppingListController {
    private final List<ShoppingItem> items = new ArrayList<>();
    private final View view;

    public ShoppingListController(View view) {
        this.view = view;
    }

    public void addItem(String name) {
        if (name != null && !name.trim().isEmpty()) {
            items.add(new ShoppingItem(name));
            view.updateTable(items);
        }
    }

    public void deleteItemByIndex(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            view.updateTable(items);
        }
    }

    public void cleanItems() {
        items.clear();
        view.updateTable(items);
    }

    public void saveItems() {
        FileChooserHelper.saveItemsToExcel(items);
    }

    public void setItems(List<ShoppingItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        view.updateTable(items);
    }

    public List<ShoppingItem> getItems() {
        return new ArrayList<>(items); // bezpečná kópia
    }
}