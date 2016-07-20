/**
 *This program deletes the photo from the gallery depending upon the photoID 
 **/
package controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import executors.DatabaseHelper;
import executors.Photos;

@WebServlet("/DeletePhoto")
public class DeletePhoto extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int photoId = Integer.parseInt(request.getParameter("photoId"));
		
		//After deleting the image return the updated homepage
		if(Photos.DeletePhoto(photoId))
		{	RequestDispatcher rd=request.getRequestDispatcher("Homepage.jsp");  
			rd.forward(request,response); 
		}
	}

}
