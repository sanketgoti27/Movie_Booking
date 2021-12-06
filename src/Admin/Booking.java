package Admin;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.util.Properties;

public class Booking {

	public static void main(String[] args) throws Exception {

		Class.forName("com.mysql.cj.jdbc.Driver");

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_theater", "root", "Admin@123$");

		Scanner sc = new Scanner(System.in);
		boolean flag = true;

		// sc.nextLine();

//--------------------------------------------------------------------------------------------------------------
//==============================================================================================================
//--------------------------------------------------------------------------------------------------------------

		String booking = (" insert into booking(name,mId,showId,noOfTicket,screenNo,email)values(?,?,?,?,?,?) ");
		PreparedStatement psBooking = con.prepareStatement(booking);
		flag = true;

		System.out.print(" Enter your full name :");
		String name = sc.next();

		int ticketPrise = 0;
		int screenId = 0;
		int noOfTicket = 0;
		int movieId = 0;
		int bookedSeats = 0;
		int showId = 0;

		while (flag) {
			String bookDateTime = null;
			flag = true;
			int movieNameId = 0;

			while (flag) {

				System.out.print(" Enter movieId ");
				movieId = sc.nextInt();

				System.out.print(" Enter ShowId :- ");
				showId = sc.nextInt();

//---------------------------------------------------------------------------------------------------------------

				String selectDateTime = "select movieNameId from movieShow where id = ?";
				PreparedStatement psSelectDateTime = con.prepareStatement(selectDateTime);

				psSelectDateTime.setInt(1, showId);

				ResultSet rsSelectDateTime = psSelectDateTime.executeQuery();

				if (rsSelectDateTime.next()) {

					movieNameId = rsSelectDateTime.getInt("movieNameId");
				}

//---------------------------------------------------------------------------------------------------------------

				if (movieId == movieNameId) {

					System.out.println(" Screen is available for this movie on your date and time ");
					flag = false;

				} else {
					System.out.println(" At your time this movie not avlialble ,choose diffrent time : ");

				}
			}

//---------------------------------------------------------------------------------------------------------------
			String selectTPrise = "select screenId,ticketPrise from movieShow where id = ?";
			PreparedStatement psSelectTPrise = con.prepareStatement(selectTPrise);

			psSelectTPrise.setInt(1, showId);

			ResultSet rsSelectTPrise = psSelectTPrise.executeQuery();

			if (rsSelectTPrise.next()) {

				screenId = rsSelectTPrise.getInt("screenId");
				ticketPrise = rsSelectTPrise.getInt("ticketPrise");
			}

//-----------------------------------------------------------------------------------------------------------

			String selectSeats = "select  sum(b.noOfTicket),b.showId, b.screenNo from booking b inner join movieShow m on b.showid = m.id where showId = ? group by showId having sum(b.noOfticket) ";
			PreparedStatement psSelectSeats = con.prepareStatement(selectSeats);

			psSelectSeats.setInt(1, showId);

			ResultSet rsSelectSeats = psSelectSeats.executeQuery();
			bookedSeats = 0;
			if (rsSelectSeats.next()) {

				bookedSeats = rsSelectSeats.getInt("sum(b.noOfTicket)");
			}

//-------------------------------------------------------------------------------------------------------------
			String selectScreen = "select seats from screen where screenId = ?";
			PreparedStatement psSelectScreen = con.prepareStatement(selectScreen);

			psSelectScreen.setInt(1, screenId);

			ResultSet rsSelectScreen = psSelectScreen.executeQuery();
			int seatsCapacity = 0;

			if (rsSelectScreen.next()) {

				seatsCapacity = rsSelectScreen.getInt("seats");

			}

//-------------------------------------------------------------------------------------------------------------
			System.out.println(" Enter No. of tickes which you want to book :-  ");
			noOfTicket = sc.nextInt();

			if (bookedSeats <= seatsCapacity) {
				int seatsLeft = (seatsCapacity - bookedSeats);
				// System.out.println("seats is available for booking ");

				if (noOfTicket <= seatsLeft) {
					System.out.println(" " + seatsLeft + " seats  left. ");
					flag = false;

				} else {
					System.out.println(" seats not available. ");
					flag = true;
				}
			}

		}
		System.out.println("you have to give :-->  " + noOfTicket * ticketPrise);

// -----------------------------------------------------------------------------------------------------------
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

		// --------------------------------------------------------------------------------------------------------------

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
		System.out.println();

		System.out.println(" Enter Your Email address :");
		String email = sc.next();
//---------------------------------------------------------------------------------------------------------------

		psBooking.setString(1, name);
		psBooking.setInt(2, movieId);
		psBooking.setInt(3, showId);
		psBooking.setInt(4, noOfTicket);
		psBooking.setInt(5, screenId);
		psBooking.setString(6, email);

		psBooking.executeUpdate();

		System.out.println(" Congratulation!!! , tickets booked");
//--------------------------------------------------------------------------------------------

//		String getmovieId = "select movieName from "

//---------------------------------------------------------------------------------------------		
		Document doc = new Document();
		try {
			PdfWriter write = PdfWriter.getInstance(doc, new FileOutputStream("sg4.pdf"));
			doc.open();
			doc.add(new Paragraph(" welcome to codeZombie theater "));

			System.out.println("\n");

			String Ticket = Integer.toString(noOfTicket);
			String Movie = Integer.toString(movieId);
			String screen = Integer.toString(screenId);

			List list = new List();

			list.add(name);
			list.add(Ticket);
			list.add(screen);
			list.add(Movie);

			doc.add(list);

			doc.close();
			write.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

//---------------------------------------------------------------------------------------------		

		
		String host = "smtp.gmail.com";
		String from = "sanketgoti1111@gmail.com";
		String pass = "uxsorfrkytztuijr";
		String to = email;
		
		Properties props = new Properties();

		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.auth", "true");
		
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, pass);
			
			}
		});
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("codeZombie ");
			message.setText(" Hello Prashant bhai !! How are You ? ");

			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText("This is message body");
		
			 MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
			  
			    String filename = "D:/Sanket/Movie_Booking/sg4.pdf";
			    	   
			    DataSource source = new FileDataSource(filename);  
			    messageBodyPart2.setDataHandler(new DataHandler(source));  
			    messageBodyPart2.setFileName(filename);  
			     
			    Multipart multipart = new MimeMultipart();  
			    multipart.addBodyPart(messageBodyPart1);  
			    multipart.addBodyPart(messageBodyPart2);  
			  
			    message.setContent(multipart );  
			    
				Transport.send(message);

				System.out.println("message sent successfully...");
		
		}catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
