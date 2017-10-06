package mypackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;


@WebServlet(name = "loginservlet", urlPatterns = { "/loginservlet" })
public class loginclass extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request,response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter out = response.getWriter();
		String email=null,password=null,mode=null;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
		    sb.append(str);
		}
		String s=sb.toString();
		try {
			JSONObject ob = new JSONObject(s);
			email=ob.getString("email");
			password=ob.getString("password");
			mode=ob.getString("mode");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*
		if(LoginVerify.f == 1){
			//out.println("<script>window.alert('you have not been approve by the admin!')</script>");
			//response.getWriter().write("<script>window.alert('you have not been approve by the admin!')</script>");
			//response.sendRedirect("home.html");	
		}else{
			System.out.println("Approved!");
		}*/
		
	    	   System.out.println(mode);				
	    	   response.setContentType("text/plain");				
	    	   boolean x=LoginVerify.validateLogin(email, password,mode);

	
	if(x==true){
		if(mode.equals("user") & LoginVerify.f == 0){
			HttpSession session=request.getSession();  
		      session.setAttribute("email",email); 
			response.getWriter().write("verified");			
		}else if(mode.equals("admin")){
			HttpSession session=request.getSession();  
		      session.setAttribute("email",email); 
			response.getWriter().write("verified");
		}else if(LoginVerify.f == 1){
			response.getWriter().write("notverified");
		}
	}
	else{
		response.getWriter().write("wrong");
	}
		/**	if(LoginVerify.validateLogin(email, password)){
				
				 response.sendRedirect("/myproject/member Dashboard/mdash.html");
		        
		        System.out.println("successful");   
			        }
			else{
			
				 response.sendRedirect("/myproject/login.html");
			    	System.out.println("username or password incorrect");
			}*/
		
			
		
		
	}

}
