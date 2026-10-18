public class Client {
    private String name;
    private String phone;

    public Client(String name, String phone) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Введите имя клиента.");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Введите телефон клиента.");
        }
        this.name = name.trim();
        this.phone = phone.trim();
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}