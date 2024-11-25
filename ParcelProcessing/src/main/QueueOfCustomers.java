package main;

import java.util.LinkedList;
import java.util.Queue;

public class QueueOfCustomers {
    private Queue<Customer> clientQueue;
    public QueueOfCustomers() {
        this.clientQueue = new LinkedList<>();
    }
    public void addCustomer(Customer client) {
        clientQueue.offer(client);
    }
    public Customer removeCustomer() {
        return clientQueue.poll();
    }
    public Customer peekNextCustomer() {
        return clientQueue.peek();
    }
    public boolean isEmpty() {
        return clientQueue.isEmpty();
    }
    public int getSize() {
        return clientQueue.size();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Customer client : clientQueue) {
            sb.append(client).append("\n");
        }
        return sb.toString();
    }
}
