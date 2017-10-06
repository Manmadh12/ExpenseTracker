package mypackage;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

/**
 * Servlet implementation class pendinguser
 */
@WebServlet("/pendinguser")
public class pendinguser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession(false);  
        String keyid=(String)session.getAttribute("email");  
        try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/exp","root",""); 
			
			JSONObject o=new JSONObject();
			JSONArray username=new JSONArray();
			System.out.println(keyid);
			String q1="Select email from register where flag=1";
			PreparedStatement s1=(PreparedStatement) con.prepareStatement(q1);
			ResultSet grp=s1.executeQuery();
			while(grp.next()){
				username.put(grp.getString("email"));
			}
			grp.close();
			
			String name=null;
			String q3="Select username from admin where username=?";
			PreparedStatement s3=(PreparedStatement) con.prepareStatement(q3);
			s3.setString(1, keyid);
			ResultSet grp2=s3.executeQuery();
			while(grp2.next()){
				name=grp2.getString("username");
			}
			
			
			o.put("pendinguser", username);
			o.put("userdetail", name);
			System.out.println(o.toString());
			response.setContentType("application/json");
			response.getWriter().write(o.toString());

        }
			catch(Exception e){ System.out.println(e);}
			System.out.println("done");
	}

}
