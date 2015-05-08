package main;
import controller.Controller;
import boundary.Menu;


public class Main {

	public static void main(String[] args) throws InterruptedException {
		Controller control = new Controller(new Menu());
		control.start();

	}

}
