package executors;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class DatabaseHelper {

	public static String ValidateLoginUser = "SELECT userId FROM user WHERE userName=? AND password=?";
	public static String CreateNewUserAccount = "INSERT INTO USER(userName, emailID, password) VALUES(?, ?, ?)";
	public static String InsertPhotoQuery = "INSERT INTO photos(photoName, photo,bigphotos, userID) VALUES (?,?,?,? )";
	public static String DeletePhotoByIdQuery = "DELETE FROM photos WHERE photoID =?";
	public static String SelectPhotoId_PhotoNameByUserId = "SELECT photoID, photoName FROM photos WHERE USERID =?";
	public static String SelectThumbnailPhotoByPhotoId = "SELECT photo FROM photos WHERE photoID=?";
	public static String SelectOriginalPhotoByPhotoId = "SELECT BigPhotos as photo FROM photos WHERE photoID=?";

	private static String ConnectionString = "jdbc:mysql://localhost:3306/photo_gallery?user=root&password=Pass@word1&useSSL=false";
	
	static Connection conn = null;
	static PreparedStatement pst = null;
	static ResultSet rs = null;
	
	public static Connection GetDBConnection() {
		Connection conn = null;
		// 1. create database connection
		try {
			conn = (Connection) DriverManager.getConnection(ConnectionString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;
	}

	public static PreparedStatement GetPrepareStatement(String query) {
		PreparedStatement queryStatement = null;
		Connection conn = null;

		try {
			conn = GetDBConnection();
			queryStatement = (PreparedStatement) conn.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return queryStatement;
	}
	
	 

	public static PreparedStatement GetPrepareStatement(String query, Connection conn) {
		PreparedStatement queryStatement = null;

		try {

			queryStatement = (PreparedStatement) conn.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return queryStatement;
	}

	/*public ResultSet GetRecordSet(String query) {
		ResultSet result = null;
		try {
			PreparedStatement queryStatement = GetPrepareStatement(query);

			result = queryStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}*/

	
}
