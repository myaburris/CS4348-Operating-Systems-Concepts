import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Table {
    private String name;
    private Waiter waiter; // Waiter that will service this table

    private Semaphore semaphore; // Semaphore to protect the seats and queue

    private List<Customer> seats;
    private Queue<Customer> queue;

    public Table(String name) {  //Initialize the table
        this.name = name;
        seats = new ArrayList<>();
        queue = new LinkedList<>();
        semaphore = new Semaphore(1);
    }

    // A table is assigned with a waiter that will service customers who is on that table
    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }
    public int getQueueSize() { // Return how many customers are in the queue
        return queue.size();
    }
    public Customer serveNextCustomer() { // This will serve the next customer that is seating
        // Get the customer that is not yet served
        try {
            for (Customer customer : seats) {
                if (!customer.isServed()) {
                    return customer;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    // Remove the seated customer from the table then if there are customers in line then let them seat
    public void removeSeatedCustomer(Customer customer) {
        try {
            semaphore.acquire();
            seats.remove(customer);
            System.out.println(customer + " is done with " + this);

            if(!queue.isEmpty()) {
                Customer nextCustomer = queue.poll();
                seats.add(nextCustomer);
                System.out.println(nextCustomer + " sat on " + this);
                waiter.signalOrder(); // Signal the waiter that there's a customer seatings
            }
            semaphore.release();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
    // Customer will go the queue, if the queue is empty then it will go to the seat right away
    public void addCustomer(Customer customer) {
        try {
            semaphore.acquire();
            if (seats.size() < 4) {
                seats.add(customer);
                System.out.println(customer + " sat on " + this);
                waiter.signalOrder(); // Signal the waiter that there's a customer seating
            } else {
                queue.add(customer);
                System.out.println(customer + " lined up for " + this);
            }
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        // Return a string reprsentation of the table
        @Override
        public String toString(){
            return "Table " + name;
        }
}










