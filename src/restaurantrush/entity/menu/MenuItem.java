package restaurantrush.entity.menu;

public abstract class MenuItem {
    protected String name;
    protected int price;
    protected int preparationUnit;

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