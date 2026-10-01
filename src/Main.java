import java.util.Random;
import java.util.Scanner;


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

        System.out.println(player.getName() + " Start with $" + player.getMoney() + ".");
        System.out.println("The board has " + board.getBoardSize() + " properties.");

        Random random = new Random();
        boolean playing = true;
        while (playing) {

            System.out.println();
            System.out.println("-----" + player.getName() + "'s turn ----");

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
            System.out.println("Play another turn? YES or NO: ");
            String playAgain = keyboard.nextLine();
            if (!playAgain.equalsIgnoreCase("YES")) {
                playing = false;
            }
            if (playing){
                if (player == player1) {
                    player = player2;
                } else {
                    player = player1;
                }
            }
        }






    }
};