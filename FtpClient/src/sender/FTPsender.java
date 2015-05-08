package sender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import main.LogException;
import reciever.CommandReciever;

public class FTPsender {

	private CommandReciever rcv;
	private BufferedWriter bw;

	public FTPsender(CommandReciever rcv, BufferedWriter bw) {
		this.rcv = rcv;
		this.bw = bw;
	}

	public boolean login(String user, String pass) throws InterruptedException, LogException, IOException {
		sendCommand("USER " + user);	

		String response = "start";
		while (!response.startsWith("331")){
			Thread.sleep(200);
			response = rcv.getReply();
			if (response.startsWith("530")){
				throw new LogException("FTP klienten modtog forkert respons fra server efter brugernavn blev indtastet: "+ response);
			}
		}
		sendCommand("PASS " + pass);
		
		while (!response.startsWith("230")){
			response = rcv.getReply();
			Thread.sleep(200);
			if (response.startsWith("5")){
				throw new LogException("Ingen adgang med det givne password: "+ pass);
			}
		}
		return true;
	}
	
	public ArrayList<String> getDataLIST(String host, int port) throws IOException{
		ArrayList<String> fileList = new ArrayList<String>();
		try (Socket datasocket = new Socket(host, port);
			 BufferedReader br = new BufferedReader(new InputStreamReader(datasocket.getInputStream()));){
		fileList.add("**************Start***************");
        String line = null;
        while ((line = br.readLine()) != null) {
            fileList.add(line);
        }
        fileList.add("**************Slut****************");
		}
		return fileList;
	}
	
	public String[] parsePASV() throws IOException{
		String pasvReturn = rcv.getReply();
		int time = 0;
		while (!pasvReturn.startsWith("227")){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			pasvReturn = rcv.getReply();
			time++;
			if (time>20){
				throw new IOException("Serveren timede ud. :(");
			}
		}
		int start = 0, end = 0;
		while (pasvReturn.charAt(start) != '('){
			start++;
		}
		end = start +1;
		while (pasvReturn.charAt(end) != ')'){
			end++;
		}
		String[] temp = pasvReturn.substring(start+1, end).split(",");
		String ip = temp[0] + "." + temp[1] + "." + temp[2] + "." + temp[3];
		int port = Integer.parseInt(temp[4]) * 256 + Integer.parseInt(temp[5]);
		return new String[]{ip, Integer.toString(port)};
	}
	
	public void sendCommand(String cmd) throws IOException{
		bw.write(cmd);
		bw.newLine();
		bw.flush();
	}

}
