package connexus;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
// import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailHelper {
	public static void sendEmail(String subject, String mailBody) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
		InternetAddress from;
		try {
			from = new InternetAddress("admin@connexus-apt.appspot.com", "Connexus Admin");
		} catch (UnsupportedEncodingException e) {
			System.err.println("Can't get FROM address for email");
			System.err.print(e);
			return;
		}
		
		List<InternetAddress> toList = new ArrayList<InternetAddress>();
		
		
//		InternetAddress INSTRUCTOR;
//		try {
//			INSTRUCTOR = new InternetAddress("kamran.ks+aptmini@gmail.com", "Kamran Saleem");
//		} catch (UnsupportedEncodingException e) {
//			System.err.println("Can't get TO address for INSTRUCTOR");
//			System.err.print(e);
//			return;
//		}
		//// XXX TODO: add for final DEPLOYMENT
		// toList.add(INSTRUCTOR);

		InternetAddress PRESTON;
		try {
			PRESTON = new InternetAddress("planders+aptmini@gmail.com", "Preston Landers");
		} catch (UnsupportedEncodingException e) {
			System.err.println("Can't get TO address for INSTRUCTOR");
			System.err.print(e);
			return;
		}
		toList.add(PRESTON);
		
		try {
		    Message msg = new MimeMessage(session);
		    msg.setSubject(subject);
		    msg.setFrom(from);
		    for (InternetAddress addr : toList ) {
			    msg.addRecipient(Message.RecipientType.TO, addr);
		    }
		    msg.setText(mailBody);
		    Transport.send(msg);
		} catch (MessagingException e) {
			System.err.println("ERROR: unable to send mail!!!");
			System.err.print(e);
		}
	}
}
