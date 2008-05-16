package yappr;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;

import ymsg.network.Session;

public class Daemon {

	public static HashSet<String> users;
	public static Session ymsgSession;
	
	public static String username = "beeplusdee";
	public static String password = "insecure";
	public static String userlist = "pegarmpaul,lzhets";
	
	public static boolean doNotification = true;
	public static int lastSentNotificationType = 0; // 0 = haven't sent out
													// typing, 1 means currently
													// set to isTyping

	public static boolean echoMessages = true;
	public static long pauseBetweenSends = 50;		// in ms

	public static Properties properties = null;
	public static String propsFilename = null;

	public static int counter;

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

	public static boolean addUser(String newuser) {
		boolean added = users.add(newuser);
		if (added) {
			try {
				properties.setProperty("userlist", Daemon.join(",",	(String[]) users.toArray()));
				properties.store(new FileOutputStream(propsFilename + ".properties"), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return added;
	}

	public static boolean removeUser(String remuser) {
		boolean removed = users.remove(remuser);
		if (removed) {
			try {
				properties.setProperty("userlist", Daemon.join(",",	(String[]) users.toArray()));
				properties.store(new FileOutputStream(propsFilename	+ ".properties"), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	public static void addMessage(String user, String msg, int messageId, int is_hidden) {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String propsFilename = null;
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

	public static String getMessagesByDate(Date from, Date to) {
		StringBuilder sb = new StringBuilder();
		//		
		// try {
		//		
		// PreparedStatement getUsers = conn.prepareStatement("select username,
		// visible_id, sent_at, msg from messages WHERE sent_at >= ? AND sent_at
		// <= ? order by sent_at DESC");
		// getUsers.setTimestamp(1, new java.sql.Timestamp(from.getTime()));
		// getUsers.setTimestamp(2, new java.sql.Timestamp(to.getTime()));
		//			
		// ResultSet rs = getUsers.executeQuery();
		//			
		// NumberFormat nf=NumberFormat.getInstance(); // Get Instance of
		// NumberFormat
		// nf.setMinimumIntegerDigits(2); // The minimum Digits required is 5
		// nf.setMaximumIntegerDigits(2); // The maximum Digits required is 5
		//			
		// while(rs.next()) {
		// sb.insert(0, nf.format(rs.getInt("visible_id")) + " <b>" +
		// rs.getString("username") + "</b>: " + rs.getString("msg") + "<br/>");
		// }
		// } catch(Exception e) {
		// e.printStackTrace();
		// }
		//		
		return sb.toString();

	}

	public static String getRecentMessage(int numMessages) {
		StringBuilder sb = new StringBuilder();

		// try {
		// Statement getUsers = conn.createStatement();
		// getUsers.setMaxRows(numMessages);
		// ResultSet rs = getUsers.executeQuery("select username, sent_at, msg
		// from messages order by sent_at DESC");
		// while(rs.next()) {
		// sb.insert(0, "<b>" + rs.getString("username") + "</b>> " +
		// rs.getString("msg") + "\n");
		// }
		// } catch(Exception e) {
		// e.printStackTrace();
		// }

		return sb.toString();

	}

	public static ArrayList<String> getRecentMsgs(int n, int is_hidden) {
		ArrayList<String> msgs = new ArrayList<String>();

		NumberFormat nf = NumberFormat.getInstance(); 	// Get Instance of
														// NumberFormat
		nf.setMinimumIntegerDigits(2); // The minimum Digits required is 5
		nf.setMaximumIntegerDigits(2); // The maximum Digits required is 5
		//		
		// try {
		// Statement getUsers = conn.createStatement();
		// getUsers.setMaxRows(n);
		// ResultSet rs = getUsers.executeQuery("select username, visible_id,
		// sent_at, msg from messages WHERE is_hidden=" + is_hidden + " order by
		// sent_at DESC");
		// while(rs.next()) {
		// msgs.add(0, nf.format(rs.getInt("visible_id")) + "
		// <b>"+rs.getString("username")+"</b>> "+rs.getString("msg")); // add a
		// 0 to reverse order
		// }
		// } catch(Exception e) {
		// e.printStackTrace();
		// }
		return msgs;
	}
}
