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
 * Servlet implementation class declinemember
 */
@WebServlet("/declinemember")
public class declinemember extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
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
        StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		String rejectname=null;
		while ((str = br.readLine()) != null) {
		    sb.append(str);
		}
		String s=sb.toString();
		try {
			JSONObject ob = new JSONObject(s);
		     rejectname=ob.getString("rejectname");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/exp","root",""); 
			String groupname=null;
			System.out.println(keyid);
			System.out.println(groupname);
			String q1="Select gname from group_head where email=? and status=?";
			PreparedStatement s1=(PreparedStatement) con.prepareStatement(q1);
			s1.setString(1, keyid);
			s1.setString(2,"A");
			ResultSet grp=s1.executeQuery();
			while(grp.next()){
				groupname=grp.getString("gname");
			}
			
			String q2="DELETE FROM group_member WHERE gname=? and member_name=?";
			PreparedStatement s2=(PreparedStatement) con.prepareStatement(q2);
			s2.setString(1,groupname);
			s2.setString(2,rejectname);
			s2.executeUpdate();
        }
        catch(Exception e){ System.out.println(e);}
	}

}
