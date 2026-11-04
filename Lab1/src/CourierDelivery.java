public class CourierDelivery implements Delivery {
    @Override
    public String getName() {
        return "Стандартная доставка курьером";
    }

    @Override
    public double getCost() {
        return 5.0;
    }

    @Override
    public int getDeadlineDays() {
        return 2;
    }

    @Override
    public boolean requiresCourier() {
        return true;
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Заказ отправлен стандартным курьером.");
    }
}

