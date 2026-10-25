import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MonopolyGUI extends Application {

    private int currentTurn = 0;
    private Player lastPlayer = null;
    private int kassimDoubles = 0;
    private int waselDoubles = 0;

    @Override
    public void start(Stage stage) {

        Label title = new Label("Welcome to Kassim Monopoly!");

        Player player1 = new Player("Kassim");
        Player player2 = new Player("Wasel");
        Board board = new Board();

        Label player1Info = new Label("Kassim - $1500");
        Label player2Info = new Label("Wasel - $1500");

        Label player1Position = new Label("Kassim is at position 0");
        Label player2Position = new Label("Wasel is at position 0");

        Button rollButton = new Button("Roll Dice");
        Button buyButton = new Button("Buy Property");
        Button jailButton = new Button("Pay $50 to Leave Jail");
        Button saveButton = new Button("Save Game");
        Button loadButton = new Button("Load Game");

        buyButton.setDisable(true);

        // ROLL DICE
        rollButton.setOnAction(e -> {

            Player currentPlayer;
            Player otherPlayer;

            if (currentTurn == 0) {
                currentPlayer = player1;
                otherPlayer = player2;
            } else {
                currentPlayer = player2;
                otherPlayer = player1;
            }

            if (currentPlayer.getInJail()) {
                title.setText(currentPlayer.getName()
                        + " is in jail! Pay $50 to leave.");
                buyButton.setDisable(true);
                lastPlayer = null;
                return;
            }

            // Rolling forfeits the previous purchase opportunity
            buyButton.setDisable(true);
            lastPlayer = null;

            int dice1 = (int) (Math.random() * 6) + 1;
            int dice2 = (int) (Math.random() * 6) + 1;

            boolean isDoubles = (dice1 == dice2);

            if (currentTurn == 0) {
                if (isDoubles) {
                    kassimDoubles++;
                } else {
                    kassimDoubles = 0;
                }
            } else {
                if (isDoubles) {
                    waselDoubles++;
                } else {
                    waselDoubles = 0;
                }
            }

            // THREE DOUBLES = JAIL
            if ((currentTurn == 0 && kassimDoubles >= 3)
                    || (currentTurn == 1 && waselDoubles >= 3)) {

                currentPlayer.setInJail(true);

                if (currentTurn == 0) {
                    kassimDoubles = 0;
                } else {
                    waselDoubles = 0;
                }

                title.setText(currentPlayer.getName()
                        + " rolled doubles 3 times and went to jail!");

                currentTurn = 1 - currentTurn;
                return;
            }

            int total = dice1 + dice2;
            int oldPosition = currentPlayer.getLocation();

            int newPosition = (oldPosition + total)
                    % board.getBoardSize();

            boolean passedGo =
                    oldPosition + total >= board.getBoardSize();

            if (passedGo) {
                currentPlayer.addMoney(200);
            }

            currentPlayer.setLocation(newPosition);
            lastPlayer = currentPlayer;

            Property property = board.getProperty(newPosition);

            String message = currentPlayer.getName()
                    + " rolled " + dice1 + " and " + dice2
                    + " and landed on " + property.getName();

            if (passedGo) {
                message += "\nPassed GO and collected $200!";
            }

            // PROPERTY AND RENT
            if (property.isOwned()) {

                if (!property.getOwner().equals(
                        currentPlayer.getName())) {

                    int rent = property.getRentPrice();

                    currentPlayer.payMoney(rent);
                    otherPlayer.addMoney(rent);

                    message += "\n" + currentPlayer.getName()
                            + " paid $" + rent
                            + " rent to " + otherPlayer.getName();

                } else {
                    message += "\nYou already own this property!";
                }

            } else {
                buyButton.setDisable(false);
                message += "\nThis property costs $"
                        + property.getPurchasePrice();
            }

            if (isDoubles) {
                message += "\nDOUBLES! Roll again!";
            } else {
                currentTurn = 1 - currentTurn;
            }

            title.setText(message);

            player1Info.setText(
                    "Kassim - $" + player1.getMoney()
            );
            player2Info.setText(
                    "Wasel - $" + player2.getMoney()
            );

            player1Position.setText(
                    "Kassim is at position " + player1.getLocation()
            );
            player2Position.setText(
                    "Wasel is at position " + player2.getLocation()
            );
        });

        // BUY PROPERTY
        buyButton.setOnAction(e -> {

            if (lastPlayer == null || buyButton.isDisabled()) {
                title.setText("Roll the dice first!");
                return;
            }

            Property property = board.getProperty(
                    lastPlayer.getLocation()
            );

            if (property.isOwned()) {
                title.setText("Property already owned!");
                buyButton.setDisable(true);
                return;
            }

            if (lastPlayer.getMoney() < property.getPurchasePrice()) {
                title.setText("Not enough money!");
                buyButton.setDisable(true);
                return;
            }

            lastPlayer.payMoney(property.getPurchasePrice());
            property.setOwner(lastPlayer.getName());

            title.setText(lastPlayer.getName()
                    + " bought " + property.getName()
                    + " for $" + property.getPurchasePrice());

            player1Info.setText(
                    "Kassim - $" + player1.getMoney()
            );
            player2Info.setText(
                    "Wasel - $" + player2.getMoney()
            );

            buyButton.setDisable(true);
        });

        // PAY TO LEAVE JAIL
        jailButton.setOnAction(e -> {

            Player currentPlayer =
                    currentTurn == 0 ? player1 : player2;

            if (!currentPlayer.getInJail()) {
                title.setText(currentPlayer.getName()
                        + " is not in jail!");
                return;
            }

            if (currentPlayer.getMoney() < 50) {
                title.setText("Not enough money to leave jail!");
                return;
            }

            currentPlayer.payMoney(50);
            currentPlayer.setInJail(false);

            title.setText(currentPlayer.getName()
                    + " paid $50 and left jail! Roll the dice.");

            player1Info.setText(
                    "Kassim - $" + player1.getMoney()
            );
            player2Info.setText(
                    "Wasel - $" + player2.getMoney()
            );
        });

        // SAVE GAME
        saveButton.setOnAction(e -> {

            try (PrintWriter writer =
                         new PrintWriter("monopoly_gui_save.txt")) {

                writer.println(currentTurn);
                writer.println(kassimDoubles);
                writer.println(waselDoubles);

                writer.println(player1.getMoney());
                writer.println(player1.getLocation());
                writer.println(player1.getInJail());

                writer.println(player2.getMoney());
                writer.println(player2.getLocation());
                writer.println(player2.getInJail());

                for (int i = 0; i < board.getBoardSize(); i++) {
                    writer.println(board.getProperty(i).getOwner());
                }

                writer.println(
                        lastPlayer == player1 ? 1 :
                                lastPlayer == player2 ? 2 : 0
                );
                writer.println(!buyButton.isDisabled());

                title.setText("Game saved successfully!");

            } catch (FileNotFoundException ex) {
                title.setText("Error saving game!");
            }
        });

        // LOAD GAME
        loadButton.setOnAction(e -> {

            File file = new File("monopoly_gui_save.txt");

            if (!file.exists()) {
                title.setText("No saved game found!");
                return;
            }

            try (Scanner reader = new Scanner(file)) {

                int savedTurn = reader.nextInt();
                int savedKassimDoubles = reader.nextInt();
                int savedWaselDoubles = reader.nextInt();

                int money1 = reader.nextInt();
                int position1 = reader.nextInt();
                boolean jail1 = reader.nextBoolean();

                int money2 = reader.nextInt();
                int position2 = reader.nextInt();
                boolean jail2 = reader.nextBoolean();

                String[] owners =
                        new String[board.getBoardSize()];

                reader.nextLine();

                for (int i = 0; i < owners.length; i++) {
                    owners[i] = reader.nextLine();
                }

                int savedLastPlayer = reader.nextInt();
                boolean canBuy = reader.nextBoolean();

                // Apply data only after reading succeeds
                currentTurn = savedTurn;
                kassimDoubles = savedKassimDoubles;
                waselDoubles = savedWaselDoubles;

                player1.setMoney(money1);
                player1.setLocation(position1);
                player1.setInJail(jail1);

                player2.setMoney(money2);
                player2.setLocation(position2);
                player2.setInJail(jail2);

                for (int i = 0; i < owners.length; i++) {
                    board.getProperty(i).setOwner(owners[i]);
                }

                if (savedLastPlayer == 1) {
                    lastPlayer = player1;
                } else if (savedLastPlayer == 2) {
                    lastPlayer = player2;
                } else {
                    lastPlayer = null;
                }

                buyButton.setDisable(
                        !canBuy || lastPlayer == null
                );

                player1Info.setText(
                        "Kassim - $" + player1.getMoney()
                );
                player2Info.setText(
                        "Wasel - $" + player2.getMoney()
                );

                player1Position.setText(
                        "Kassim is at position "
                                + player1.getLocation()
                );
                player2Position.setText(
                        "Wasel is at position "
                                + player2.getLocation()
                );

                title.setText("Game loaded successfully!\n"
                        + (currentTurn == 0 ? "Kassim" : "Wasel")
                        + "'s turn.");

            } catch (Exception ex) {
                title.setText("Error loading saved game!");
            }
        });

        // GUI LAYOUT
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(
                title,
                player1Info,
                player1Position,
                player2Info,
                player2Position,
                rollButton,
                buyButton,
                jailButton,
                saveButton,
                loadButton
        );

        Scene scene = new Scene(layout, 650, 550);

        stage.setTitle("Kassim Monopoly");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

