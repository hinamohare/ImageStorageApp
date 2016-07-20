/**
 * This class performs the registration for the new user
 * It takes the userName, user emailID and password and creates an account for that user
 * **/

package executors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUp {
	public static boolean register(String name, String email, String pass) {
		boolean status = false;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String driver = "com.mysql.jdbc.Driver";
           
        try {
        	 Class.forName(driver).newInstance();
        	pst = (PreparedStatement)DatabaseHelper.GetPrepareStatement(DatabaseHelper.CreateNewUserAccount);
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, pass);

            pst.executeUpdate();
            
            status = true;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return status;
	}

}
