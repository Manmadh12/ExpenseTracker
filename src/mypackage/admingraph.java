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
 * Servlet implementation class admingraph
 */
@WebServlet("/admingraph")
public class admingraph extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession(false);  
        String keyid=(String)session.getAttribute("email");  
        try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/exp","root",""); 
			
			JSONObject o=new JSONObject();
			
			JSONArray total_tran= new JSONArray();
			JSONArray groupname1= new JSONArray();
			String q2="Select count(bill_id) as totalbills,gname as group_name from transaction group by gname";
			PreparedStatement s2=(PreparedStatement) con.prepareStatement(q2);
			ResultSet grp1=s2.executeQuery();
			while(grp1.next()){
				total_tran.put(grp1.getInt("totalbills"));
				groupname1.put(grp1.getString("group_name"));
			}
			grp1.close();
			
			JSONArray total_amount= new JSONArray();
			JSONArray groupname2= new JSONArray();
			String q3="Select sum(total_amt) as totalamount,gname as group_name from transaction group by gname";
			PreparedStatement s3=(PreparedStatement) con.prepareStatement(q3);
			ResultSet grp2=s3.executeQuery();
			while(grp2.next()){
				
				total_amount.put(grp2.getInt("totalamount"));
				groupname2.put(grp2.getString("group_name"));
			}
			grp2.close();
			
			JSONArray total_user= new JSONArray();
			JSONArray groupname3= new JSONArray();
			String q4="Select count(email) as totaluser,gname as group_name from group_member where  status='A' group by gname ";
			PreparedStatement s4=(PreparedStatement) con.prepareStatement(q4);
			ResultSet grp3=s4.executeQuery();
			while(grp3.next()){
				total_user.put(grp3.getInt("totaluser"));
				groupname3.put(grp3.getString("group_name"));
	
			}
			grp3.close();
			
			o.put("transactionbyeach",total_tran);
			o.put("totalgroup1byeach",groupname1);
	        o.put("tran_amountbyeach",total_amount);
	        o.put("totalgroup2byeach",groupname2);
	        o.put("total_userbyeach",total_user);
	        o.put("totalgroup3byeach",groupname3);
	        String n=o.toString();
			
			System.out.println(n);
			response.setContentType("application/json");
			response.getWriter().write(o.toString());
			
	}
        
        catch(Exception e){ System.out.println(e);}
		System.out.println("done");
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		

}
}
