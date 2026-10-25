import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MonopolyGUI extends Application {

    private int currentTurn = 0;
    private Player lastPlayer = null;
    private int kassimDoubles = 0;
    private int waselDoubles = 0;

    @Override
    public void start(Stage stage) {

        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Board board = new Board();

        // GRAPHICAL BOARD
        MonopolyBoardView boardView =
                new MonopolyBoardView(board, player1, player2);

        Label title = new Label("Welcome to Kassim Monopoly!");
        title.setWrapText(true);
        title.setMaxWidth(340);

        Label player1Info = new Label();
        Label player2Info = new Label();
        Label player1Position = new Label();
        Label player2Position = new Label();

        Button rollButton = new Button("Roll Dice");
        Button buyButton = new Button("Buy Property");
        Button houseButton = new Button("Buy House ($50)");
        Button jailButton = new Button("Pay $50 to Leave Jail");
        Button saveButton = new Button("Save Game");
        Button loadButton = new Button("Load Game");

        buyButton.setDisable(true);
        houseButton.setDisable(true);

        // UPDATE PLAYER STATS AND BOARD
        Runnable updateDisplay = () -> {
            player1Info.setText("Player1 - $" + player1.getMoney());
            player2Info.setText("Player2 - $" + player2.getMoney());

            player1Position.setText(
                    "Player1 is at position " + player1.getLocation());

            player2Position.setText(
                    "Player2 is at position " + player2.getLocation());

            boardView.updateBoard();
        };

        // CHECK WHETHER CURRENT PLAYER CAN BUY HOUSES
        Runnable updateHouseButton = () -> {
            Player player = currentTurn == 0 ? player1 : player2;
            boolean canBuild = false;

            if (!player.getInJail()) {
                for (int i = 0; i < board.getBoardSize(); i++) {
                    Property property = board.getProperty(i);

                    if (property.getOwner().equals(player.getName())
                            && property.canBuildHouse()
                            && board.ownsCompleteSet(
                            player.getName(),
                            property.getColorGroup())
                            && player.getMoney() >= property.getHousePrice()) {
                        canBuild = true;
                        break;
                    }
                }
            }

            houseButton.setDisable(!canBuild);
        };

        updateDisplay.run();
        updateHouseButton.run();

        // ROLL DICE
        rollButton.setOnAction(e -> {

            Player currentPlayer =
                    currentTurn == 0 ? player1 : player2;

            Player otherPlayer =
                    currentTurn == 0 ? player2 : player1;

            if (currentPlayer.getInJail()) {
                title.setText(currentPlayer.getName()
                        + " is in jail! Pay $50 to leave.");
                buyButton.setDisable(true);
                lastPlayer = null;
                updateHouseButton.run();
                return;
            }

            buyButton.setDisable(true);
            lastPlayer = null;

            int dice1 = (int) (Math.random() * 6) + 1;
            int dice2 = (int) (Math.random() * 6) + 1;

            boolean isDoubles = dice1 == dice2;

            if (currentTurn == 0) {
                kassimDoubles = isDoubles ? kassimDoubles + 1 : 0;
            } else {
                waselDoubles = isDoubles ? waselDoubles + 1 : 0;
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
                        + " rolled doubles three times and went to jail!");

                currentTurn = 1 - currentTurn;
                updateDisplay.run();
                updateHouseButton.run();
                return;
            }

            int oldPosition = currentPlayer.getLocation();
            int total = dice1 + dice2;

            int newPosition =
                    (oldPosition + total) % board.getBoardSize();

            boolean passedGo =
                    oldPosition + total >= board.getBoardSize();

            if (passedGo) {
                currentPlayer.addMoney(200);
            }

            currentPlayer.setLocation(newPosition);

            Property property = board.getProperty(newPosition);

            String message = currentPlayer.getName()
                    + " rolled " + dice1 + " and " + dice2
                    + " and landed on " + property.getName();

            if (passedGo) {
                message += "\nPassed GO and collected $200!";
            }

            // CHANCE CARD
            if (board.isChance(newPosition)) {
                message += "\n"
                        + MonopolyCards.drawChance(currentPlayer);

                // COMMUNITY CHEST CARD
            } else if (board.isCommunityChest(newPosition)) {
                message += "\n"
                        + MonopolyCards.drawCommunityChest(currentPlayer);

                // PROPERTY
            } else {
                lastPlayer = currentPlayer;

                if (property.isOwned()) {
                    if (!property.getOwner().equals(
                            currentPlayer.getName())) {

                        int rent = property.getRentPrice();

                        currentPlayer.payMoney(rent);
                        otherPlayer.addMoney(rent);

                        message += "\n" + currentPlayer.getName()
                                + " paid $" + rent
                                + " rent to " + otherPlayer.getName();

                        if (property.getHouses() > 0) {
                            message += "\nHouses: "
                                    + property.getHouses();
                        }

                    } else {
                        message += "\nYou already own this property!";
                        message += "\nHouses: " + property.getHouses();
                    }

                } else {
                    buyButton.setDisable(false);

                    message += "\nThis property costs $"
                            + property.getPurchasePrice();
                }
            }

            if (isDoubles) {
                message += "\nDOUBLES! Roll again!";
            } else {
                currentTurn = 1 - currentTurn;
            }

            title.setText(message);
            updateDisplay.run();
            updateHouseButton.run();
        });

        // BUY PROPERTY
        buyButton.setOnAction(e -> {

            if (lastPlayer == null || buyButton.isDisabled()) {
                title.setText("Roll the dice first!");
                return;
            }

            Property property =
                    board.getProperty(lastPlayer.getLocation());

            if (board.isChance(lastPlayer.getLocation())
                    || board.isCommunityChest(lastPlayer.getLocation())) {

                title.setText("You cannot buy this space!");
                buyButton.setDisable(true);
                return;
            }

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

            buyButton.setDisable(true);
            updateDisplay.run();
            updateHouseButton.run();
        });

        // BUY HOUSE
        houseButton.setOnAction(e -> {

            Player currentPlayer =
                    currentTurn == 0 ? player1 : player2;

            if (currentPlayer.getInJail()) {
                title.setText("You cannot buy houses while in jail!");
                return;
            }

            Property selectedProperty = null;

            for (int i = 0; i < board.getBoardSize(); i++) {
                Property property = board.getProperty(i);

                if (property.getOwner().equals(currentPlayer.getName())
                        && property.canBuildHouse()
                        && board.ownsCompleteSet(
                        currentPlayer.getName(),
                        property.getColorGroup())) {

                    if (selectedProperty == null
                            || property.getHouses()
                            < selectedProperty.getHouses()) {
                        selectedProperty = property;
                    }
                }
            }

            if (selectedProperty == null) {
                title.setText("You must own a complete color set "
                        + "before buying houses!");
                updateHouseButton.run();
                return;
            }

            int housePrice = selectedProperty.getHousePrice();

            if (currentPlayer.getMoney() < housePrice) {
                title.setText("Not enough money to buy a house!");
                updateHouseButton.run();
                return;
            }

            currentPlayer.payMoney(housePrice);
            selectedProperty.addHouse();

            title.setText(currentPlayer.getName()
                    + " bought a house on "
                    + selectedProperty.getName()
                    + " for $" + housePrice
                    + "\nTotal houses: " + selectedProperty.getHouses()
                    + "\nNew rent: $" + selectedProperty.getRentPrice());

            updateDisplay.run();
            updateHouseButton.run();
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

            updateDisplay.run();
            updateHouseButton.run();
        });

        // SAVE GAME
        saveButton.setOnAction(e -> {

            try (PrintWriter writer =
                         new PrintWriter("monopoly_gui_save.txt")) {

                writer.println("MONOPOLY_SAVE_V2");
                writer.println(board.getBoardSize());

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
                    Property property = board.getProperty(i);
                    writer.println(property.getOwner());
                    writer.println(property.getHouses());
                }

                writer.println(
                        lastPlayer == player1 ? 1 :
                                lastPlayer == player2 ? 2 : 0);

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

                String version = reader.nextLine();

                if (!version.equals("MONOPOLY_SAVE_V2")) {
                    title.setText("Old save format detected. "
                            + "Start a new game and save again.");
                    return;
                }

                int savedBoardSize = reader.nextInt();

                if (savedBoardSize != board.getBoardSize()) {
                    title.setText("Saved board does not match "
                            + "the current game.");
                    return;
                }

                int savedTurn = reader.nextInt();
                int savedKassimDoubles = reader.nextInt();
                int savedWaselDoubles = reader.nextInt();

                int money1 = reader.nextInt();
                int position1 = reader.nextInt();
                boolean jail1 = reader.nextBoolean();

                int money2 = reader.nextInt();
                int position2 = reader.nextInt();
                boolean jail2 = reader.nextBoolean();

                String[] owners = new String[savedBoardSize];
                int[] houses = new int[savedBoardSize];

                reader.nextLine();

                for (int i = 0; i < savedBoardSize; i++) {
                    owners[i] = reader.nextLine();
                    houses[i] = reader.nextInt();
                    reader.nextLine();
                }

                int savedLastPlayer = reader.nextInt();
                boolean canBuy = reader.nextBoolean();

                currentTurn = savedTurn;
                kassimDoubles = savedKassimDoubles;
                waselDoubles = savedWaselDoubles;

                player1.setMoney(money1);
                player1.setLocation(position1);
                player1.setInJail(jail1);

                player2.setMoney(money2);
                player2.setLocation(position2);
                player2.setInJail(jail2);

                for (int i = 0; i < savedBoardSize; i++) {
                    Property property = board.getProperty(i);
                    property.setOwner(owners[i]);
                    property.setHouses(houses[i]);
                }

                if (savedLastPlayer == 1) {
                    lastPlayer = player1;
                } else if (savedLastPlayer == 2) {
                    lastPlayer = player2;
                } else {
                    lastPlayer = null;
                }

                boolean specialSpace = lastPlayer != null
                        && (board.isChance(lastPlayer.getLocation())
                        || board.isCommunityChest(lastPlayer.getLocation()));

                buyButton.setDisable(
                        !canBuy || lastPlayer == null || specialSpace);

                updateDisplay.run();
                updateHouseButton.run();

                title.setText("Game loaded successfully!\n"
                        + (currentTurn == 0 ? "Kassim" : "Wasel")
                        + "'s turn.");

            } catch (Exception ex) {
                title.setText("Error loading saved game!");
            }
        });

        // GUI LAYOUT
        VBox controls = new VBox(12);
        controls.setAlignment(Pos.CENTER);

        controls.getChildren().addAll(
                title,
                player1Info,
                player1Position,
                player2Info,
                player2Position,
                rollButton,
                buyButton,
                houseButton,
                jailButton,
                saveButton,
                loadButton
        );

        // BOARD ON LEFT, CONTROLS ON RIGHT
        HBox layout = new HBox(20);
        layout.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(boardView, controls);

        Scene scene = new Scene(layout, 1050, 650);

        stage.setTitle("Kassim Monopoly");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
