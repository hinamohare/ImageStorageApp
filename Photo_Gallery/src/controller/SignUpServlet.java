package controller;

import java.io.*;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import executors.SignUp;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  response.setContentType("text/html");  
	        PrintWriter out = response.getWriter();  
	        
	        String n=request.getParameter("username");
	        String e=request.getParameter("emailID"); 
	        String p=request.getParameter("password"); 
	        

	        if(SignUp.register(n, e, p))
	        { 
	        	out.print("<p style=\"color:green\">Account successfully created</p>");
	            RequestDispatcher rd=request.getRequestDispatcher("login.jsp");  
	            rd.forward(request,response);  
	        }  
	        else{  
	            out.print("<p style=\"color:red\">Can't create account</p>");  
	            RequestDispatcher rd=request.getRequestDispatcher("login.jsp");  
	            rd.include(request,response);  
	        }  

	        out.close(); 
	}

}
