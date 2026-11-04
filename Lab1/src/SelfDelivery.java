public class SelfDelivery implements Delivery {
    @Override
    public String getName() {
        return "Самовывоз";
    }

    @Override
    public double getCost() {
        return 0.0;
    }

    @Override
    public int getDeadlineDays() {
        return 1;
    }

    @Override
    public boolean requiresCourier() {
        return false;
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Заказ подготовлен к самовывозу.");
    }
}
