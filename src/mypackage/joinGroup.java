package mypackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
import com.mysql.jdbc.PreparedStatement;

/**
 * Servlet implementation class joinGroup
 */
@WebServlet("/joinGroup")
public class joinGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	  
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doPost(request,response);
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			  HttpSession session=request.getSession(false);  
		        String keyid=(String)session.getAttribute("email");  
			System.out.println("request received");
			String gn=null;
			StringBuilder sb = new StringBuilder();
			BufferedReader br = request.getReader();
			String str = null;
			while ((str = br.readLine()) != null) {
			    sb.append(str);
			}
			String s=sb.toString();
			try {
				JSONObject ob = new JSONObject(s);
			gn=ob.getString("groupname");
				
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//String qgroup="Select gname from group_member where email=?";
			
			 
			try{  
				Class.forName("com.mysql.jdbc.Driver");  
				Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/exp","root",""); 
				
			
				String member_name=null;
			
			
				System.out.println(keyid);
				System.out.println(gn);
				String q1="Select name from register where email=?";
				PreparedStatement s1=(PreparedStatement) con.prepareStatement(q1);
				s1.setString(1, keyid);
				ResultSet grp=s1.executeQuery();
				while(grp.next()){
					member_name=grp.getString("name");
				}
				grp.close();
				
				
				String q="Insert into group_member values('"+keyid+"','"+gn+"','P','"+member_name+"')";
				Statement stmt=con.createStatement();
				stmt.executeUpdate(q);
				}catch(Exception e){ System.out.println(e);}
					System.out.println("done");
			
		}
}
