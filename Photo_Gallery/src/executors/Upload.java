/**
 *  This method is used to insert uploaded photos to database
**/

package executors;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.Statement;

public class Upload {

	public static int InsertPhoto(String photoname, InputStream thumbnailImageStream,InputStream originalImageStream, int userId) {

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		// Count shows the number of rows updated in database
		int Count = 0;
		try {

			
			conn = DatabaseHelper.GetDBConnection();
			Statement stSetLimit = conn.createStatement();
			String querySetLimit = "SET GLOBAL max_allowed_packet=4294967295;";
			stSetLimit.execute(querySetLimit);
			pst = DatabaseHelper.GetPrepareStatement(DatabaseHelper.InsertPhotoQuery,conn);
			pst.setString(1, photoname);
			pst.setBlob(2, thumbnailImageStream);
			pst.setBlob(3, originalImageStream);
			pst.setInt(4, userId);
		
			Count = pst.executeUpdate(); // returns the number of rows affected

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Upload : close conn: " + e.getMessage());
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Upload : close pst: " + e.getMessage());
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Upload : close rs: " + e.getMessage());
				}
			}
		}

		return Count;

	}

}
