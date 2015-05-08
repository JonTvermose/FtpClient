package reciever;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class DataReciever implements Runnable {
	
	private String host, savePath, fileName;
	private int port;
	private CommandReciever rcv;
	
	public DataReciever(String host, int port, String savePath, String fileName, CommandReciever rcv){
		this.host = host;
		this.port = port;
		this.savePath = savePath;
		this.fileName = fileName;
		this.rcv = rcv;
	}

	@Override
	public void run() {
		File fil = new File(savePath + fileName);
		try ( 	Socket datasocket = new Socket(host, port);
				InputStream inputStream = datasocket.getInputStream();
				FileOutputStream outputStream = new FileOutputStream(fil);){	
			String reply = rcv.getReply();
			int time = 0;
			while (!reply.startsWith("150")){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				reply = rcv.getReply();
				time++;
				if (time > 20){ // Venter 2 sekunder på serversvar
					throw new FileNotFoundException();					
				}
			}
			int BufferStoerrelse = 4096;
			byte[] buffer = new byte[BufferStoerrelse];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		} catch (FileNotFoundException e){
			System.out.println("Filen: " + fileName + " blev ikke fundet.");
			fil.delete();
		} catch (IOException e1) {
			System.out.println("Der skete en fejl, filen: " + fileName + " slettes.");
			fil.delete();
		}
		System.out.println("Filen: " + fileName + " er hentet: " + fil.getAbsolutePath());
	}
	

}
