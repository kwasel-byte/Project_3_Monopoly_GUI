import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MonopolyBoardView extends GridPane {

    private final Board board;
    private final Player player1;
    private final Player player2;

    public MonopolyBoardView(
            Board board, Player player1, Player player2) {

        this.board = board;
        this.player1 = player1;
        this.player2 = player2;

        setAlignment(Pos.CENTER);
        setHgap(4);
        setVgap(4);
        setPadding(new Insets(10));

        updateBoard();
    }

    public void updateBoard() {
        getChildren().clear();

        for (int i = 0; i < board.getBoardSize(); i++) {

            Property property = board.getProperty(i);

            Label nameLabel = new Label(
                    i + ": " + property.getName());
            nameLabel.setWrapText(true);

            Label ownerLabel = new Label();

            if (board.isChance(i)) {
                ownerLabel.setText("CHANCE");
            } else if (board.isCommunityChest(i)) {
                ownerLabel.setText("COMMUNITY CHEST");
            } else {
                ownerLabel.setText(
                        "Owner: " + property.getOwner()
                                + "\nHouses: " + property.getHouses());
            }

            String players = "";

            if (player1.getLocation() == i) {
                players += "Player 1";
            }

            if (player2.getLocation() == i) {
                players += "Player 2";
            }

            Label playersLabel = new Label(players);
            playersLabel.setStyle(
                    "-fx-font-weight: bold;"
                            + "-fx-text-fill: blue;");

            VBox cell = new VBox(
                    4, nameLabel, ownerLabel, playersLabel);

            cell.setAlignment(Pos.CENTER);
            cell.setPrefSize(120, 100);
            cell.setPadding(new Insets(5));

            String background = "#f2f2f2";

            if (board.isChance(i)) {
                background = "#ffe599";
            } else if (board.isCommunityChest(i)) {
                background = "#c9daf8";
            } else if (property.isOwned()) {
                background = "#d9ead3";
            }

            cell.setStyle(
                    "-fx-background-color: " + background + ";"
                            + "-fx-border-color: black;"
                            + "-fx-border-width: 1;");

            // Position each property around the square
            int row;
            int column;

            if (i <= 4) {
                row = 0;
                column = i;
            } else if (i <= 8) {
                row = i - 4;
                column = 4;
            } else if (i <= 12) {
                row = 4;
                column = 12 - i;
            } else {
                row = 16 - i;
                column = 0;
            }

            add(cell, column, row);
        }

        // Center of the Monopoly board
        Label centerLabel = new Label("KASSIM\nMONOPOLY");
        centerLabel.setAlignment(Pos.CENTER);
        centerLabel.setStyle(
                "-fx-font-size: 26px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: darkred;");

        add(centerLabel, 1, 1, 3, 3);
    }
}
