/*
 * This class is responsible for displaying photos depending upon their photo ID
 * */
package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import executors.DatabaseHelper;

@WebServlet("/DisplayPhoto")
public class DisplayPhoto extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn= null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Blob blobImage = null;
		ServletOutputStream out = response.getOutputStream();
		try {
			
			int photoId = Integer.parseInt(request.getParameter("photoId"));
			String photoType = (String)request.getParameter("type");
			conn = DatabaseHelper.GetDBConnection();
			if (photoType=="thumbnail") {
				pst = DatabaseHelper.GetPrepareStatement(DatabaseHelper.SelectThumbnailPhotoByPhotoId,conn);
			}
			else{			
				pst = (PreparedStatement)DatabaseHelper.GetPrepareStatement(DatabaseHelper.SelectOriginalPhotoByPhotoId,conn);
			} 

			pst.setInt(1, photoId);

			// execute the query which returns a result set that contains all
			// the photos and their names for specific userID
			rs = pst.executeQuery();

			InputStream inBinaryStream = null;
			
			//if the query is executed successfully, get the blob object reference 
			if (rs.next()) {
				blobImage = rs.getBlob("photo");
			} 
			else {
				response.setContentType("text/html");

				out.println("<font color='red'>image not found for given id</font>");

				return;
			}
			
			//retrieve the blob image in stream
			inBinaryStream = blobImage.getBinaryStream();
			response.setContentType("image/jpeg");
			
			// the image is stored in buffer and sent out in packs of 1024 bytes
			int length = (int) blobImage.length();
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			while ((length = inBinaryStream.read(buffer)) != -1)
			{
				out.write(buffer, 0, length);
			}
			inBinaryStream.close();
			out.flush();

			
		} catch (Exception e) {
			System.out.println("exception in retriving photos in DisplayPhoto : " + e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Display image: close conn: " + e.getMessage());
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Display image: close pst: " + e.getMessage());
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Display image: close rs: " + e.getMessage());
				}
			}

		}

	}

}
