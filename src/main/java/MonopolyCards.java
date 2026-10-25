import java.util.Random;

public class MonopolyCards {

    private static final Random random = new Random();

    private static final String[] chanceCards = {
            "Bank pays you $100!",
            "Speeding ticket! Pay $50.",
            "You won a contest! Collect $150.",
            "Car repairs! Pay $75.",
            "Lucky day! Collect $200."
    };

    private static final int[] chanceMoney = {
            100, -50, 150, -75, 200
    };

    private static final String[] communityCards = {
            "Doctor's fee! Pay $50.",
            "You received a gift! Collect $100.",
            "School fees! Pay $75.",
            "Tax refund! Collect $150.",
            "Birthday money! Collect $200."
    };

    private static final int[] communityMoney = {
            -50, 100, -75, 150, 200
    };

    public static String drawChance(Player player) {
        int index = random.nextInt(chanceCards.length);
        applyMoney(player, chanceMoney[index]);

        return "CHANCE CARD!\n" + chanceCards[index];
    }

    public static String drawCommunityChest(Player player) {
        int index = random.nextInt(communityCards.length);
        applyMoney(player, communityMoney[index]);

        return "COMMUNITY CHEST CARD!\n"
                + communityCards[index];
    }

    private static void applyMoney(Player player, int amount) {
        if (amount >= 0) {
            player.addMoney(amount);
        } else {
            player.payMoney(-amount);
        }
    }
}
