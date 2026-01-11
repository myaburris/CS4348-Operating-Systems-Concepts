import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main {

    private static final int NUM_CUSTOMERS = 40;

    // Semaphore which forces main class to wait until last customer exits restaurant
    private static Semaphore customerExitSemaphore = new Semaphore(1);
    private static int numCustomerExits = 0;

    private static Semaphore lastCustomerExitSemaphore = new Semaphore(0);

    public static void signalCustomerExit() { //add one to exit
        try {
            customerExitSemaphore.acquire();
            numCustomerExits++;
            //System.out.println("LEFT");

            if (numCustomerExits >= NUM_CUSTOMERS) {
                // Signal that the last customer has exited
                lastCustomerExitSemaphore.release();
                //System.out.println("LEFT");
            }

            customerExitSemaphore.release();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
    public static void main(String[] args) throws Exception { // Initialize the threads and start
       
        List<Table> tables = new ArrayList<>(); // 3 tables
        tables.add(new Table("A"));
        tables.add(new Table("B"));
        tables.add(new Table("C"));

        Kitchen kitchen = new Kitchen(); // There will be 1 kitchen shared by all waiters
    
        List<Waiter> waiters = new ArrayList<>(); // 3 waiters
        waiters.add(new Waiter(1, kitchen));
        waiters.add(new Waiter(2, kitchen));
        waiters.add(new Waiter(3, kitchen));

        tables.get(0).setWaiter(waiters.get(0)); //there will be one waiter assigned for each table
        tables.get(1).setWaiter(waiters.get(1));
        tables.get(2).setWaiter(waiters.get(2));

        waiters.get(0).setTable(tables.get(0));
        waiters.get(1).setTable(tables.get(1));
        waiters.get(2).setTable(tables.get(2));

        List<Door> doors = new ArrayList<>(); // There will be 2 doors only in the restaurant for entry of customers
        doors.add(new Door(1));
        doors.add(new Door(2));
        
        Cashier cashier = new Cashier(); // There will be 1 cashier for payment of customers

        List<Customer> customers = new ArrayList<>(); // There will be 40 customers

        for (int i = 1; i <= NUM_CUSTOMERS; i++) {
            customers.add(new Customer(i, tables, doors, cashier));
        }

        for (Waiter waiter : waiters) { // Start all waiter and customer threads
            waiter.start();
        }

        for (Customer customer : customers) {
            customer.start();
        }

        lastCustomerExitSemaphore.acquire(); // Wait for the last customer

        for (Waiter waiter : waiters) { // Signal all waiters to to stop waiting
            waiter.signalOrder();
        }
    }
}




       