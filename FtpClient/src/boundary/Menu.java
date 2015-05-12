package boundary;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
	
	private Scanner scan;
	
	public Menu(){
		this.scan = new Scanner(System.in);
	}

	public void login(boolean b) {
		System.out.println("****************************");
		System.out.println("*** Login " + (b ? "lykkedes. ***":"mislykkedes. ***"));
		System.out.println("****************************");
	}

	public String showMenu() {
		System.out.println("****************************************************");
		System.out.println("MENU - Godtagne kommandoer:");
		System.out.println("> LIST");
		System.out.println("> RETR");
		System.out.println("> QUIT");
		System.out.println("> MAX_VALUE");
		System.out.println("> Eventuel anden indtastning sendes til server. (CWD, CDUP, etc.)");
		System.out.println("****************************************************");
		return scan.nextLine();
	}

	public String getUser() {
		System.out.println("Indtast brugernavn:");
		return scan.nextLine();
	}

	public String getPass() {
		System.out.println("Indtast password:");
		return scan.nextLine();
	}

	public String getHost() {
		System.out.println("Indtast host:");
		return scan.nextLine();
	}

	public void show(String readLine) {
		System.out.println("< " + readLine);
	}

	public void showFiles(ArrayList<String> list) {
		for (String file : list){
			System.out.println(file);
		}
	}

	public String getFileName() {
		System.out.println("");
		System.out.println("Indtast filnavn: (f.eks. \"license.txt\")");
		return scan.nextLine();
	}

	public String getSavePath() {
		System.out.println("");
		System.out.println("Indtast savePath: (f.eks. \"C:/Users/Jon/Desktop/\")");
		return scan.nextLine();
	}

	public void exit() {
		System.out.println("*****************************");
		System.out.println("*** Programmet afsluttes. ***");	
		System.out.println("*****************************");
	}

	public String getReadPath() {
		System.out.println("");
		System.out.println("Indtast readPath: (f.eks. \"C:/Users/Jon/Desktop/maxnumber.txt\")");
		return scan.nextLine();
	}

}
