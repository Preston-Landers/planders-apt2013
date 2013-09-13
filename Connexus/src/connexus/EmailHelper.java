package connexus;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
// import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.common.base.CharMatcher;

import connexus.model.Stream;

public class EmailHelper {
	
	public static void sendStreamCreateInvitation(Stream stream, String subscribersStr, String subscribersNote) {
		List<InternetAddress> toList = new ArrayList<InternetAddress>(); 
		List<InternetAddress> ccList = null; // new ArrayList<InternetAddress>(); 
		String subject = "Come see my new stream on connexus-apt.appspot.com";
		StringBuilder body = new StringBuilder();
		
		body.append("\nA user on connexus-apt.appspot.com has invited you to view a stream!\n\n");
		body.append(stream.getOwnerName() + " has created " + stream.getName() + "\n\n");

		body.append("Vist the stream here:\n\n\thttp://connexus-apt.appspot.com" + stream.getViewURI() + "\n\n\n");
		
		if (subscribersNote != null && subscribersNote.length() > 0) {
			body.append("== Personal message from " + stream.getOwnerName() + " follows ==\n\n");
			body.append(subscribersNote);
			body.append("\n");
		}
		
		List<String> subList = Arrays.asList(subscribersStr.split("\\s*,\\s*")); 
		CharMatcher wspace = CharMatcher.is(' ');

		// Ensure trimmed tags
		for (String subscriberEmail : subList) {
			subscriberEmail = wspace.trimFrom(subscriberEmail);
			if (subscriberEmail.length() > 0) {
				InternetAddress newAddr;
				try {
					newAddr = new InternetAddress(subscriberEmail);
					toList.add(newAddr);
				} catch (AddressException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		System.err.print(body.toString());
		sendEmail(toList, ccList, subject, body.toString());
	}
	
	public static List<InternetAddress> getReportToAddresses() {
		List<InternetAddress> toList = new ArrayList<InternetAddress>();
		
		
//		InternetAddress INSTRUCTOR;
//		try {
//			INSTRUCTOR = new InternetAddress("kamran.ks+aptmini@gmail.com", "Kamran Saleem");
//		} catch (UnsupportedEncodingException e) {
//			System.err.println("Can't get TO address for INSTRUCTOR");
//			e.printStackTrace(System.err);
//			return;
//		}
		//// XXX TODO: add for final DEPLOYMENT
		// toList.add(INSTRUCTOR);

		InternetAddress PRESTON;
		try {
			PRESTON = new InternetAddress("planders+aptmini@gmail.com", "Preston Landers");
			toList.add(PRESTON);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Can't get TO address for INSTRUCTOR");
			e.printStackTrace(System.err);
		}
	
		return toList;
	}
	
	public static void sendEmail(List<InternetAddress> toList, List<InternetAddress> ccList, String subject, String mailBody) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
		InternetAddress from;
		try {
			from = new InternetAddress(Config.productAdminEmail, Config.productName + " Admin");
		} catch (UnsupportedEncodingException e) {
			System.err.println("Can't get FROM address for email");
			e.printStackTrace(System.err);
			return;
		}
		
		try {
		    Message msg = new MimeMessage(session);
		    msg.setSubject(subject);
		    msg.setFrom(from);
		    if (toList != null) {
			    for (InternetAddress addr : toList ) {
				    msg.addRecipient(Message.RecipientType.TO, addr);
			    }
		    }
		    if (ccList != null) {
			    for (InternetAddress addr : ccList ) {
				    msg.addRecipient(Message.RecipientType.CC, addr);
			    	
			    }
		    }
		    msg.setText(mailBody);
		    Transport.send(msg);
		} catch (MessagingException e) {
			System.err.println("ERROR: unable to send mail!!!");
			e.printStackTrace(System.err);
		}
	}
}
