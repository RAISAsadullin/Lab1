public interface Delivery {
    String getName();
    double getCost();
    int getDeadlineDays();
    boolean requiresCourier();
    void deliver(Order order);
}
