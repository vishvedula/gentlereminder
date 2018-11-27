	package com.setreminder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class ReminderJob implements Job {
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {

			/* Connect to MongoDB */

			MongoClient mongo = new MongoClient("localhost", 27017);
			DB db = mongo.getDB("test");
			DBCollection collection = db.getCollection("setReminder");
			List<String> list = new ArrayList<String>();
			DBCursor cursor = collection.find();

			while (cursor.hasNext()) {
				/*
				 * Using JSONObject to fetch each json document and filter based
				 * on the Key
				 */
				JSONObject jsonObj = new JSONObject(cursor.next().toString());
				list.add((String) jsonObj.get("reminder"));
			}

			/* Prepare the Mail content using Java Mail */
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.mail.yahoo.com"); // for gmail use
																// smtp.gmail.com
			props.put("mail.smtp.auth", "true");
			props.put("mail.debug", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");

			Session mailSession = Session.getInstance(props,
					new javax.mail.Authenticator() {

						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(
									"vishvedula@yahoo.com", "Vish_Yahoo@123");
						}
					});

			mailSession.setDebug(true); // Enable the debug mode

			Message msg = new MimeMessage(mailSession);

			// --[ Set the FROM, TO, DATE and SUBJECT fields
			msg.setFrom(new InternetAddress("vishvedula@yahoo.com"));
			
			msg.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse("vishvedula@gmail.com"));
			
			//To send to more than 1 recipient
			/*msg.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse("vinvedula@gmail.com"));*/

			msg.setSentDate(new Date());
			msg.setSubject("REMINDER list from Vishal's java application !!");

			// --[ Create the body of the mail

			String message ="";
			/*message = "<i>Reminders List!</i><br>";
	        message += "<b>In a tabular form </b><br>";*/
			
	        message +="<html><body><table width='100%' border='1' align='center'>";
			for (int i = 0; i < list.size(); i++) {
				message+="<tr><td>";
				message+="<font color=red>";
		        message+= list.get(i).toString();
		        message+="</font>";
		        message+="</td></tr>";
			}
			message+="</table></body></html>";
			msg.setContent(message, "text/html"); // To send as HTML  table format (indentation)
			
			// To send as simple text
			//msg.setText(message);
			
			
			// --[ Ask the Transport class to send our mail message
			Transport.send(msg);

		} catch (Exception E) {
			System.out.println("Oops something has gone pearshaped!");
			System.out.println(E);
		}
	}

}
