import java.util.concurrent.Semaphore;

public class Cashier{
    private Semaphore semaphore;

    public Cashier() { // Initialize the cashier
        semaphore = new Semaphore(1);
    }

    public void acceptPayment(Customer customer) { // Accept customer's payment
        try {
            semaphore.acquire();
            System.out.println(customer + " paid the bill");
            semaphore.release();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
