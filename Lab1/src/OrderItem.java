public class OrderItem {
    private String productName;
    private int quantity;
    private double price;

    public OrderItem(String productName, int quantity, double price) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Введите название товара.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше нуля.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной.");
        }
        this.productName = productName.trim();
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return quantity * price;
    }
}