public class ShoppingItem {
    private final String name;

    public ShoppingItem(String name) {
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }
}