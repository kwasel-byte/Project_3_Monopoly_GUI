import jdk.swing.interop.SwingInterOpUtils;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Welcome to Kassim Monopoly!");
        System.out.println("Enter your name: ");
        String playerName = keyboard.nextLine();

        Player  player = new Player(playerName);
        Board board = new Board();

        System.out.println(player.getName() + " Start with $" + player.getMoney() + ".");
        System.out.println("The board has " + board.getBoardSize() + " properties.");



    }
};