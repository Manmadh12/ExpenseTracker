package mypackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONValue;

import com.google.gson.Gson;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@WebServlet(name = "addbillserv", urlPatterns = { "/addbillserv" })
public class addbill extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
								
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  HttpSession session=request.getSession(false);  
	        String keyid=(String)session.getAttribute("email");  
		System.out.println("request received");
		String purpose=null,datestr=null;
		Float amount=null;
		Date date=new Date();
		String groupname=null;
		
		 System.out.println("reached");
		
		JSONArray memamt=new JSONArray();
		String[] gm=null;
		Float[] amt=null;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
		    sb.append(str);
		}
		String s=sb.toString();
		 System.out.println(s);
		 System.out.println("reached");
		try {
			JSONObject ob = new JSONObject(s);
		purpose=ob.getString("purpose");
		 System.out.println("reached");
			amount=(float) ob.getInt("amount");
			datestr=ob.getString("date");
			memamt=ob.getJSONArray("memamt");
			
			SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
		gm=new String[memamt.length()];
		amt=new Float[memamt.length()];
			
		
			 for (int i = 0; i < memamt.length(); i++) {
			 JSONObject obj=new JSONObject();
			  obj = memamt.getJSONObject(i);
		            gm[i] = obj.getString("id");
		            amt[i]=(float) obj.getInt("amount");
		           // amt[i]=Float.parseFloat(st);
		        }
		System.out.println(gm[0]);	
		System.out.println(amt[0]);
			 
			
			try {
				date=sdf.parse(datestr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	
		
		
		//String qgroup="Select gname from group_member where email=?";
		
		 
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/exp","root",""); 
		
			
			int count=amt.length;
			System.out.println(keyid);
			String qgroup="Select gname from group_member where email=?";
			PreparedStatement sgroup=(PreparedStatement) con.prepareStatement(qgroup);
			sgroup.setString(1, keyid);
			ResultSet grp=sgroup.executeQuery();
			while(grp.next()){
				groupname=grp.getString("gname");
			}
			grp.close();
			String q="Insert into transaction values('"+keyid+"',NULL,'"+purpose+"','"+amount+"','"+datestr+"','"+groupname+"',0,'"+count+"','P')";
			Statement stmt=con.createStatement();
			stmt.executeUpdate(q);
			
			int bill_id=-1;
			String q1="Select MAX(bill_id) from transaction where payee=? and status='P'";
			PreparedStatement s1=(PreparedStatement) con.prepareStatement(q1);
			s1.setString(1, keyid);
           ResultSet r1=s1.executeQuery();
           while(r1.next()){
				bill_id=r1.getInt("MAX(bill_id)");
			}
			r1.close();
			System.out.println("reached");
			int j=-1;
			String [] em=new String[count+1];
				String q2="Select email from group_member where gname=? ";
				PreparedStatement s2=(PreparedStatement) con.prepareStatement(q2);
				s2.setString(1, groupname);
				
	           ResultSet r2=s2.executeQuery();
	           while(r2.next()){
				em[++j]=r2.getString("email");
				}
				r2.close();
				System.out.println("reached");
			
			
			for(int i=0;i<count;i++){
				String q3="Insert into bills values('"+bill_id+"','"+em[i]+"','"+amt[i]+"','P','"+groupname+"',0)";
				Statement s3=con.createStatement();
				stmt.executeUpdate(q3);
				
				
			}
           
			}catch(Exception e){ System.out.println(e);}
				
				
				System.out.println("done");
				
			}
	}


