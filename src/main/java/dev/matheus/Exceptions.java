package dev.matheus;

public class Exceptions {
    public static class CustomerNotFoundException extends RuntimeException {
        public CustomerNotFoundException(String customerName, String customerSurname) {
            super("Customer not found: " + customerName + " " + customerSurname);
        }
    }
}
