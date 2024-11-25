package main;

public class Customer {
    private int queueNumber;
    private String name;
    private String parcelID;
    public Customer(int queueNumber, String name, String parcelID) {
        this.queueNumber = queueNumber;
        this.name = name;
        this.parcelID = parcelID;
    }
    public int getQueueNumber() {
        return queueNumber;
    }
    public String getName() {
        return name;
    }
    public String getParcelID() {
        return parcelID;
    }
    @Override
    public String toString() {
        return "Queue Number: " + queueNumber + ", Name: " + name + ", Package ID: " + parcelID;
    }
}
