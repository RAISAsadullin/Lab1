public class ExpressDelivery implements Delivery {
    @Override
    public String getName() {
        return "Экспресс-доставка";
    }

    @Override
    public double getCost() {
        return 12.0;
    }

    @Override
    public int getDeadlineDays() {
        return 0;
    }

    @Override
    public boolean requiresCourier() {
        return true;
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Заказ отправлен экспресс-курьером.");
    }
}