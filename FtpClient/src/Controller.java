import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import reciever.CommandReciever;
import reciever.DataReciever;
import sender.FTPsender;
import boundary.Menu;


public class Controller {

	private FTPsender send;
	private CommandReciever rcv;
	private DataReciever data;
	private Menu menu;

	public Controller(Menu menu){
		this.send = new FTPsender();
		this.rcv = new CommandReciever(menu);
		this.data = new DataReciever();
		this.menu = menu;
	}

	private void connect(String user, String pass, String host){
		try (Socket socket = new Socket(host, 21);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));){
			Thread readerThread = new Thread(rcv);
			readerThread.run();
			boolean active = true;
			while (active){
				String choice = menu.showMenu();
				switch(choice.toUpperCase()){
				case 1:
				break;
				default:
				}
			}
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void start(){
		String user = menu.getUser();
		String pass = menu.getPass();
		String host = menu.getHost();
		connect(user, pass, host);
	}

}
