package mypackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

/**
 * Servlet implementation class declinehead
 */
@WebServlet("/declinehead")
public class declinehead extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public declinehead() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		HttpSession session=request.getSession(false);  
        String keyid=(String)session.getAttribute("email");
       String rejectgroup=null;
        StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
		    sb.append(str);
		}
		String s=sb.toString();
		try {
			JSONObject ob = new JSONObject(s);
		
			rejectgroup=ob.getString("rejectgroup");
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
        try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/exp","root",""); 
			
			
			String q2="Delete from group_head where gname=?";
			PreparedStatement s2=(PreparedStatement) con.prepareStatement(q2);
			
			s2.setString(1,rejectgroup);
			s2.executeUpdate();
			
        }
        catch(Exception e){ System.out.println(e);}
		
	}
	}
