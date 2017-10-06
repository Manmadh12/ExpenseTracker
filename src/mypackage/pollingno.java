package mypackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;

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
 * Servlet implementation class pollingno
 */
@WebServlet("/pollingno")
public class pollingno extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 HttpSession session=request.getSession(false);  
	        String keyid=(String)session.getAttribute("email");
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		int billid=0;
		String gname=null;
	
		while ((str = br.readLine()) != null) {
		    sb.append(str);
		}
		String s=sb.toString();
		try {
			JSONObject ob = new JSONObject(s);
		     billid=ob.getInt("billid");
		    
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/exp","root","");
			
			
			String qgroup="Select gname from group_member where email=?";
			PreparedStatement sgroup=(PreparedStatement) con.prepareStatement(qgroup);
			sgroup.setString(1, keyid);
			ResultSet grp0=sgroup.executeQuery();
			while(grp0.next()){
				gname=grp0.getString("gname");
			}
			grp0.close();
			String q21="update bills set vote_status=1  where bill_id=? and gname=? and member_id=?";
			PreparedStatement s21=(PreparedStatement) con.prepareStatement(q21);
			
		    s21.setInt(1,billid);
		    s21.setString(2,gname);
		    s21.setString(3,keyid);
			s21.executeUpdate();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		
		
	}

}
