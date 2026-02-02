package com.fleet.service;

import com.fleet.bean.Vehicle;
import com.fleet.bean.Trip;
import com.fleet.bean.FuelExpense;
import com.fleet.dao.VehicleDAO;
import com.fleet.dao.TripDAO;
import com.fleet.dao.FuelExpenseDAO;

import java.util.Date;
import java.util.List;

public class FleetService {

    private VehicleDAO vehicleDAO = new VehicleDAO();
    private TripDAO tripDAO = new TripDAO();
    private FuelExpenseDAO fuelDAO = new FuelExpenseDAO();
    public String viewVehicleDetails(String vehicleID) {
        Vehicle vehicle = vehicleDAO.findVehicle(vehicleID);
        if (vehicle == null) return "VEHICLE NOT FOUND";

        return "ID: " + vehicle.getVehicleID() +
               ", Model: " + vehicle.getModel() +
               ", Category: " + vehicle.getCategory() +
               ", Tank: " + vehicle.getTankCapacity() +
               ", Distance: " + vehicle.getTotalDistance() +
               ", Fuel Used: " + vehicle.getTotalFuelUsed();
    }
    public boolean addNewVehicle(Vehicle v) {
        if (v == null) return false;
        if (v.getVehicleID() == null || v.getVehicleID().isEmpty()) return false;
        if (v.getModel() == null || v.getModel().isEmpty()) return false;
        if (v.getTankCapacity() <= 0) return false;

        return vehicleDAO.insertVehicle(v);
    }
    public boolean removeVehicle(String vehicleID) {
        Vehicle vehicle = vehicleDAO.findVehicle(vehicleID);
        if (vehicle == null) return false;

        List<Trip> trips = tripDAO.findTripsByVehicle(vehicleID);
        List<FuelExpense> expenses = fuelDAO.findExpensesByVehicle(vehicleID);

        if (!trips.isEmpty() || !expenses.isEmpty()) return false;

        return vehicleDAO.deleteVehicle(vehicleID);
    }
    public boolean logTrip(String vehicleID, Date tripDate, double distance, double fuelConsumed) {
        if (distance <= 0 || fuelConsumed <= 0) return false;

        Vehicle vehicle = vehicleDAO.findVehicle(vehicleID);
        if (vehicle == null) return false;

        if (fuelConsumed > vehicle.getTankCapacity()) return false;

        Trip t = new Trip();
        t.setTripID(tripDAO.generateTripID());
        t.setVehicleID(vehicleID);
        t.setTripDate(new java.sql.Date(tripDate.getTime())); // Convert to sql.Date
        t.setDistanceTraveled(distance);
        t.setFuelConsumed(fuelConsumed);

        boolean tripInserted = tripDAO.recordTrip(t);
        boolean mileageUpdated = vehicleDAO.updateMileage(vehicleID,
                vehicle.getTotalDistance() + distance,
                vehicle.getTotalFuelUsed() + fuelConsumed);

        return tripInserted && mileageUpdated;
    }
    public boolean recordFuelPurchase(String vehicleID, double fuelVolume, double cost, Date purchaseDate, String stationName) {
        if (fuelVolume <= 0 || cost <= 0) return false;

        Vehicle vehicle = vehicleDAO.findVehicle(vehicleID);
        if (vehicle == null) return false;

        if (fuelVolume > vehicle.getTankCapacity()) return false;

        FuelExpense fe = new FuelExpense();
        fe.setExpenseID(fuelDAO.generateExpenseID());
        fe.setVehicleID(vehicleID);
        fe.setFuelVolume(fuelVolume);
        fe.setCost(cost);
        fe.setPurchaseDate(new java.sql.Date(purchaseDate.getTime())); // Convert to sql.Date
        fe.setStationName(stationName);

        boolean expenseInserted = fuelDAO.recordFuelExpense(fe);
        boolean fuelUpdated = vehicleDAO.updateMileage(vehicleID,
                vehicle.getTotalDistance(),
                vehicle.getTotalFuelUsed() + fuelVolume);

        return expenseInserted && fuelUpdated;
    }
    public double calculateFuelEfficiency(String vehicleID) {
        Vehicle vehicle = vehicleDAO.findVehicle(vehicleID);
        if (vehicle == null) return 0.0;
        if (vehicle.getTotalFuelUsed() == 0) return 0.0;

        return vehicle.getTotalDistance() / vehicle.getTotalFuelUsed();
    }
}
