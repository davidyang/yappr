package yappr;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageStore {

	private ArrayList<Message> messages;
	private int counter;
	private int size;
	private int copySize;
	
	public MessageStore(int size, int copySize) {
		messages = new ArrayList<Message>(size);
		for(int i=0; i<size; i++){
			messages.add(new Message(0,"",""));
		}
		counter = 0;
		this.size = size;
		this.copySize = copySize;
	}
	
	public ArrayList<Message> getRecentMessages(int num) {
		// return num recent messages
		if(num > counter) // can't return more than our memory, probably should throw some kind of exception
			num = counter;
		
		int endIndex = counter - 1;
		int startIndex = counter - num;
		if(startIndex < 0) 
			startIndex = 0;
		
		ArrayList<Message> ret = new ArrayList<Message>(num);
		for(int i = startIndex; i<=endIndex; i++) {
			ret.add(messages.get(i));
		}
		return ret;
	}
	
	public void addMessage(Message msg) {
		messages.set(counter, msg);
		counter++;
		// reset this and copy over some basic stuff
		if(counter == size) {
			for(int i=0; i<copySize; i++) {
				messages.set(i, messages.get(size-copySize+i));
			}
			counter = copySize;
		}
	}
	
}
