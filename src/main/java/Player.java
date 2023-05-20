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
     * @param username (String)
     */

    public Player(String username){
        this.username = username;
        this.nbWins = 0;
    }


    /**
     * Accessor to recover the username of a player
     * 
     * @return (String) username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Accessor to recover the numbers of wins of a player
     * 
     * @return (int) nbWins
     */
    public int getNbWins() {
        return nbWins;
    }

    /**
     * Accessor to assign the number of wins to a player
     * 
     * @param nbWin (int)
     */
    public void setNbWins(int nbWin) {
        this.nbWins = nbWin;
    }

    /**
     * Redefine 'toString' method of the Object class to display the username of a
     * player
     * 
     * @return (String) : a text
     */
    @Override
    public String toString() {
        return this.getUsername();
    }
}
