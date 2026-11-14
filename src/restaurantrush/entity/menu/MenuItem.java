package restaurantrush.entity.menu;

public abstract class MenuItem {
    private final String name;
    private final int price;
    private final int preparationUnit;

    public MenuItem(String name, int price, int preparationUnit) {
        this.name = name;
        this.price = price;
        this.preparationUnit = preparationUnit;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getPreparationUnit() {
        return preparationUnit;
    }
}