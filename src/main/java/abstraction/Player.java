package abstraction;

/**
 * This class corresponds to all elements that are related to a player in a CY-PATH game.
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class Player {
    /**
     * State the Player's class attributes
     */

	private String username;
    private int nbWins;

    /**
     * Create a player from his username
     * 
     * @param username Name of the player
     */

    public Player(String username){
        this.username = username;
        this.nbWins = 0;
    }


    /**
     * Get the username of a player
     * 
     * @return Username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the numbers of wins of a player
     * 
     * @return Number of game won of the player
     */
    public int getNbWins() {
        return nbWins;
    }

    /**
     * Set the number of wins to a player
     * 
     * @param nbWin Number of winning game
     */
    public void setNbWins(int nbWin) {
        this.nbWins = nbWin;
    }

    /**
     * Redefine 'toString' method of the Object class to display the username of a player
     * 
     * @return String to display the user's username
     */
    @Override
    public String toString() {
        return this.getUsername();
    }
}
