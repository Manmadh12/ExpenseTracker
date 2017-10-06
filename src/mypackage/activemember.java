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
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

/**
 * Servlet implementation class activemember
 */
@WebServlet("/activemember")
public class activemember extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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
		
        try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/exp","root",""); 
			JSONArray groupmember= new JSONArray();
			
			JSONObject o=new JSONObject();
			System.out.println(keyid);
			String groupname=null;
			String q1="Select gname from group_head where email=? and status=?";
			PreparedStatement s1=(PreparedStatement) con.prepareStatement(q1);
			s1.setString(1, keyid);
			s1.setString(2,"A");
			ResultSet grp=s1.executeQuery();
			while(grp.next()){
				groupname=grp.getString("gname");
			}
			grp.close();
			String q2="Select member_name from group_member where gname=? and status=?";
			PreparedStatement s2=(PreparedStatement) con.prepareStatement(q2);
			s2.setString(1, groupname);
			s2.setString(2,"A");
			ResultSet grp2=s2.executeQuery();
			while(grp2.next()){
				groupmember.put(grp2.getString("member_name"));
			}
			grp2.close();
			
			String name=null;
			String q3="Select name from register where email=?";
			PreparedStatement s3=(PreparedStatement) con.prepareStatement(q3);
			s3.setString(1, keyid);
			ResultSet grp3=s3.executeQuery();
			while(grp3.next()){
				name=grp3.getString("name");
			}
			grp3.close();
			
        o.put("activemember", groupmember);
        o.put("userdetail", name);
		System.out.println(o.toString());
		response.setContentType("application/json");
		response.getWriter().write(o.toString());
        }
        catch(Exception e){ System.out.println(e);}
				System.out.println("done");
	}
	}

