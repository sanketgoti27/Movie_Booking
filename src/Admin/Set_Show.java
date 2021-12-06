package Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Set_Show {

	public static void main(String[] args) throws Exception {

		boolean flag = true;

		Class.forName("com.mysql.cj.jdbc.Driver");

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_theater", "root", "Admin@123$");

		Scanner sc = new Scanner(System.in);

//-----------------------------------------------------------------------------------------------------
		String loginQuery = ("select username from Login where username = ? ");
		PreparedStatement psLogin = con.prepareStatement(loginQuery);

		System.out.print(" Username :- ");
		String username = sc.next();

		ResultSet rslogin = null;

		while (flag) {

			psLogin.setString(1, username);
			rslogin = psLogin.executeQuery();

			if (rslogin.next()) {
				String checkuser = rslogin.getString("username");

				while (flag) {

					if (username.equals(checkuser)) {
						System.out.println("username is correct");
						flag = false;
					}
				}
			} else {
				System.out.println("re Enter Username :- ");
				username = sc.next();
			}
		}
		flag = true;

//----------------------------------------------------------------------------------------------------------

		String passwordQuery = ("select password from Login where password = ? and username = ?");
		PreparedStatement psPassword = con.prepareStatement(passwordQuery);

		System.out.print(" Password :- ");
		String password = sc.next();

		ResultSet rsPassword = null;

		while (flag) {

			psPassword.setString(1, password);
			psPassword.setString(2, username);

			rsPassword = psPassword.executeQuery();
			if (rsPassword.next()) {
				String userPassword = rsPassword.getString("password");

				if (password.equals(userPassword))
					System.out.println(" welcome!!! , " + username);
				flag = false;

			} else {
				System.out.println("password not match ");
				System.out.print("You have to re Enter your password :- ");
				password = sc.next();
			}
		}
//----------------------------------------------------------------------------------------
		String LoginId = "select userId from login where username = ?";
		PreparedStatement psLoginId = con.prepareStatement(LoginId);

		psLoginId.setString(1, username);

		ResultSet rsLoginId = psLoginId.executeQuery();

		int loginId = 0;

		if (rsLoginId.next())
			loginId = rsLoginId.getInt("userId");

//=================================================================================================================
		flag = true;

		String selectUseName = "select isAdmin from user where userLoginId = ?";
		PreparedStatement psSelectUName = con.prepareStatement(selectUseName);

		psSelectUName.setInt(1, loginId);

		ResultSet rsSelectUseName = psSelectUName.executeQuery();

		int isAdmin = 0;

		if (rsSelectUseName.next())
			isAdmin = rsSelectUseName.getInt("isAdmin");
//-------------------------------------------------------------------------------------------------

		if (isAdmin == 1) {
			System.out.println("yes , you are Admin you have Accsess ");

		} else {
			System.out.println(" you are a user , Not Admin \n you have no Accsess ");
			System.exit(0);
		}

//------------------------------------------------------------------------------------------------
		String movieShow = "insert into movieShow ( startTime , ScreenId , MovieNameId , dateOfRelease ,ticketPrise ) values (?,?,?,?,?)";
		PreparedStatement psMovieShow = con.prepareStatement(movieShow);

		sc.nextLine();
		System.out.println("Enter date in this formate only --> dd-mm-yyyy  <-- \n with first show time ");
		String movieDate = sc.nextLine();

// ---------------------------------------------------------------------------------------------------

		Date d1 = null;
		String d2 = null;
		String d3 = null;

		SimpleDateFormat inputDate = null;
		// SimpleDateFormat timeFormat = null;
		while (flag) {

			inputDate = new SimpleDateFormat("dd-MM-yyyy hh.mm aa");
			d1 = inputDate.parse(movieDate);

			SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
			d2 = newFormat.format(d1);

			int fridayOrNot = d1.getDay();

			if (fridayOrNot == 5) {
				System.out.println("yes , on this date it is  a friday ");
				flag = false;

			} else {
				System.out.println(" No, on this date not friday ");
				System.out.println(" show only set on Friday ");
				System.out.print(" RE - Enter date which is you know it's Friday :-");
				movieDate = sc.nextLine();
			}
		}

		flag = true;
		int movieNameId = 0;
		int screenId = 0;
		System.out.println(
				" Enter movieId which is \n     1 - suryavanshi \n     2 - satyamev jayte 2 \n     3 - Antim  ");

		movieNameId = sc.nextInt();

//====================================================================================================		

		while (flag) {
			System.out.print(" Enter Screen number which in 1 , 2 , 3 :- ");
			screenId = sc.nextInt();

// ------------------------------------------------------------------------------------------------------

			String checkScreenId = "select screenId from movieShow where startTime = ? and  screenId = ?";
			PreparedStatement psCheckScreenId = con.prepareStatement(checkScreenId);

			psCheckScreenId.setString(1, movieDate);

			psCheckScreenId.setInt(2, screenId);
			int sId = 0;
			ResultSet rsCheckScreenId = psCheckScreenId.executeQuery();

			if (rsCheckScreenId.next()) {
				sId = rsCheckScreenId.getInt("screenId");
			}
			if (sId == screenId) {
				System.out.println(" this Screen is occupied at this time ");
			}

			else {
				System.out.println(" yes , as per your requirement the Show and the Screen is providing you");
				flag = false;
			}
		}
	
		System.out.print(" Enter a prise for one show :");
		int setPrise = sc.nextInt();
//====================================================================================================

		for (int i = 1; i <= 7; i++) {
			Calendar cal = Calendar.getInstance();

			String dateOfRelease = (d2);

			for (int j = 1; j <= 4; j++) {

				String startTime = inputDate.format(d1);
				psMovieShow.setString(1, startTime);
				psMovieShow.setInt(2, screenId);
				psMovieShow.setInt(3, movieNameId);
				psMovieShow.setString(4, dateOfRelease);
				psMovieShow.setInt(5, setPrise);

				psMovieShow.executeUpdate();

				cal.setTime(d1);
				cal.add(Calendar.HOUR_OF_DAY, 3);

				d1 = cal.getTime();
			}
			cal.setTime(d1);
			cal.add(Calendar.HOUR_OF_DAY, 12);
			d1 = cal.getTime();
		}
		System.out.println(" Everything set Sucssesfully... ");
//--------------------------------------------------------------------------------------------------

	}
}
