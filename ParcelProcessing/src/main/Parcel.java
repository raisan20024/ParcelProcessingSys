package main;
public class Parcel {
    private String parcelID;
    private int daysInDepot;
    private double weight;
    private double length;
    private double width;
    private double height;
    private boolean isCollected;
    public Parcel(String parcelID, int daysInDepot, double weight, double length, double width, double height) {
        this.parcelID = parcelID;
        this.daysInDepot = daysInDepot;
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.isCollected = false; 
    }
    public String getParcelID() {
        return parcelID;
    }
    public int getDaysInDepot() {
        return daysInDepot;
    }
    public double getWeight() {
        return weight;
    }
    public double getLength() {
        return length;
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }
    public boolean isCollected() {
        return isCollected;
    }
    public void setCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }
    public double calculateVolume() {
        return length * width * height;
    }
    @Override
    public String toString() {
        return "Pacrcel ID: " + parcelID + ", Days in Depot: " + daysInDepot + ", Weight: " + weight + "kg, Dimensions: " 
               + length + "x" + width + "x" + height + " meters, Collected: " + (isCollected ? "Yes" : "No");
    }
}
