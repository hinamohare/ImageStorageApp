package executors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
	
	public static int Validate(String name, String pass) {  	
		boolean status = false;
        int userID = -1;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String driver = "com.mysql.jdbc.Driver";
        
           
        try {
        	 Class.forName(driver).newInstance();
            pst = (PreparedStatement)DatabaseHelper.GetPrepareStatement(DatabaseHelper.ValidateLoginUser);
            pst.setString(1, name);
            pst.setString(2, pass);

            rs = pst.executeQuery();
            status = rs.next();
            
            if(status==false) //no matching record is present 
            {
            	return userID;
            }
            else
            {
            	userID = rs.getInt("userID");
            }
           
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                	System.out.println("Excetion from login : close conn: "+e.getMessage());
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                	System.out.println("Excetion from login : close pst: "+e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                	System.out.println("Excetion from login : close rs: "+e.getMessage());
                }
            }
        }
        return userID;
	}
	
	
}
