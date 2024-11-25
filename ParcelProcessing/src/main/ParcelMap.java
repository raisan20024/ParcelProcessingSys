package main;

import java.util.HashMap;
import java.util.Map;

public class ParcelMap {
    private Map<String, Parcel> parcels;
    public ParcelMap() {
        this.parcels = new HashMap<>();
    }
    public void addParcel(Parcel parcel) {
        parcels.put(parcel.getParcelID(), parcel);
    }
    public Parcel getParcel(String parcelID) {
        return parcels.get(parcelID);
    }
    public Parcel removeParcel(String parcelID) {
        return parcels.remove(parcelID);
    }
    public boolean containsParcel(String parcelID) {
        return parcels.containsKey(parcelID);
    }
    public int getParcelCount() {
        return parcels.size();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Parcel parcel : parcels.values()) {
            sb.append(parcel).append("\n");
        }
        return sb.toString();
    }
}
