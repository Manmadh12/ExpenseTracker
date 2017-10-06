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
 * Servlet implementation class pollingyes
 */
@WebServlet("/pollingyes")
public class pollingyes extends HttpServlet {
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
		int billid=0;
		String gname=null;
		float total_amount=0;
		while ((str = br.readLine()) != null) {
		    sb.append(str);
		}
		String s=sb.toString();
		try {
			JSONObject ob = new JSONObject(s);
		     billid=ob.getInt("billid");
		     total_amount=ob.getInt("total_amount");
		    
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	//	System.out.println(billid);
		//System.out.println(total_amount);
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/exp","root","");
			String purpose=null;
			String qgroup="Select gname from group_member where email=?";
			PreparedStatement sgroup=(PreparedStatement) con.prepareStatement(qgroup);
			sgroup.setString(1, keyid);
			ResultSet grp0=sgroup.executeQuery();
			while(grp0.next()){
				gname=grp0.getString("gname");
			}
			grp0.close();
			
			int vote=0;
			int outoff=0;
			String q1="Select votes,out_off,purpose from transaction where bill_id=? and gname=?";
			PreparedStatement s1=(PreparedStatement) con.prepareStatement(q1);
			s1.setInt(1, billid);
			s1.setString(2,gname);
			ResultSet grp=s1.executeQuery();
			while(grp.next()){
				vote=grp.getInt("votes");
				outoff=grp.getInt("out_off");
				purpose=grp.getString("purpose");
			}
			grp.close();
			int newvote=vote+1;
		//	System.out.println("reached");
		//	System.out.println(vote);
			//System.out.println(outoff);
			String q2="update transaction set votes=?  where bill_id=? and gname=?";
			PreparedStatement s2=(PreparedStatement) con.prepareStatement(q2);
			s2.setInt(1,newvote);
		    s2.setInt(2,billid);
		    s2.setString(3,gname);
			s2.executeUpdate();
			
			String q21="update bills set vote_status=1  where bill_id=? and gname=? and member_id=?";
			PreparedStatement s21=(PreparedStatement) con.prepareStatement(q21);
			
		    s21.setInt(1,billid);
		    s21.setString(2,gname);
		    s21.setString(3,keyid);
			s21.executeUpdate();
			
			
			System.out.println("fraction "+((float)newvote/(float)outoff));
			if(((float)newvote/(float)outoff)>=0.5){
				
				String q51="Update transaction set status='A' where bill_id=? and gname=?";
				PreparedStatement sup51=(PreparedStatement) con.prepareStatement(q51);
				sup51.setFloat(1, billid);
				
				sup51.setString(2, gname);
				sup51.executeUpdate();
				
				String[] mid=new String[50];
				float[] mam=new float[50];
				String qr1="Select member_id,amount from bills where bill_id=?";
				PreparedStatement sr1=(PreparedStatement) con.prepareStatement(qr1);
				sr1.setInt(1, billid);
				
				ResultSet gr1=sr1.executeQuery();
				int i=-1;
				int k=-1;
				while(gr1.next()){
					mid[++i]=gr1.getString("member_id");
					mam[++k]=(float) gr1.getFloat("amount");
				}
				gr1.close();
				System.out.println(mam[0]);
				System.out.println(mid[0]);
				System.out.println(i);
				String payeeid=null;
				String qo="Select payee from transaction where bill_id=?";
				PreparedStatement qo1=(PreparedStatement) con.prepareStatement(qo);
				qo1.setInt(1, billid);
				
				ResultSet go=qo1.executeQuery();
				while(go.next()){
					
					payeeid=go.getString("payee");
				}
				go.close();
				
				if(purpose.equals("transfer")){
					for(int j=0;j<=i;j++){
						float receive=0;
						float total_amt=0;
					String qold2="Select receive,total_amount from tran_summary where email=?";
					PreparedStatement qoldd2=(PreparedStatement) con.prepareStatement(qold2);
					qoldd2.setString(1, mid[j]);
					
					ResultSet gold2=qoldd2.executeQuery();
					while(gold2.next()){
						
						receive=gold2.getFloat("receive");
						total_amt=gold2.getFloat("total_amount");
					}
					gold2.close();
					System.out.println("reached1");
					System.out.println(j);
					System.out.println(mam[j]);
					float newreceive=receive+mam[j];
					total_amt=total_amt-mam[j];
					String q5="Update tran_summary set receive=?,total_amount=? where email=? and gname=?";
					PreparedStatement sup5=(PreparedStatement) con.prepareStatement(q5);
					sup5.setFloat(1, newreceive);
					sup5.setFloat(2, total_amt);
					sup5.setString(3, mid[j]);
					sup5.setString(4, gname);
					sup5.executeUpdate();
					
					}
					
					float gave=0;
					float total_amt=0;
					
					String qol="Select gave,total_amount from tran_summary where email=?";
					PreparedStatement qold=(PreparedStatement) con.prepareStatement(qol);
					qold.setString(1, payeeid);
					
					
					
					ResultSet gol=qold.executeQuery();
					while(gol.next()){
						
						gave=gol.getFloat("gave");
						total_amt=gol.getFloat("total_amount");
						
					}
					gol.close();
					gave=gave+total_amount;
					 total_amt=total_amt+total_amount;
					String q5="Update tran_summary set gave=?,total_amount=? where email=? and gname=?";
					PreparedStatement sup5=(PreparedStatement) con.prepareStatement(q5);
					sup5.setFloat(1, gave);
					sup5.setFloat(2, total_amt);
					sup5.setString(3, payeeid);
					sup5.setString(4, gname);
					sup5.executeUpdate();
					
				}
				
				else{
				for(int j=0;j<=i;j++){
					float spent=0;
					float total_amt=0;
				String qold2="Select spent,total_amount from tran_summary where email=?";
				PreparedStatement qoldd2=(PreparedStatement) con.prepareStatement(qold2);
				qoldd2.setString(1, mid[j]);
				
				ResultSet gold2=qoldd2.executeQuery();
				while(gold2.next()){
					
					spent=gold2.getFloat("spent");
					total_amt=gold2.getFloat("total_amount");
				}
				gold2.close();
				System.out.println("reached1");
				System.out.println(j);
				System.out.println(mam[j]);
				float newspent=spent+mam[j];
			     total_amt=total_amt-mam[j];
				String q5="Update tran_summary set spent=?,total_amount=? where email=? and gname=?";
				PreparedStatement sup5=(PreparedStatement) con.prepareStatement(q5);
				sup5.setFloat(1, newspent);
				sup5.setFloat(2, total_amt);
				sup5.setString(3, mid[j]);
				sup5.setString(4, gname);
				sup5.executeUpdate();
				
				}
				System.out.println("reached2");
				float paid=0;
				float total_amt=0;
				String qol="Select paid,total_amount from tran_summary where email=?";
				PreparedStatement qold=(PreparedStatement) con.prepareStatement(qol);
				qold.setString(1, payeeid);
				
				ResultSet gol=qold.executeQuery();
				while(gol.next()){
					
					
					paid=gol.getFloat("paid");
					total_amt=gol.getFloat("total_amount");
					
				}
				gol.close();
				paid=paid+total_amount;
				total_amt=total_amt+total_amount;
				String q5="Update tran_summary set paid=?,total_amount=? where email=? and gname=?";
				PreparedStatement sup5=(PreparedStatement) con.prepareStatement(q5);
				sup5.setFloat(1, paid);
				sup5.setFloat(2, total_amt);
				sup5.setString(3, payeeid);
				sup5.setString(4, gname);
				sup5.executeUpdate();
			}
				}
			
			
        }
        catch(Exception e){ System.out.println(e);}
		
		
		
		
	}

}
