package yappr;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;

import ymsg.network.Session;
import ymsg.network.event.SessionListener;
import ymsg.network.event.SessionChatEvent;
import ymsg.network.event.SessionConferenceEvent;
import ymsg.network.event.SessionErrorEvent;
import ymsg.network.event.SessionEvent;
import ymsg.network.event.SessionExceptionEvent;
import ymsg.network.event.SessionFileTransferEvent;
import ymsg.network.event.SessionFriendEvent;
import ymsg.network.event.SessionNewMailEvent;
import ymsg.network.event.SessionNotifyEvent;
import ymsg.support.MessageDecoder;

public class MySessionListener implements ymsg.network.event.SessionListener {

	public void buzzReceived(SessionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void chatConnectionClosed(SessionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void chatLogoffReceived(SessionChatEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void chatLogonReceived(SessionChatEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void chatMessageReceived(SessionChatEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}					

	public void chatUserUpdateReceived(SessionChatEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void conferenceInviteDeclinedReceived(SessionConferenceEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void conferenceInviteReceived(SessionConferenceEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void conferenceLogoffReceived(SessionConferenceEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void conferenceLogonReceived(SessionConferenceEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void conferenceMessageReceived(SessionConferenceEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void connectionClosed(SessionEvent arg0) {
		// TODO Auto-generated method stub
		// just try to reconnect
		System.out.println("Connection was closed - attempting to reconnect.");
		Daemon.connectToYahooMessenger();

	}

	public void contactRejectionReceived(SessionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void contactRequestReceived(SessionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void errorPacketReceived(SessionErrorEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void fileTransferReceived(SessionFileTransferEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void friendAddedReceived(SessionFriendEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void friendRemovedReceived(SessionFriendEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void friendsUpdateReceived(SessionFriendEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void inputExceptionThrown(SessionExceptionEvent arg0) {
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void listReceived(SessionEvent arg0) {
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}

	public void messageReceived(SessionEvent arg0) {
		Session ymsgSession = Daemon.getSession();
		StringBuffer ret = new StringBuffer();
		
		MessageDecoder md = new MessageDecoder();
		Date now = new Date();
		
		String userMesg = md.decodeToText(arg0.getMessage());
		
		// this is a hack since Yahoo doesn't send an end typing notification but only relays the message.. 
		Daemon.lastSentNotificationType = 0;

		Pattern message = Pattern.compile("^\\s*/(\\w+)(\\s+([\\w\\.\\:\\-]+))?");
		Matcher matcher = message.matcher(userMesg);
		
		String command = "";
		String arg = "";
		while(matcher.find()) {
			command = matcher.group(1);
			arg = matcher.group(3);
		}	
		
//		System.out.println(msg.toString());
		
		if(command.equals("who")) {
			msgReceivedWho(arg0.getFrom(), ymsgSession);
		} else if(command.equals("hide")) {
			if(arg!= null) {
				relayMsg(arg0.getFrom(), "["+arg0.getFrom()+" made a hidden statement; do /show X to show (to everyone!) the last X hidden statements]", ymsgSession);
				Daemon.hiddenMessageStore.addMessage(new Message(0, arg0.getFrom(), arg));
			}
		} else if(command.equals("show")) {
			int count = 2;
			if(arg != null) {
				count = Integer.parseInt(arg);
			}
			sendReceivedRecentHidden(arg0.getFrom(), count);
		} else if(command.equals("rand")) {
			int randomNumber;
			int max = 10;
			Random generator = new Random();
			if(arg != null) {
				max = Integer.parseInt(arg);
			} 
			randomNumber = generator.nextInt(max) + 1;
			relayMsg("", "[User " + arg0.getFrom() + " rolled " + randomNumber + " out of " + max +"]", ymsgSession);
		} else if(command.equals("typ")) {
			Daemon.doNotification = !Daemon.doNotification;
			relayMsg("", "[User "+arg0.getFrom()+" just changed typing notifications to " + Daemon.doNotification + "]", ymsgSession);
		} else if(command.equals("echo")) {
			Daemon.echoMessages = !Daemon.echoMessages;
			relayMsg("", "[User "+arg0.getFrom()+" just changed echo effect to " + Daemon.echoMessages + "]", ymsgSession);
		} else if(command.equals("delay")) {			
			if(arg!= null) {
				Daemon.pauseBetweenSends = Long.parseLong(arg);
				relayMsg("", "[User "+arg0.getFrom()+" just changed delay timing to " + Daemon.pauseBetweenSends + "ms]", ymsgSession);
			}
		} else if(command.equals("jn")) {
			relayMsg(arg0.getFrom(), "[User "+arg0.getFrom()+" just joined the chat]", ymsgSession);
			msgReceivedAdd(arg0.getFrom(), ymsgSession);
		} else if(command.equals("add")) {
			if(arg!= null) {
				relayMsg(arg0.getFrom(), "[User "+arg0.getFrom()+" added " + arg + " to the chat]", ymsgSession);
				msgReceivedAdd(arg, ymsgSession);
			}
		} else if(command.equals("lv")) {
			relayMsg(arg0.getFrom(), "[User "+arg0.getFrom()+" is leaving the chat]", ymsgSession);
			msgReceivedRem(arg0.getFrom(), ymsgSession);
		} else if(command.equals("kick")) {
			if(arg!= null) {
				relayMsg(arg0.getFrom(), "[User "+arg0.getFrom()+" kicked " + arg + " chat]", ymsgSession);
				msgReceivedRem(arg, ymsgSession);
			}
		} else if(command.equals("help")) {
			msgReceivedHelp(arg0.getFrom(), ymsgSession);
		} else if(command.equals("rec")) {
			int count = 10;
			if(arg != null) {
				count = Integer.parseInt(arg);
			}
			sendReceivedRecent(arg0.getFrom(), count);
		} else {
			NumberFormat nf=NumberFormat.getInstance(); // Get Instance of NumberFormat
			nf.setMinimumIntegerDigits(2);  // The minimum Digits required is 5
			nf.setMaximumIntegerDigits(2); // The maximum Digits required is 5
			int cur = Daemon.getNextInt();
			String number=(nf.format(cur));

			// log it - fourth parameter sets whether or not it's hidden (0 - not hidden, 1 - hidden)
			Daemon.messageStore.addMessage(new Message(cur, arg0.getFrom(), userMesg));
			relayMsg(arg0.getFrom(), "<b>" + number + "</b> " + arg0.getFrom() + "> " + userMesg, ymsgSession);
		}
	}

	// sends a msg to all users on the channel
	private void relayMsg(String from, String msg, Session ymsgSess) {
		try {
			ArrayList<String> users = Daemon.getUsers();
			for(String user : users) {
				if(user.equals(from) && !Daemon.echoMessages) {
					continue;
				}
				ymsgSess.sendMessage(user, msg);
				Thread.sleep(Daemon.pauseBetweenSends);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

//	private void msgReceivedRecent(String from, int count) {
//		try {
//			Session ses = Daemon.getSession();
//			String msg = Daemon.getRecentMessage(count);
//			ses.sendMessage(from, "[Last " + count + " messages on "+Daemon.getYahooID()+"]\n" + msg);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	private void sendReceivedRecent(String from, int count) {
		try {
			int MAXLENGTH = 900;
			ymsg.network.Session ses = Daemon.getSession();
			ArrayList<Message> recent = Daemon.messageStore.getRecentMessages(count);
			StringBuffer sb = new StringBuffer();
			sb.append("[Last "+count+" messages on "+Daemon.getYahooID()+"]\n");
			int numsent = 0;
			for (Message msg : recent) {
				if (sb.length()+msg.getFormattedMessage().length()>MAXLENGTH) {
					ses.sendMessage(from, sb.toString());
					sb.delete(0, sb.length());
					Thread.sleep(250);
				}
				sb.append(msg.getFormattedMessage());
				sb.append("\n");
				numsent++;
			}
			if (sb.length()>0) {
				ses.sendMessage(from, sb.toString());
			}
			if (numsent==0) {
				ses.sendMessage(from, "I didn't find any recent messages.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void sendReceivedRecentHidden(String from, int count) {
		try {
			int MAXLENGTH = 900;
			ymsg.network.Session ses = Daemon.getSession();
			ArrayList<Message> recent = Daemon.hiddenMessageStore.getRecentMessages(count);
			StringBuffer sb = new StringBuffer();
			sb.append("[Last "+count+" hidden messages on "+Daemon.getYahooID()+" as requested by "+from+"]\n");
			int numsent = 0;
			for (Message msg : recent) {
				if (sb.length()+msg.getFormattedMessage().length()>MAXLENGTH) {
					relayMsg("", sb.toString(), Daemon.getSession());
					sb.delete(0, sb.length());
					Thread.sleep(250);
				}
				sb.append(msg.getFormattedMessage());
				sb.append("\n");
				numsent++;
			}
			if (sb.length()>0) {
				relayMsg("", sb.toString(), Daemon.getSession());
			}
			if (numsent==0) {
				relayMsg("", "I didn't find any recent messages.", Daemon.getSession());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	private void msgReceivedWho(String from, Session ymsgSess) {
		try {
			ArrayList<String> users = Daemon.getUsers();
			StringBuffer usermsg  = new StringBuffer();				
			for(String user : users) {
				usermsg.append(user + " ");
			}
			ymsgSess.sendMessage(from, "[Users on channel " + Daemon.getYahooID() + "] " + usermsg.toString());

		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	private void msgReceivedAdd(String from, Session ymsgSess) {
		try {
			if (Daemon.addUser(from)) {
				ymsgSess.sendMessage(from, "[Adding user "+from+" to channel "+Daemon.getYahooID()+"]");
			} else {
				ymsgSess.sendMessage(from, "[User "+from+" already exists on channel "+Daemon.getYahooID()+"]");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void msgReceivedRem(String from, Session ymsgSess) {
		try {
			if (Daemon.removeUser(from)){
				ymsgSess.sendMessage(from, "[Removed user "+from+" from channel "+Daemon.getYahooID()+"]");
			} else {
				ymsgSess.sendMessage(from, "[User "+from+" does not exist on channel "+Daemon.getYahooID()+"]");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void msgReceivedHelp(String from, Session ymsgSess){
		try {
			String helpmsg = "[Help for " + Daemon.getYahooID()+"]:\n"+
"/help    : Show this help.\n"+
"/who     : Show who's listening.\n"+
"/jn      : Join the channel.\n"+
"/lv      : Leave the channel. (You can still send but won't receive.)\n"+
"/rec n   : Recall the last n messages.\n"+
"/typ     : Toggle typing notification. (Set false if dropping messages.)\n"+
"/echo    : Toggle echoing of own messages.\n"+
"/delay s : Insert a delay of s milliseconds between transmission to each user.\n"+
"/hide X  : Place a 'sealed bid'. Don't reveal X till someone types /show.\n"+
"/show n  : Reveal (to everyone!) the last n hidden messages.\n";
			ymsgSess.sendMessage(from, helpmsg);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void newMailReceived(SessionNewMailEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + ": " + arg0.getSubject());
		if(arg0.getFrom() != null && arg0.getSubject() != null)
			relayMsg("", "[Mail: <" + arg0.getFrom() + "> " + arg0.getSubject() + "]", Daemon.getSession());
	}

	public void notifyReceived(SessionNotifyEvent arg0) {
		if(arg0.isTyping() && Daemon.doNotification) {
	//		System.out.println(arg0.getMode() + " " + Daemon.lastSentNotificationType);
			if((arg0.getMode() ^ Daemon.lastSentNotificationType) == 1) {
				Daemon.lastSentNotificationType = arg0.getMode();
				this.relayNotification(arg0.getFrom(), arg0.getMode());
			}
		}
	}
	
	// send a typing notification to everyone except who it's from
	private void relayNotification(String from, int isTyping) {
		Session ymsgSession = Daemon.getSession();
		try {
			ArrayList<String> users = Daemon.getUsers();
			for(String user : users) {
				if(user.equals(from)) {
					continue;
				}
				if(isTyping == 0) {
					ymsgSession.sendTypingNotification(user, false);
				} else {
					ymsgSession.sendTypingNotification(user, true);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void offlineMessageReceived(SessionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getFrom() + " " + arg0.getMessage());

	}
	
	private String strip(String msg) {
		msg = msg.replaceAll("\\<[^\\>]*\\>", "");
		msg = msg.replaceAll("\\033\\[.*[\\d;]+m", "");
		msg = msg.replaceAll("mailto:", "");
		return msg;
	}
}
