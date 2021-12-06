package Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class show {

	public static void main(String[] args) throws Exception {

		boolean flag = true;
		Class.forName("com.mysql.cj.jdbc.Driver");

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_theater", "root", "Admin@123$");

		String Query = ("insert into user(name,address,isadmin) VALUES (?,?,?)");
		PreparedStatement pstmt = con.prepareStatement(Query, Statement.RETURN_GENERATED_KEYS);

		Scanner sc = new Scanner(System.in);

		System.out.print("Enter your full name :-  ");
		String uName = sc.nextLine();

		System.out.print("Enter your address :-  ");
		String uaddress = sc.nextLine();

		System.out.print("if you are admin then enter 1 or if you are user then Enter 0 :-  ");
		int isadmin = 0;
		isadmin = sc.nextInt();			

		while (flag) {
			
			if (isadmin == 0 || isadmin == 1) {
				flag = false;
			}
			else {
				System.out.print(" you have to Enter only 0 or 1 : ");
				isadmin = sc.nextInt();

			}
		}

		pstmt.setString(1, uName);
		pstmt.setString(2, uaddress);
		pstmt.setInt(3, isadmin);

//-----------------------------------------------------------------------------------------

		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();

		int id = 0;
		rs.next();
		id = rs.getInt(1);

//		System.out.print(id);
		System.out.println("insert Sucsessfully :");
//--------------------------------------------------------------------------------------------------------
		String sql = ("insert into  login (username,password) VALUES (?,?)");
		PreparedStatement pstmtsql = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		System.out.print(" Enter username :");
		String username = sc.next();

		String selctUsername = "select username from Login where username = ? ";
		PreparedStatement psusername = con.prepareStatement(selctUsername);

		psusername.setString(1, username);

		ResultSet rsuNsme = psusername.executeQuery();
		if (rsuNsme.next()) {
			String checkName = rsuNsme.getString("username");

			while (flag) {

				if (username.equals(checkName)) {
					System.out.println(" Username already exist , please Enter Unique username");
					System.out.print("Re_Enter usename :");
					username = sc.next();

					flag = true;
				} else {
					System.out.println("username available");
					flag = false;
				}
			}
		}

		flag = true;
		String password = "";

		while (flag) {

			System.out.println("Enter a Password :- ");
			password = sc.next();

			System.out.print("confirm  password :- ");
			String rePassword = sc.next();

			if (password.equals(rePassword)) {
				System.out.println("welcome , your information is Saved.");

				password = rePassword;

				flag = false;

			} else {
				System.out.println(" RE - Enter , password not matched. ");
				flag = true;
			}
		}

		pstmtsql.setString(1, username);
		pstmtsql.setString(2, password);

		pstmtsql.executeUpdate();
		ResultSet rspass = pstmtsql.getGeneratedKeys();

		rspass.next();
		int userId = rspass.getInt(1);

		System.out.println("Insert Succesfully...");
//--------------------------------------------------------------------------------------------------
		

		String updateLoginId = ("update user set userLoginId = ? where id = ? ");
		PreparedStatement psUpdateLoginId= con.prepareStatement(updateLoginId );

		psUpdateLoginId.setInt(1, userId);
		psUpdateLoginId.setInt(2, id);

		int userLoginId = psUpdateLoginId.executeUpdate();

//---------------------------------------------------------------------------------------------------
//===================================================================================================
		

	}
}