import java.util.ArrayList;

public class Order {
    private int id;
    private Client client;
    private Courier courier;
    private String deliveryAddress;
    private Delivery delivery;
    private OrderStatus status;
    private ArrayList<OrderItem> items;

    public Order(int id, Client client, String deliveryAddress, Delivery delivery) {
        if (id <= 0) {
            throw new IllegalArgumentException("Номер заказа должен быть положительным.");
        }
        if (client == null) {
            throw new IllegalArgumentException("Не указан клиент.");
        }
        if (deliveryAddress == null || deliveryAddress.isBlank()) {
            throw new IllegalArgumentException("Введите адрес доставки.");
        }
        if (delivery == null) {
            throw new IllegalArgumentException("Выберите способ доставки.");
        }

        this.id = id;
        this.client = client;
        this.deliveryAddress = deliveryAddress.trim();
        this.delivery = delivery;
        this.courier = null;
        this.status = OrderStatus.NEW;
        this.items = new ArrayList<>();
    }

    public void addItem(OrderItem item) {
        ensureEditable();
        if (item == null) {
            throw new IllegalArgumentException("Товар не может быть пустым.");
        }
        items.add(item);
    }

    public void removeItem(int itemNumber) {
        ensureEditable();
        if (itemNumber < 1 || itemNumber > items.size()) {
            throw new IllegalArgumentException("Нет товара с таким номером.");
        }
        items.remove(itemNumber - 1);
    }

    public void replaceItem(int itemNumber, OrderItem item) {
        ensureEditable();
        if (itemNumber < 1 || itemNumber > items.size()) {
            throw new IllegalArgumentException("Нет товара с таким номером.");
        }
        if (item == null) {
            throw new IllegalArgumentException("Товар не может быть пустым.");
        }
        items.set(itemNumber - 1, item);
    }

    public void changeClient(String name, String phone) {
        ensureEditable();
        this.client = new Client(name, phone);
    }

    public void changeDeliveryAddress(String deliveryAddress) {
        ensureEditable();
        if (deliveryAddress == null || deliveryAddress.isBlank()) {
            throw new IllegalArgumentException("Введите адрес доставки.");
        }
        this.deliveryAddress = deliveryAddress.trim();
    }

    public void changeDeliveryMethod(Delivery delivery) {
        ensureEditable();
        if (delivery == null) {
            throw new IllegalArgumentException("Выберите способ доставки.");
        }
        this.delivery = delivery;
        if (!delivery.requiresCourier()) {
            this.courier = null;
        }
    }

    public void assignCourier(Courier courier) {
        ensureEditable();
        if (!delivery.requiresCourier()) {
            throw new IllegalStateException("Для самовывоза курьер не требуется.");
        }
        if (courier == null) {
            throw new IllegalArgumentException("Необходимо выбрать курьера.");
        }
        this.courier = courier;
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Courier getCourier() {
        return courier;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public ArrayList<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public void startDelivery() {
        if (status != OrderStatus.NEW) {
            throw new IllegalStateException("Начать доставку можно только для нового заказа.");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("В заказе должен быть хотя бы один товар.");
        }
        if (delivery.requiresCourier() && courier == null) {
            throw new IllegalStateException("Для этого способа доставки необходимо назначить курьера.");
        }
        status = OrderStatus.IN_PROGRESS;
    }

    public void performDelivery() {
        if (status != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("Сначала необходимо начать доставку.");
        }
        delivery.deliver(this);
    }

    public void deliver() {
        if (status == OrderStatus.IN_PROGRESS) {
            status = OrderStatus.DELIVERED;
        }
    }

    public void cancel() {
        if (status == OrderStatus.NEW || status == OrderStatus.IN_PROGRESS) {
            status = OrderStatus.CANCELLED;
        }
    }

    public double getTotalPrice() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public double getFinalPrice() {
        return getTotalPrice() + delivery.getCost();
    }

    private void ensureEditable() {
        if (status != OrderStatus.NEW) {
            throw new IllegalStateException("Заказ нельзя изменить после начала доставки.");
        }
    }
}