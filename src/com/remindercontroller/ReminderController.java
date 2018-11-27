package com.remindercontroller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.setreminder.Reminder;

@Controller
public class ReminderController {
	
	
	// POST method
	@RequestMapping(method = RequestMethod.POST, value = "/reminder/add.htm")
	public @ResponseBody
	Reminder add(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Reminder reminder = new Reminder();

		String reminderName = request.getParameter("addReminder");
		if (null != reminderName) {
			this.invokeMongoForAdd(reminderName);
		}
		reminder.setReminder(reminderName);

		return reminder;
	}

	// GET method
	@RequestMapping(method = RequestMethod.GET, value = "/reminder/fetch.htm")
	public @ResponseBody
	List<String> fetch(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<String> list = new ArrayList<String>();
			list = this.invokeMongoForFetch();

		return list;
	}

	//DELETE method
	@RequestMapping(method = RequestMethod.POST, value = "/reminder/delete.htm")
	public @ResponseBody
	String delete(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Boolean isDeleted = false;
			String reminderName = request.getParameter("deleteReminder");
			if (null != reminderName) {
			this.invokeMongoForDelete(reminderName);
			}
		return reminderName;
	}

	// PUT method
	@RequestMapping(method = RequestMethod.POST, value = "/reminder/update.htm")
	
	public @ResponseBody void updateReminder(HttpServletRequest request, HttpServletResponse response){
		String oldReminder = request.getParameter("oldReminder");
		String newReminder = request.getParameter("newReminder");
		this.invokeMongoForUpdate(oldReminder, newReminder);

	}
	
	
	// MongoDB private methods

	private void invokeMongoForAdd(String reminder) {
		MongoClient mongo;
		try {
			mongo = new MongoClient("localhost", 27017);
			DB db = mongo.getDB("test");

			DBCollection table = db.getCollection("setReminder");

			BasicDBObject document = new BasicDBObject();
			document.put("reminder", reminder);
			table.insert(document);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}
	
	
	private  List<String> invokeMongoForFetch() throws UnknownHostException{
		MongoClient mongo = new MongoClient("localhost", 27017);
		try {
			List<String> list = new ArrayList<String>();
			DB db = mongo.getDB("test");
			DBCollection collection = db.getCollection("setReminder");
			
			DBCursor cursor = collection.find();

			while (cursor.hasNext()) {
				/*Using JSONObject to fetch each json document and filter based
				 on the Key*/
				JSONObject jsonObj;
				try {
					jsonObj = new JSONObject(cursor.next().toString());
					list.add((String) jsonObj.get("reminder"));
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		mongo.close();
		return list;
		} finally{
			
		}
	}
	
	private boolean invokeMongoForDelete(String reminderName){
		MongoClient mongo;
		Boolean isDeleted = false;
			try {
				mongo = new MongoClient("localhost", 27017);
				DB db = mongo.getDB("test");
				DBCollection collection = db.getCollection("setReminder");
				
				BasicDBObject document = new BasicDBObject();
				document.put("reminder", reminderName);
				collection.remove(document);
						isDeleted = true;
						return isDeleted;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return isDeleted;
		}

	private void invokeMongoForUpdate(String oldReminder, String newReminder) {

		MongoClient mongo;
		try {
			mongo = new MongoClient("localhost", 27017);
			DB db = mongo.getDB("test");
			DBCollection collection = db.getCollection("setReminder");
			try {

				BasicDBObject oldDocument = new BasicDBObject();
				oldDocument.put("reminder", oldReminder);

				// Create new Document
				BasicDBObject newDocument = new BasicDBObject();
				newDocument.put("reminder", newReminder);

				BasicDBObject updateObj = new BasicDBObject();
				updateObj.put("$set", newDocument);

				// update old document with new One.
				collection.update(oldDocument, newDocument);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}