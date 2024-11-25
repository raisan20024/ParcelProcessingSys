package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Manager {
    private QueueOfCustomers customerQueue;
    private ParcelMap parcelMap;
    private Worker worker;
    private Log log;

    // Constructor
    public Manager() {
        this.customerQueue = new QueueOfCustomers();
        this.parcelMap = new ParcelMap();
        this.worker = new Worker(parcelMap);
        this.log = Log.getInstance();
    }

    // Method to initialize parcels from a file
    public void initializeParcels(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String parcelID = parts[0];
                int daysInDepot = Integer.parseInt(parts[1]);
                double weight = Double.parseDouble(parts[2]);
                double length = Double.parseDouble(parts[3]);
                double width = Double.parseDouble(parts[4]);
                double height = Double.parseDouble(parts[5]);

                Parcel parcel = new Parcel(parcelID, daysInDepot, weight, length, width, height);
                parcelMap.addParcel(parcel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to initialize customers from a file
    public void initializeCustomers(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int queueNumber = Integer.parseInt(parts[0]);
                String name = parts[1];
                String parcelID = parts[2];

                Customer customer = new Customer(queueNumber, name, parcelID);
                customerQueue.addCustomer(customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to start processing customers
    public void startProcessing() {
        while (!customerQueue.isEmpty()) {
            Customer customer = customerQueue.removeCustomer();
            worker.processCustomer(customer);
        }
        log.writeLogToFile("log.txt");
    }

    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.initializeParcels("parcels.txt"); // Ensure the file is in the working directory
        manager.initializeCustomers("clients.txt"); // Ensure the file is in the working directory
        manager.startProcessing();
    }
}
