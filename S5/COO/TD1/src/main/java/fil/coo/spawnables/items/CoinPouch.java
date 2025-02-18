package fil.coo.spawnables.items;

import fil.coo.spawnables.beings.GamePlayer;
import fil.coo.spawnables.interfaces.Item;
import org.apache.log4j.Logger;

import java.util.Optional;
import java.util.Random;

public class CoinPouch extends Item<CoinPouch> {

    private final static Logger logger = Logger.getLogger(CoinPouch.class);

    /**
     * The amount of gold this purse contains.
     */
    private int goldAmount;

    @Override
    protected void applySpecificEffect(GamePlayer player) {
        player.changeGold(goldAmount);
        logger.info("You gained " + goldAmount + " gold and now have " + player.getGold() + " coins in your pouch.");
    }

    @Override
    public int getAmount() {
        return goldAmount;
    }

    @Override
    public void setAmount(int amount) {
        this.goldAmount = amount;
    }

    @Override
    public String getMenuDescription() {
        return "Gold purse: +" + goldAmount + " gold.";
    }


    /**
     * @return a random spawn of {@link CoinPouch} with a random amount of gold inside specified by {@link #getSpawnRate()}
     */
    @Override
    public Optional<CoinPouch> getRandomSpawn() {
        if (willSpawn()) {

            return Optional.of(new CoinPouch()
                    .withGoldAmount(getRandomAmountHeld()));
        }
        return Optional.empty();
    }


    /**
     * @param goldAmount the amount of gold this purse should hold.
     * @return this instance
     */
    private CoinPouch withGoldAmount(int goldAmount) {
        this.setAmount(goldAmount);
        return this;
    }

    @Override
    public int getRandomAmountHeld() {
        return new Random().nextInt(21);
    }

    @Override
    public double getSpawnRate() {
        return 0.2;
    }

    @Override
    public boolean willSpawn() {
        return new Random().nextDouble() <= getSpawnRate();
    }

}
