package mypackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Statement;

import org.json.JSONException;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class myclass
 */
@WebServlet(name = "myservlet", urlPatterns = { "/myservlet" })
public class myclass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
   @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	doPost(request,response);
		 }
		

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");

		
		String name=null,email=null,contact = null,password=null;
		StringBuilder sb = new StringBuilder();	
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		String s=sb.toString();
		try {
			JSONObject ob = new JSONObject(s);
			name=ob.getString("name");
			email=ob.getString("email");
			contact=ob.getString("contact");
			password=ob.getString("password");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String q="Insert into register values('"+name+"','"+email+"','"+contact+"','"+password+"','No','1')";	
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/exp","root","");	 
			Statement stmt=con.createStatement();
			stmt.executeUpdate(q);
		}catch(Exception e){ System.out.println(e);}			
		System.out.println("done");
		// response.sendRedirect("/myproject/login.html");
	}
}
