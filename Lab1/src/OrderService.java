import java.util.ArrayList;

public class OrderService {
    private ArrayList<Order> orders = new ArrayList<>();
    private int nextOrderId = 1;

    public Order createOrder(String clientName, String clientPhone,
                             String address, Delivery delivery) {
        Client client = new Client(clientName, clientPhone);
        Order order = new Order(nextOrderId, client, address, delivery);
        orders.add(order);
        nextOrderId++;
        return order;
    }

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Заказ не может быть пустым.");
        }
        orders.add(order);
        if (order.getId() >= nextOrderId) {
            nextOrderId = order.getId() + 1;
        }
    }

    public void showOrders() {
        if (orders.isEmpty()) {
            System.out.println("Заказов пока нет.");
            return;
        }

        for (Order order : orders) {
            showOrderDetails(order);
        }
    }

    public Order findOrderById(int id) {
        for (Order order : orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    public void showOrderDetails(Order order) {
        if (order == null) {
            System.out.println("Заказ не найден.");
            return;
        }

        System.out.println("Заказ №" + order.getId());
        System.out.println("Клиент: " + order.getClient().getName());
        System.out.println("Телефон клиента: " + order.getClient().getPhone());
        System.out.println("Адрес доставки: " + order.getDeliveryAddress());
        System.out.println("Статус: " + order.getStatus());
        System.out.println("Товары:");

        int itemNumber = 1;
        for (OrderItem item : order.getItems()) {
            System.out.println(itemNumber + ". " + item.getProductName()
                    + ", количество: " + item.getQuantity()
                    + ", цена за штуку: " + item.getPrice()
                    + ", сумма: " + item.getTotalPrice());
            itemNumber++;
        }

        Delivery delivery = order.getDelivery();
        System.out.println("Способ доставки: " + delivery.getName());
        System.out.println("Стоимость товаров: " + order.getTotalPrice());
        System.out.println("Стоимость доставки: " + delivery.getCost());
        if (delivery.requiresCourier()) {
            System.out.println("Срок доставки: " + deadlineText(delivery.getDeadlineDays()));
        } else {
            System.out.println("Заказ будет готов к самовывозу: " + deadlineText(delivery.getDeadlineDays()));
        }

        if (order.getCourier() != null) {
            System.out.println("Курьер: " + order.getCourier().getName());
            System.out.println("Телефон курьера: " + order.getCourier().getPhone());
        } else {
            System.out.println("Курьер не требуется.");
        }
        System.out.println("Итоговая стоимость: " + order.getFinalPrice());
    }

    private String deadlineText(int days) {
        if (days <= 0) {
            return "сегодня";
        }
        if (days == 1) {
            return "завтра";
        }
        return "послезавтра";
    }
}
