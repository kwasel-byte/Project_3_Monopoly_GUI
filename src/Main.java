import java.util.Random;
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

        Random random = new Random();

        int diec1 = random.nextInt(6) + 1;
        int diec2 = random.nextInt(6) + 1;
        int spaces = diec1 + diec2;

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
            String buyCoice = keyboard.nextLine();
            if (buyCoice.equalsIgnoreCase("Yes")) {
                if (player.getMoney() >= landProprty.getPurchasePrice()) {
                    player.payMoney(landProprty.getPurchasePrice());
                    landProprty.setOwner(player.getName());
                    System.out.println("You now have $" + landProprty.getPurchasePrice() + ".");
                    System.out.println("Money Left: $" + player.getMoney());
                } else {
                    System.out.println("dont have enough money.");
                }
            }
        }






    }
};