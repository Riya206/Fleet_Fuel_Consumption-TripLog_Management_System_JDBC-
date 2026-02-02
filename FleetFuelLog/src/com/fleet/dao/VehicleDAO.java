package com.fleet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.fleet.bean.Vehicle;
import com.fleet.util.DBUtil;

public class VehicleDAO {
	public Vehicle findVehicle(String vehicleID) {
		Connection connection=DBUtil.getDBConnection();
		String query="Select * From VEHICLE_TBL where VEHICLE_ID=?";
		Vehicle v = null;
				 try {
			    	   PreparedStatement ps=connection.prepareStatement(query);
			    	   ps.setString(1, vehicleID);
			    	   ResultSet rs=ps.executeQuery();
			    	   if(rs.next()) {
			    		   v = new Vehicle();
			    		   v.setVehicleID(rs.getString(1));
			               v.setModel(rs.getString(2));
			               v.setCategory(rs.getString(3));
			               v.setTankCapacity(rs.getDouble(4));
			               v.setTotalDistance(rs.getDouble(5));
			               v.setTotalFuelUsed(rs.getDouble(6));
			    		   
			    	   }
			    	 }
			    	   catch(SQLException e) {
			    		   e.printStackTrace();	  
			       }
				return v;
		
	}


	public boolean insertVehicle(Vehicle v) {
		Connection connection=DBUtil.getDBConnection();
		String query="insert into VEHICLE_TBL values(?,?,?,?,?,?)";
				try {
					PreparedStatement ps=connection.prepareStatement(query);
					 ps.setString(1, v.getVehicleID());
			            ps.setString(2, v.getModel());
			            ps.setString(3, v.getCategory());
			            ps.setDouble(4, v.getTankCapacity());
			            ps.setDouble(5, 0);
			            ps.setDouble(6, 0);
			            int rowsUpdated = ps.executeUpdate();
		    	        if (rowsUpdated > 0) {
		    	            return true;  
		    	        } else {
		    	            return false; 
		    	        }
			            

				}
				catch(SQLException e){
					e.printStackTrace();
					
				}
				return false;
	
}

     public boolean updateMileage(String vehicleID,double newDistance,double newFuel) {
    	 Connection connection = DBUtil.getDBConnection();
 	    String query = "UPDATE VEHICLE_TBL SET TOTAL_DISTANCE = ? , TOTAL_FUEL_USED = ? WHERE VEHICLE_ID=?";

 	    try {
 	        PreparedStatement ps = connection.prepareStatement(query);
 	        ps.setDouble(1, newDistance);
 	        ps.setDouble(2, newFuel);
 	        ps.setString(3, vehicleID);
 	        int rowsUpdated = ps.executeUpdate();
 	        if (rowsUpdated > 0) {
 	            return true;  
 	        } else {
 	            return false; 
 	        }
 	    } catch (SQLException e) {
 	        e.printStackTrace();
 	    }
 	    return false; 
    }
     public boolean deleteVehicle(String vehicleID) {
    	 Connection connection = DBUtil.getDBConnection();
  	    String query = "DELETE FROM VEHICLE_TBL WHERE VEHICLE_ID=?";

  	    try {
  	        PreparedStatement ps = connection.prepareStatement(query);
  	        ps.setString(1, vehicleID);
  	        int rowsUpdated = ps.executeUpdate();
  	        if (rowsUpdated > 0) {
  	            return true;  
  	        } else {
  	            return false; 
  	        }
  	    } catch (SQLException e) {
  	        e.printStackTrace();
  	    }
  	    return false; 
     }
     }
     
		
	

