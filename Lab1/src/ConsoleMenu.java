import java.util.Scanner;

public class ConsoleMenu {
    private Scanner scanner;

    public ConsoleMenu() {
        scanner = new Scanner(System.in);
    }

    public void showMenu() {
        System.out.println("Служба доставки");
        System.out.println("CREATE: Создать заказ");
        System.out.println("SHOW: Показать список заказов");
        System.out.println("EDIT: Изменить заказ");
        System.out.println("START: Начать доставку и завершить программу");
        System.out.println("EXIT: Выйти из программы");
    }

    public String readChoice() {
        System.out.print("Выберите действие: ");
        return scanner.nextLine().trim();
    }

    public String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            String value = readText(prompt);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            String value = readText(prompt);
            try {
                double number = Double.parseDouble(value);
                if (Double.isFinite(number)) {
                    return number;
                }
                System.out.println("Введите корректное число.");
            } catch (NumberFormatException e) {
                System.out.println("Введите число, например 15.5.");
            }
        }
    }

    public Delivery readDeliveryMethod() {
        while (true) {
            System.out.println("Выберите способ доставки:");
            System.out.println("1. Стандартная доставка курьером, стоимость 5.0, срок послезавтра");
            System.out.println("2. Экспресс-доставка, стоимость 12.0, срок сегодня");
            System.out.println("3. Самовывоз, стоимость 0.0, готовность завтра");
            String choice = readText("Номер способа доставки: ");

            if (choice.equals("1")) {
                return new CourierDelivery();
            }
            if (choice.equals("2")) {
                return new ExpressDelivery();
            }
            if (choice.equals("3")) {
                return new SelfDelivery();
            }
            System.out.println("Такого способа доставки нет. Выберите 1, 2 или 3.");
        }
    }

    public void createOrder(OrderService orderService) {
        String clientName = readText("Имя клиента: ");
        String clientPhone = readText("Телефон клиента: ");
        String address = readText("Адрес доставки или пункт самовывоза: ");
        Delivery delivery = readDeliveryMethod();

        Order order = orderService.createOrder(clientName, clientPhone, address, delivery);

        boolean addMore = true;
        while (addMore) {
            String productName = readText("Название товара: ");
            int quantity = readPositiveInt("Количество: ");
            double price = readNonNegativeDouble("Цена за штуку: ");
            order.addItem(new OrderItem(productName, quantity, price));

            String answer = readText("Добавить ещё товар? (ДА/НЕТ): ");
            addMore = answer.equalsIgnoreCase("ДА") || answer.equalsIgnoreCase("YES");
        }

        if (delivery.requiresCourier()) {
            String courierName = readText("Имя курьера: ");
            String courierPhone = readText("Телефон курьера: ");
            order.assignCourier(new Courier(courierName, courierPhone));
        }

        System.out.println("Заказ создан.");
        orderService.showOrderDetails(order);
    }

    public void editOrder(OrderService orderService) {
        int id = readPositiveInt("Введите номер заказа: ");
        Order order = orderService.findOrderById(id);

        if (order == null) {
            System.out.println("Заказ не найден.");
            return;
        }
        if (order.getStatus() != OrderStatus.NEW) {
            System.out.println("Изменять можно только заказ, доставка которого ещё не началась.");
            return;
        }

        boolean editing = true;
        while (editing) {
            System.out.println("Что хотите изменить?");
            System.out.println("1. Данные клиента");
            System.out.println("2. Адрес доставки");
            System.out.println("3. Способ доставки и курьер");
            System.out.println("4. Добавить товар");
            System.out.println("5. Удалить товар");
            System.out.println("6. Изменить товар");
            System.out.println("0. Закончить изменение");
            int choice = readInt("Выберите действие: ");

            try {
                if (choice == 1) {
                    String name = readText("Новое имя клиента: ");
                    String phone = readText("Новый телефон клиента: ");
                    order.changeClient(name, phone);
                } else if (choice == 2) {
                    String address = readText("Новый адрес доставки: ");
                    order.changeDeliveryAddress(address);
                } else if (choice == 3) {
                    Delivery delivery = readDeliveryMethod();
                    order.changeDeliveryMethod(delivery);
                    if (delivery.requiresCourier()) {
                        String courierName = readText("Имя курьера: ");
                        String courierPhone = readText("Телефон курьера: ");
                        order.assignCourier(new Courier(courierName, courierPhone));
                    }
                } else if (choice == 4) {
                    String productName = readText("Название товара: ");
                    int quantity = readPositiveInt("Количество: ");
                    double price = readNonNegativeDouble("Цена за штуку: ");
                    order.addItem(new OrderItem(productName, quantity, price));
                } else if (choice == 5) {
                    orderService.showOrderDetails(order);
                    int itemNumber = readPositiveInt("Номер товара для удаления: ");
                    order.removeItem(itemNumber);
                } else if (choice == 6) {
                    orderService.showOrderDetails(order);
                    int itemNumber = readPositiveInt("Номер товара для изменения: ");
                    String productName = readText("Новое название товара: ");
                    int quantity = readPositiveInt("Новое количество: ");
                    double price = readNonNegativeDouble("Новая цена за штуку: ");
                    order.replaceItem(itemNumber, new OrderItem(productName, quantity, price));
                } else if (choice == 0) {
                    editing = false;
                } else {
                    System.out.println("Такого действия нет.");
                }

                if (editing) {
                    orderService.showOrderDetails(order);
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Изменение не выполнено: " + e.getMessage());
            }
        }
    }

    public boolean startDelivery(OrderService orderService) {
        int id = readPositiveInt("Введите номер заказа для начала доставки: ");
        Order order = orderService.findOrderById(id);

        if (order == null) {
            System.out.println("Заказ не найден.");
            return false;
        }

        try {
            order.startDelivery();
            order.performDelivery();
            Delivery delivery = order.getDelivery();

            System.out.println("Статус заказа: " + order.getStatus());
            System.out.println("Стоимость товаров: " + order.getTotalPrice());
            System.out.println("Стоимость доставки: " + delivery.getCost());
            System.out.println("Итоговая стоимость: " + order.getFinalPrice());

            String deadline = deadlineText(delivery.getDeadlineDays());
            if (delivery.requiresCourier()) {
                System.out.println("Заказ едет к вам. Ожидайте его " + deadline + ".");
            } else {
                System.out.println("Заказ будет готов к самовывозу " + deadline + ".");
            }
            System.out.println("Программа завершает работу.");
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Не удалось начать доставку: " + e.getMessage());
            return false;
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Введите число больше нуля.");
        }
    }

    private double readNonNegativeDouble(String prompt) {
        while (true) {
            double value = readDouble(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("Число не может быть отрицательным.");
        }
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