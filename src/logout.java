

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

/**
 * Servlet implementation class logout
 */
@WebServlet(name = "logout", urlPatterns = { "/logout" })
public class logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  HttpSession session=request.getSession(false);  
	        String keyid=(String)session.getAttribute("email");  
		//System.out.println("request received");
	       
			try{  
				Class.forName("com.mysql.jdbc.Driver");  
				Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/exp","root",""); 
				
				String qr1="update register set login_status='No'   where email=?";
				
				PreparedStatement st1=(PreparedStatement) con.prepareStatement(qr1);
				st1.setString(1, keyid);
				st1.executeUpdate();
				
				
	            session.invalidate();
				}catch(Exception e){ System.out.println(e);}
					
					
					System.out.println("done");
					 RequestDispatcher view = request.getRequestDispatcher("/home.html");
				

				    view.forward(request, response);
				}
	
	
	}


