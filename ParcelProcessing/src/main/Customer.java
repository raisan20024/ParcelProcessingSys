package main;

public class Customer {
    private int queueNumber;
    private String name;
    private String packageID;
    public Customer(int queueNumber, String name, String packageID) {
        this.queueNumber = queueNumber;
        this.name = name;
        this.packageID = packageID;
    }
    public int getQueueNumber() {
        return queueNumber;
    }
    public String getName() {
        return name;
    }
    public String getPackageID() {
        return packageID;
    }
    @Override
    public String toString() {
        return "Queue Number: " + queueNumber + ", Name: " + name + ", Package ID: " + packageID;
    }
}
