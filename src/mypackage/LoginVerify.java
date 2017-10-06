package mypackage;

import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class LoginVerify {
	public static String key;
	public static int f =0;
	public static boolean validateLogin(String e, String p, String m) {
		boolean status = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/exp", "root", "");
			String sql = "SELECT flag FROM register WHERE email = '"+e+"' and password = '"+p+"'";
			Statement stmt1 = con.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sql);
			//status = rs.next();
			if(rs1.next()){
				f = rs1.getInt("flag");
				if(f == 1){
					System.out.print("you have not been approve by the admin");
					
				}
			}
			String q = null;
			if (m.equals("user")) {
				q = "SELECT * FROM register where email='" + e + "' and password='" + p + "'";
			} else {
				q = "SELECT * FROM admin where username='" + e + "' and password='" + p + "'";
			}
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery(q);
			status = rs.next();
		} catch (Exception ex) {
			System.out.println(ex);
		}

		return status;
	}
}
