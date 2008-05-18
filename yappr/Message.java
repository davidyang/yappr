package yappr;

import java.util.Date;

public class Message {
	public String user;
	public String message;
	public Date timestamp;
	public int number;
	
	public Message(int number, String user, String message) {
		this.user = user;
		this.number = number;
		this.message = message;
		timestamp = new Date();
	}
	
	public String getFormattedMessage() {
		return "<b>" + number + "</b> " + user + "> " + message;
	}
}
