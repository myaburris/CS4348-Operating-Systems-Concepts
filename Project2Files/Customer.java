import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Customer extends Thread {

    private int customerId;
    private List<Table> tables;
    private List<Door> doors;
    private Cashier cashier;

    private Semaphore semaphore;
    private boolean served;

    // Initialize the customer
    public Customer(int customerId, List<Table> tables, List<Door> doors, Cashier cashier) {
        this.customerId = customerId;
        this.tables = tables;
        this.doors = doors;
        this.cashier = cashier;
        served = false;

        semaphore = new Semaphore(0); // This will make customer wait to be served
    }

    @Override  // Return a string representation
    public String toString() {
        return "Customer " + customerId;
    }

    public boolean isServed(){ //Check is customer has been served
        return served;
    }
    public void setServed(boolean served) {  // Mark customer as served
        this.served = served;
    }
    public void signal() { // Signal the customer to stop waiting
        semaphore.release();
    }

    // Start customer, choose the best table, wait to get served and then pay afterwards
    @Override
    public void run() {
        try {
            Random random = new Random();
            Door chosenDoor = doors.get(random.nextInt(doors.size())); // Customer enters one of the doors
            chosenDoor.enter(this);
            Table chosenTable = tables.get(random.nextInt(tables.size())); // Look for the best table
            System.out.println(this + " initially chooses " + chosenTable);
    
            if (chosenTable.getQueueSize() > 7) {
                for (Table table : tables) { // If line is long, look for another table that is shorter
                    if (table != chosenTable && table.getQueueSize() < chosenTable.getQueueSize()) {
                        chosenTable = table;
                    }
                }
                System.out.println(this + " chooses " + chosenTable + " as backup because initial table has long queue");
            }
    
            chosenTable.addCustomer(this); //Go to the chosen table, and wait to be served
            semaphore.acquire();

            System.out.println(this + " is now eating"); //Once food arrives starts eating
            Thread.sleep(random.nextInt(1000) + 200);
            System.out.println(this + " is finished eating");

            chosenTable.removeSeatedCustomer(this); // Done eating, then leave the table
            cashier.acceptPayment(this); // Pay for the bill
            System.out.println(this + " leaves restaurant"); // Customer leaves the restaurant
            Main.signalCustomerExit();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}

   