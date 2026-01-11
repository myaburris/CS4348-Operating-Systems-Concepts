import java.util.Random;
import java.util.concurrent.Semaphore;

public class Waiter extends Thread {
    private int waiterId;
    private Semaphore semaphore;
    private Table table;
    private Kitchen kitchen;

    public Waiter(int waiterId, Kitchen kitchen) {  // Initialize the waiter
        this.waiterId = waiterId;
        this.kitchen = kitchen;
        semaphore = new Semaphore(0);
    }

    public void setTable(Table table) { // Initiaize the waiter's table
        this.table = table; 
    }
    // Signal is recieved from a customer that sat on the table it is servicing. This means waiter has to move and get an order
    public void signalOrder() {
        semaphore.release();
    }

    // Entry point of the waiter to execute independently
    @Override
    public void run() {
        try {
            while (true) {
                semaphore.acquire(); //Wait for a customer from the table it is assigned
                Customer customer = table.serveNextCustomer(); //Service the next customer from the table

                if (customer == null) { // If there are no customer to serve next, then stop now
                    break;
                }
                System.out.println(this + " is now serving " + customer); // Use the kitchen
                kitchen.use(this); 

                System.out.println(this + " is waiting outside kitchen for order to be ready..."); // Wait for the food outside the kitchen to get the order
                Random random = new Random();
                Thread.sleep(random.nextInt(1000) + 300);

                System.out.println(this + " will now go back to kitchen to get customer's order."); // Go back to the kitchen to grab food
                kitchen.use(this);
        
                customer.setServed(true);
                System.out.println(this + " has served " + customer);
        
                customer.signal();
        
                // Process next customer next after
            }
            System.out.println(this + " left the restaurant");
        } catch (Exception e) {
        e.printStackTrace(System.out);
    }
}
    // Return a string represenation of a waiter
    @Override
    public String toString() {
        return "Waiter " + waiterId;
    }
}

