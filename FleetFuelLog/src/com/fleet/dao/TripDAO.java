package com.fleet.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fleet.bean.Trip;
import com.fleet.util.DBUtil;



public class TripDAO {
	public int generateTripID() {
		Connection connection= DBUtil.getDBConnection();
 	   String query="select TRIP_seq.NEXTVAL from dual";
 	   try {
 		   PreparedStatement ps=connection.prepareStatement(query);
     	   ResultSet rs=ps.executeQuery();
     	   rs.next();
     	   int seqNumber=rs.getInt(1);
     	   return seqNumber;
     	   }
 	   catch(SQLException e) {
 		   e.printStackTrace();
 		   return 0;
     	   }
	}
	public boolean recordTrip(Trip t) {
		Connection connection = DBUtil.getDBConnection();
	    String query = "INSERT INTO TRIP_TBL VALUES (?,?,?,?,?)";

	    try {
	        PreparedStatement ps = connection.prepareStatement(query);
	        ps.setInt(1, t.getTripID());
            ps.setString(2, t.getVehicleID());
            Date d=new Date(t.getTripDate().getTime());
            ps.setDate(3,d);
            ps.setDouble(4, t.getDistanceTraveled());
            ps.setDouble(5, t.getFuelConsumed());

	        int rowsInserted = ps.executeUpdate();
	        if (rowsInserted > 0) {
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	public List<Trip> findTripsByVehicle(String vehicleID)	{
		List<Trip> trips = new ArrayList<>();
		Connection connection= DBUtil.getDBConnection();
 	   String query="SELECT * from TRIP_TBL where VEHICLE_ID=?";
 	   try {
 	   PreparedStatement ps=connection.prepareStatement(query);
 	   ps.setString(1, vehicleID);
 	   ResultSet rs=ps.executeQuery();
 	  while (rs.next()) {
          Trip t = new Trip();
          t.setTripID(rs.getInt("TRIP_ID"));
          t.setVehicleID(rs.getString("VEHICLE_ID"));
          t.setTripDate(rs.getDate("TRIP_DATE"));
          t.setDistanceTraveled(rs.getDouble("DISTANCE"));
          t.setFuelConsumed(rs.getDouble("FUEL_CONSUMED"));
          trips.add(t);
 	 }
 	   }
 	   catch(SQLException e) {
 		   e.printStackTrace();	   
 	   }
 	   return trips;
    }
		
	}

