package mypackage;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class dashdisplay
 */
@WebServlet("/dashdisplay")
public class dashdisplay extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  HttpSession session=request.getSession(false);  
	        String keyid=(String)session.getAttribute("email");  
	    float spent=0,paid=0,receive=0,gave=0;
	    String name=null;
	    String gname=null;
	    try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/exp","root",""); 
			JSONObject o=new JSONObject();
			String q4="Select member_name,gname from group_member where email=?";
			PreparedStatement st4=(PreparedStatement) con.prepareStatement(q4);
			st4.setString(1, keyid);
			ResultSet g4=st4.executeQuery();
			while(g4.next()){
				name=g4.getString("member_name");
				gname=g4.getString("gname"); 
			}
			g4.close();
			
			JSONArray summary=new JSONArray();
			String q0="SELECT group_member.member_name,tran_summary.spent,tran_summary.paid,tran_summary.receive,tran_summary.gave,tran_summary.total_amount from  group_member INNER JOIN tran_summary on group_member.email=tran_summary.email and tran_summary.gname=?";
			PreparedStatement st0=(PreparedStatement) con.prepareStatement(q0);
			st0.setString(1, gname);
		
			
			ResultSet g0=st0.executeQuery();
			while(g0.next()){
				JSONObject obj=new JSONObject();
				obj.put("name",g0.getString("member_name"));
				obj.put("paid", g0.getString("paid"));
				obj.put("spent", g0.getString("spent"));
				obj.put("received", g0.getString("receive"));
				obj.put("gave", g0.getString("gave"));
				obj.put("total_amount", g0.getString("total_amount"));
				summary.put(obj);
			}
			g0.close();
			
			
			
			System.out.println(name);
			String q1="Select spent,paid,receive,gave from tran_summary where email=?";
			PreparedStatement st1=(PreparedStatement) con.prepareStatement(q1);
			st1.setString(1, keyid);
			ResultSet g1=st1.executeQuery();
			while(g1.next()){
				spent=g1.getFloat("spent");
			paid=g1.getFloat("paid");
				receive=g1.getFloat("receive");
				gave=g1.getFloat("gave");
			}
			g1.close();
			
			
			
			
			System.out.println(name);
			float total_amt=(paid+gave)-(spent+receive);
			System.out.println("done");
			JSONArray gnames= new JSONArray();
			
			JSONArray pendingdetail=new JSONArray();
			
			
			
			String q2="SELECT member_name from  group_member where gname=? and status=?";
			PreparedStatement st2=(PreparedStatement) con.prepareStatement(q2);
			st2.setString(1, gname);
			st2.setString(2,"A");
			ResultSet g2=st2.executeQuery();
			while(g2.next()){
				JSONObject obj=new JSONObject();
				obj.put("id",g2.getString("member_name"));
				obj.put("amount", new Float(0));
				gnames.put(obj);
			}
			g2.close();
			
			//String q4="Select vote_status from bills where bill_id=? and member_id=?";

			String q3="SELECT purpose,bill_id,total_amt,date,votes from transaction where gname=? and status='P'";
			PreparedStatement st3=(PreparedStatement) con.prepareStatement(q3);
		st3.setString(1, gname);
			ResultSet g3=st3.executeQuery();
			while(g3.next()){
				int x=0;
				int id=g3.getInt("bill_id");
				String q5="Select vote_status from bills where bill_id=? and member_id=?";
				PreparedStatement st5=(PreparedStatement) con.prepareStatement(q5);
				st5.setInt(1, id);
				st5.setString(2,keyid);
					ResultSet g5=st5.executeQuery();
					while(g5.next()){
						x=g5.getInt("vote_status");
					}
					if(x==0){
				JSONObject obj=new JSONObject();
				obj.put("purpose",g3.getString("purpose"));
				obj.put("id",g3.getInt("bill_id") );
				obj.put("amt",g3.getString("total_amt") );
				obj.put("votes",g3.getString("votes") );
				obj.put("date",g3.getDate("date"));
				pendingdetail.put(obj);}
			}
			g3.close();
			
			 o.put("pendingbills", pendingdetail);
		o.put("grouplist",gnames);
		o.put("total",new Float(total_amt));
		o.put("name", new String(name));
		o.put("lend", new Float(gave));
		o.put("debt", new Float(receive));
		o.put("summary", summary);
		
		
			
String n=o.toString();
		
		System.out.println(n);	
			 
	
	
	response.setContentType("application/json");
	response.getWriter().write(o.toString());

			
			}catch(Exception e){ System.out.println(e);}
				
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
