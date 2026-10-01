import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;



public class Main {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Welcome to Kassim Monopoly!");
        System.out.println("Enter your name: ");
        String playerName = keyboard.nextLine();

        Player  player1 = new Player(playerName);
        System.out.println("Enter player 2 name: ");
        String player2Name = keyboard.nextLine();
        Player player2 = new Player(player2Name);

        Player player = player1;
        Board board = new Board();
        System.out.print("Load the saved game? (yes/no): ");
        String loadChoice = keyboard.nextLine();

        if (loadChoice.equalsIgnoreCase("yes")) {
            loadGame(player1, player2, board);
        }

        System.out.println(player.getName() + " Start with $" + player.getMoney() + ".");
        System.out.println("The board has " + board.getBoardSize() + " properties.");

        Random random = new Random();
        boolean playing = true;
        int doublesInARow = 0;

        while (playing) {

            System.out.println();
            System.out.println("-----" + player.getName() + "'s turn ----");

            if (player.getInJail()) {
                System.out.println(player.getName() + " is a jail.");
                player.payMoney(50);
                player.setInJail(false);
                System.out.println(player.getName() + " Paid $50  and left jail.");
            }

            int diec1 = random.nextInt(6) + 1;
            int diec2 = random.nextInt(6) + 1;
            int spaces = diec1 + diec2;

            boolean rolledDouble = diec1 ==  diec2;

            if (rolledDouble) {
                doublesInARow = doublesInARow + 1;
                System.out.println("You rolled doubles!");
            } else {
                doublesInARow = 0;
            }
            if (doublesInARow == 3) {
                player.setInJail(true);
                doublesInARow = 0;
                System.out.println("Three doubles " + player.getName() + " goes to jail.");
                if (player == player1) {
                    player = player2;
                }  else {
                    player = player1;
                }
                continue;
            }
            System.out.println("The First Roll is " + diec1 + " The Second Roll is " + diec2 + ".");
            System.out.println("Move " + spaces + " spaces.");
            int newLocation = player.getLocation() + spaces;
            if (newLocation >= board.getBoardSize()) {
                newLocation = newLocation - board.getBoardSize();
                player.addMoney(200);
                System.out.println("You know get %200");
            }
            player.setLocation(newLocation);

            Property landProprty = board.getProperty(player.getLocation());
            System.out.println("Your On " + landProprty.getName() + ".");

            if (!landProprty.isOwned()) {
                System.out.println("property costs $" + landProprty.getPurchasePrice() + ".");
                System.out.println("Do you what to buy(Yes or No)? ");
                String buyChoice = keyboard.nextLine();

                if (buyChoice.equalsIgnoreCase("Yes")) {
                    if (player.getMoney() >= landProprty.getPurchasePrice()) {
                        player.payMoney(landProprty.getPurchasePrice());
                        landProprty.setOwner(player.getName());
                        System.out.println("You now have $" + landProprty.getPurchasePrice() + ".");
                        System.out.println("Money Left: $" + player.getMoney());
                    } else {
                        System.out.println("dont have enough money.");
                    }
                }
            } else if (landProprty.getOwner().equals(player.getName())) {
                System.out.println("Already own This ");
            } else {
                int rent = landProprty.getRentPrice();

                System.out.println("This property belongs to " + landProprty.getOwner() + ".");
                System.out.println("You paid $" + rent + " in rent.");

                player.payMoney(rent);

                if (landProprty.getOwner().equals(player1.getName())) {
                    player1.addMoney(rent);
                } else {
                    player2.addMoney(rent);
                }
            }
            if (player.getMoney() <= 0) {
                System.out.println(player.getName() + " has no money.");
                board.releaseProperty(player.getName());
                System.out.println("All of " + player.getName() + "'s properties are unowned.");
                playing = false;
            }

            if (playing && !rolledDouble) {
                System.out.println("Play another turn? yes or no: ");
                String playAgain = keyboard.nextLine();

                if (!playAgain.equalsIgnoreCase("yes")) {
                    playing = false;
                }
            }

            saveGame(player1, player2, board);

            if (playing && !rolledDouble) {
                if (player == player1) {
                    player = player2;
                } else {
                    player = player1;
                }
            }

        }
    }
    public static void saveGame(Player player1, Player player2, Board board) {
        try {
            PrintWriter writer = new PrintWriter(new File("monopoly_save.txt"));

            writer.println(player1.getMoney());
            writer.println(player1.getLocation());
            writer.println(player1.getInJail());
            writer.println(player2.getMoney());
            writer.println(player2.getLocation());
            writer.println(player2.getInJail());

            for (int i = 0; i < board.getBoardSize(); i++) {
                writer.println(board.getProperty(i).getOwner());
            }
            writer.close();
            System.out.println("Game saved.");

        } catch (FileNotFoundException e) {
            System.out.println("Could not save the game.");
        }
    }
    public static void loadGame(Player player1, Player player2, Board board) {
        try {
            Scanner fileScanner = new Scanner(new File("monopoly_save.txt"));

            player1.setMoney(fileScanner.nextInt());
            player1.setLocation(fileScanner.nextInt());
            player1.setInJail(fileScanner.nextBoolean());

            player2.setMoney(fileScanner.nextInt());
            player2.setLocation(fileScanner.nextInt());
            player2.setInJail(fileScanner.nextBoolean());

            fileScanner.nextLine();

            for (int i = 0; i < board.getBoardSize(); i++) {
                String owner = fileScanner.nextLine();
                board.getProperty(i).setOwner(owner);
            }

            fileScanner.close();
            System.out.println("Saved game loaded.");

        } catch (FileNotFoundException e) {
            System.out.println("No saved game was found.");
        }
    }
};