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
                if (!rolledDouble) {
                    System.out.println("Play another turn? yes or no: ");
                    String playAgain  = keyboard.nextLine();

                    if (!playAgain.equalsIgnoreCase("yes")) {
                        playing = false;
                    }
                }
                if (playing && !rolledDouble) {
                    if (player == player1) {
                        player = player2;
                    } else  {
                        player = player1;
                    }
                }




        }






    }
};