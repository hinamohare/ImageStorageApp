package controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import executors.Photos;
import executors.Upload;

/**
 * This servlet takes the input request from client which contains photo name and photo
 * it uploads the input data to the database 
 * returns the uploaded photo to the client in thumbnail
 */
@WebServlet("/Upload")
@MultipartConfig  //indicates that the page expects input to be multipart
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		try
		{
	    
			String photoName = request.getParameter("photoname"); // Retrieves <input type="text" name="description">
		    Part filePart = request.getPart("photo"); // Retrieves <input type="file" name="file">
		    
		    //obtain name of photo in photoName for debug purpose
		    System.out.println("photoname upload name: "+photoName);
	
		    // obtains the upload file part in this multipart request
		    InputStream originalImageInputStream = null; 
		    InputStream longImageInputStream = null; 
		    
		    
       
		    if (filePart != null) {
           
		    // prints out some information for debugging
		    System.out.println("The input photo details: ");
            System.out.println(filePart.getName()+"  ");
            System.out.println(filePart.getSize()+"  ");
            System.out.println(filePart.getContentType());
             
            // obtains input stream of the upload file
            originalImageInputStream = filePart.getInputStream(); 
            longImageInputStream = filePart.getInputStream(); 
		    }
		    else
		    {
		    	response.setContentType("text/html");

				out.println("<font color='red'>Image not uploaded</font>");

				return;
		    }
		    
		 
		  
		    int userId = -1 ;  //set the default userId
		    //retrive the session userID
		    HttpSession session = request.getSession(false); 
		    userId = (int) session.getAttribute("userId");  
		
		    //print session userId for debug purpose
		    System.out.println("userId in upload servlet: "+userId);
		       
		    // convert the original image to thumbnail image
		    InputStream thumbnailImageStream = Photos.OriginalToThumbnailConverter(originalImageInputStream);
		    
		    
		    //insert the uploaded photo to database 
		   int result = Upload.InsertPhoto( photoName, thumbnailImageStream, longImageInputStream, userId);
          
		   if(result>0)
            {
        	   out.print("Successfully uploaded photo..."); 
        	  
        	   RequestDispatcher rd=request.getRequestDispatcher("Homepage.jsp");  
	           rd.forward(request,response); 
	            
        	   
            }
           else
           {
        	   out.print("<p style=\"color:red\">Failed to upload photo, try again...</p>");  
	            RequestDispatcher rd=request.getRequestDispatcher("Homepage.jsp");  
	            rd.include(request,response);  
           }
        }
        		
		catch(Exception ex )
		{
			System.out.println(ex.getMessage());
		}
		
		
	}

}
