
package com.fleet.dao;

import com.fleet.bean.FuelExpense;
import com.fleet.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuelExpenseDAO {
    public int generateExpenseID() {
        int expenseID = 0;
        String query = "SELECT fuel_exp_seq.NEXTVAL FROM dual"; 
        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                expenseID = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenseID;
    }
    public boolean recordFuelExpense(FuelExpense fe) {
        boolean status = false;
        String query = "INSERT INTO FUEL_EXPENSE_TBL (EXPENSE_ID, VEHICLE_ID, FUEL_VOLUME, COST, PURCHASE_DATE, STATION_NAME) VALUES (?,?,?,?,?,?)";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, fe.getExpenseID());
            ps.setString(2, fe.getVehicleID());
            ps.setDouble(3, fe.getFuelVolume());
            ps.setDouble(4, fe.getCost());
            ps.setDate(5, new java.sql.Date(fe.getPurchaseDate().getTime()));
            ps.setString(6, fe.getStationName());

            int rowsInserted = ps.executeUpdate();
            status = rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }
    public List<FuelExpense> findExpensesByVehicle(String vehicleID) {
        List<FuelExpense> expenses = new ArrayList<>();
        String query = "SELECT * FROM FUEL_EXPENSE_TBL WHERE VEHICLE_ID = ? ORDER BY PURCHASE_DATE";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, vehicleID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FuelExpense fe = new FuelExpense();
                    fe.setExpenseID(rs.getInt("EXPENSE_ID"));
                    fe.setVehicleID(rs.getString("VEHICLE_ID"));
                    fe.setFuelVolume(rs.getDouble("FUEL_VOLUME"));
                    fe.setCost(rs.getDouble("COST"));
                    fe.setPurchaseDate(rs.getDate("PURCHASE_DATE"));
                    fe.setStationName(rs.getString("STATION_NAME"));
                    expenses.add(fe);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }
}
