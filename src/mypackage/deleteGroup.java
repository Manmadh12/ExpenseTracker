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
import com.mysql.jdbc.Statement;

/**
 * Servlet implementation class deleteGroup
 */
@WebServlet("/deleteGroup")
public class deleteGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
			
			String q3="Delete from group_member where gname=?";
			PreparedStatement s3=(PreparedStatement) con.prepareStatement(q3);
			s3.setString(1,rejectgroup);
			s3.executeUpdate();
			
			String q4="Delete from tran_summary where gname=?";
			PreparedStatement s4=(PreparedStatement) con.prepareStatement(q4);
			s4.setString(1,rejectgroup);
			s4.executeUpdate();
			
        }
        catch(Exception e){ System.out.println(e);}
		
	}

}
