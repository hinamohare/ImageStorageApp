package controller;

import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import executors.Login;


@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//logout action
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			String action = request.getParameter("action");
			
			
				HttpSession session = request.getSession(false);
				if(session != null)
					session.invalidate();
				
				out.println("<p style=\"color:purple\">You are logged out successfully</p>");
				RequestDispatcher rd=request.getRequestDispatcher("login.jsp");  
	            rd.include(request,response);
			
		}
	
	//login action
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  
			response.setContentType("text/html");  
	        PrintWriter out = response.getWriter(); 
	       
	        
	  try{      
	        String userName=request.getParameter("username");  
	        String pwd=request.getParameter("password"); 
	        
	       
	        int userId = Login.Validate(userName, pwd);
	        
	        if(userId>0)
	        {  //user is valid and hence set the session userId and userName
	        	HttpSession session = request.getSession(true);
	        	session.setAttribute("userId", userId);
	        	session.setAttribute("userName",userName);
	        	System.out.println("userID login: " +userId+"  userName:  "+userName);
	        	
	        	
	            RequestDispatcher rd=request.getRequestDispatcher("Homepage.jsp");  
	            rd.forward(request,response);  
	        }  
	        else
	        {  
	            out.print("<p style=\"color:red\">Sorry username or password error</p>");  
	            RequestDispatcher rd=request.getRequestDispatcher("login.jsp");  
	            rd.include(request,response);  
	        }  
	  
	         
	  }
	
	  catch(Exception e)
	  {
		  System.out.println("Exception occurred in loginServlet :"+e.getMessage());
	  }
	
	}

}
