package controller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import main.LogException;
import reciever.CommandReciever;
import reciever.DataReciever;
import sender.FTPsender;
import boundary.Menu;


public class Controller {

	private Menu menu;

	public Controller(Menu menu){
		this.menu = menu;
	}

	private void connect(String user, String pass, String host) throws InterruptedException{
		try (Socket socket = new Socket(host, 21);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));){
			
			CommandReciever rcv = new CommandReciever(menu, br);
			Thread readerThread = new Thread(rcv);
			readerThread.start();
			FTPsender send = new FTPsender(rcv, bw);

			if (send.login(user, pass)){
				boolean active = true;
				while (active){
					String choice = menu.showMenu();
					
					switch(choice.toUpperCase()){
					case "LIST":
						send.sendCommand("PASV");
						String[] dataCon = send.parsePASV();
						send.sendCommand("LIST");
						ArrayList<String> list = send.getDataLIST(dataCon[0], Integer.parseInt(dataCon[1]));
						menu.showFiles(list);
						break;
					case "QUIT":
						send.sendCommand("QUIT");
						Thread.sleep(500); // Afventer "godnat-besked" fra serveren.
						active = false;
						menu.exit();
						break;
					case "FILE":
						String fileName = menu.getFileName();
						String savePath = menu.getSavePath();
						send.sendCommand("PASV");
						String[] dataCon2 = send.parsePASV();
						DataReciever data = new DataReciever(dataCon2[0], Integer.parseInt(dataCon2[1]), savePath, fileName, rcv);
						send.sendCommand("RETR " + fileName);
						Thread dataThread = new Thread(data);
						dataThread.start();
						break;
					default:
						send.sendCommand(choice);
					}
				}
			} else {
				menu.login(false);
			}
			readerThread.join();

		} catch (UnknownHostException e) {
			System.out.println("FEJL!: Kunne ikke identificere host.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("FEJL!: " + e.getMessage());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (LogException e) {
			menu.login(false);
		} 
	}

	public void start() throws InterruptedException{
		String user = menu.getUser();
		String pass = menu.getPass();
		String host = menu.getHost();
		connect(user, pass, host);
	}
}