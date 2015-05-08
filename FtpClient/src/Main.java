import reciever.CommandReciever;
import reciever.DataReciever;
import sender.FTPsender;


public class Main {

	public static void main(String[] args) {
		FTPsender send = new FTPsender();
		CommandReciever cmd = new CommandReciever();
		DataReciever data = new DataReciever();

	}

}
