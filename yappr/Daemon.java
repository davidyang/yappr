package yappr;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import ymsg.network.Session;

public class Daemon {

	public static HashSet<String> users;
	public static Session ymsgSession;
	public static String propsFilename = null;
	
	public static String username = "beeplusdee";
	public static String password = "insecure";
	public static String userlist = "pegarmpaul,lzhets";
	
	public static boolean doNotification = false;	
	public static int lastSentNotificationType = 0; // 0 = haven't sent out
													// typing, 1 means currently
													// set to isTyping

	public static boolean echoMessages = true;
	public static long pauseBetweenSends = 50;		// in ms

	public static Properties properties = null;

	public static int counter;

	public static MessageStore messageStore = null;
	public static MessageStore hiddenMessageStore = null;
	
	public static String join(String token, String[] strings) {
		StringBuffer sb = new StringBuffer();

		for (int x = 0; x < (strings.length - 1); x++) {
			sb.append(strings[x]);
			sb.append(token);
		}
		sb.append(strings[strings.length - 1]);

		return (sb.toString());
	}

	public static ArrayList<String> getUsers() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.addAll(users);
		return ret;
	}

	public static void changeAndSavePropsFile(String key, String value) {
		try {
			FileOutputStream fos = new FileOutputStream(propsFilename + ".properties");
			properties.setProperty(key, value);
			System.out.println(properties.get("userlist"));
			properties.store(fos, null);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static String getUserList() {
		String[] userString = new String[users.size()]; 
		users.toArray(userString);
		System.out.println(Daemon.join(",", userString));
		return Daemon.join(",", userString);
	}
	
	public static boolean addUser(String newuser) {
		boolean added = users.add(newuser);
		if (added) {
			changeAndSavePropsFile("userlist", getUserList());
		}
		return added;
	}

	public static boolean removeUser(String remuser) {
		boolean removed = users.remove(remuser);
		if (removed) {
			changeAndSavePropsFile("userlist", getUserList());
		}
		return removed;
	}

	public static int getNextInt() {
		counter++;
		counter %= 100;
		return counter;
	}

	public static String getYahooID() {
		return username;
	}

	public static Session getSession() {
		return ymsgSession;
	}

	public static void connectToYahooMessenger() {
		try {
			ymsgSession.login(username, password);
		} catch (IllegalStateException e) {
			System.out.println("already logged in");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0); // exit and hopefully the bot will catch it
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		messageStore = new MessageStore(1000, 20);
		hiddenMessageStore = new MessageStore(50, 10);
		
		if (args.length == 0) {
			System.out.println("Please provide [instance_name] where instance_name is the first part of your instance_name.properties file");
			return;
		} else {
			propsFilename = args[0];
		}

		System.out.println(System.getProperty("user.dir"));
		properties = new Properties();
		try {
			properties.load(new FileInputStream(propsFilename + ".properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		username = properties.getProperty("yahoo_id");
		password = properties.getProperty("password");
		userlist = properties.getProperty("userlist");

		ymsgSession = new Session();

		MySessionListener mySessionListener = new MySessionListener();
		ymsgSession.addSessionListener(mySessionListener);

		connectToYahooMessenger();

		users = new HashSet<String>();

		String[] propusers = null;
		propusers = userlist.split(",");
		for (String user : propusers) {
			users.add(user);
		}

		while (true) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
