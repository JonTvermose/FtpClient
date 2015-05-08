package reciever;

import java.io.BufferedReader;
import java.io.IOException;

import boundary.Menu;

public class CommandReciever implements Runnable {

	private Menu menu;
	private BufferedReader br;
	private String reply;

	public CommandReciever(Menu menu, BufferedReader br) {
		this.menu = menu;
		this.br = br;
		reply = "Start";
	}

	@Override
	public void run() {
		System.out.println("Reciever Service started.");
		
		while(reply != null && !reply.startsWith("530") && !reply.startsWith("221")){
			try {
				reply = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			menu.show(reply);
		}
		
		System.out.println("Reciever Service ended.");
	}
	
	public String getReply(){
		return reply;
	}

}
