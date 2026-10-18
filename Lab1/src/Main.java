public class Main {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        ConsoleMenu menu = new ConsoleMenu();
        boolean running = true;

        while (running) {
            menu.showMenu();
            String choice = menu.readChoice();

            try {
                if (choice.equalsIgnoreCase("CREATE")) {
                    menu.createOrder(orderService);
                } else if (choice.equalsIgnoreCase("SHOW")) {
                    orderService.showOrders();
                } else if (choice.equalsIgnoreCase("EDIT")) {
                    menu.editOrder(orderService);
                } else if (choice.equalsIgnoreCase("START")) {
                    if (menu.startDelivery(orderService)) {
                        running = false;
                    }
                } else if (choice.equalsIgnoreCase("EXIT")) {
                    System.out.println("Работа программы завершена.");
                    running = false;
                } else {
                    System.out.println("Неизвестная команда. Выберите CREATE, SHOW, EDIT, START или EXIT.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Не удалось выполнить действие: " + e.getMessage());
            }
        }
    }
}