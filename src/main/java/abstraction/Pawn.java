package abstraction;

/**
 * This class corresponds to all elements that are related to a pawn in a CY-PATH game.
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class Pawn implements Cloneable{
    /**
     * Current position of the player
     */
	private Point position;
    /**
     * ID of the pawn
     */
    private int id;
    /**
     * Number of available fences of the pawn
     */
    private int availableFences;
    /**
     * Starting side of the pawn. Useful for knowing which it needs to go to win the game.
     */
    private Side startingSide;
    /**
     * Color of the pawn
     */
    private ColorPawn color;
    /**
     * Board associated to the pawn
     */
    private Board board;
    /**
     * Player associated to the pawn
     */
    private Player player;

    /**
     * Create a point from its position, id and color. It is necessary to take into
     * account the number of remaining cells of the game board, its starting point,
     * the game board and a player.
     * 
     * @param position        (Point) : coordinates of the current pawn
     * @param id              (int) : id of the pawn
     * @param availableFences (int) : number of barriers still available for a player
     * @param startingSide    (Side) : side of the game board from which the pawn move
     * @param color           (Color) : color of a pawn
     * @param board           (Board) : current board game
     * @param player          (Player) : the player who has the pawn
     */

    public Pawn(Point position, int id, int availableFences, Side startingSide, ColorPawn color, Board board, Player player){
        this.position = position;
        this.id = id;
        this.availableFences = board.getGame().getNbMaxTotalFences() / board.getGame().getBoard().getNbPawns();
        this.board = board;
        this.startingSide = startingSide;
        this.color = color;
        this.player = player;
    }

    /**
     * Create a point from its id and color. It is necessary to take into
     * account its starting point, the game board and a player.
     * 
     * @param id           (int) : id of the pawn
     * @param startingSide (Side) : side of the game board from which the pawn
     * @param color        (Color) : color of a pawn
     * @param player       (Player) : the player who has the pawn
     * @param nbMaxFences  (int) : Max number of fences in total (with all the pawns)
     * @param nbPawns      (int) : Number of pawns
     */

    public Pawn(int id, Side startingSide, ColorPawn color, Player player, int nbMaxFences, int nbPawns){
        this.position = null;
        this.id = id;
        this.availableFences = nbMaxFences / nbPawns;
        this.board = null;
        this.startingSide = startingSide;
        this.color = color;
        this.player = player;
    }

    /**
     * Create a point from its id and color. It is necessary to take into account its starting point, position, and the availableFences of the player.
     * 
     * @param id (int) : id of the pawn
     * @param startingSide (Side) : side of the game board from which the pawn
     * @param color (Color) : color of a pawn
     * @param position (Point) : current position of the pawn
     * @param availableFences (int) number of fences that the player can again place
     */

     public Pawn(int id, Side startingSide, ColorPawn color, Point position, int availableFences){
        this.position = position;
        this.id = id;
        this.availableFences = availableFences;
        this.board = null;
        this.startingSide = startingSide;
        this.color = color;
        this.player = null;
    }
    
    /**
     * Get the position of a pawn
     * 
     * @return (Point) Position of the pawn
     */

    public Point getPosition() {
        return position;
    }

    /**
     * Set the player associated to the pawn
     * @param player player to be associated with the pawn
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Set the position of a pawn to the pawn
     * 
     * @param position Position of the pawn
     */

    public void setPosition(Point position){
        this.position = position;
    }

    /**
     * Accessor to recover the ID of the pawn
     * 
     * @return (int) ID of the pawn
     */

    public int getId() {
        return id;
    }

    /**
     * Get the number of the available fences of the pawn
     * 
     * @return (int) Available fences remaining
     */

    public int getAvailableFences() {
        return this.availableFences;
    }

    /**
     * Set the number of the available fences of the pawn
     * 
     * @param availableFences Number of fences to set
     */

    public void setAvailableFences(int availableFences) {
        this.availableFences = availableFences;
    }

    /**
     * Get the initial starting side of the pawn
     * 
     * @return (Side) Starting side of the pawn
     */

    public Side getStartingSide() {
        return startingSide;
    }

    /**
     * Get the color of a pawn
     * 
     * @return Color of the pawn
     */

    public ColorPawn getColor() {
        return color;
    }

    /**
     * Get the board game of the pawn
     * 
     * @return (Board) Board of the pawn
     */

    public Board getBoard() {
        return this.board;
    }

    /**
     * Change the board of the pawn
     * @param board Board to assign to the pawn
     */
    public void setBoard(Board board) {
        this.board = board;
        if(this.position == null) {
            this.position = board.getGrid().getCenterOfSide(startingSide);
        }
    }

    /**
     * Get the player associated with the current pawn
     * 
     * @return Player associated to the pawn
     */

    public Player getPlayer(){
        return player;
    }

    /**
     * A procedure that updates the number of remaining fences to be placed
     */

    public void decreaseAvailableFences(){
        this.setAvailableFences(this.getAvailableFences()-1);
    }

    /**
     * A procedure to update the current point position
     * 
     * @param position New position of the pawn
     */

    public void move(Point position){
        this.setPosition(position);
    }

    /**
     * Redefine 'toString' method of the Object class to display information about a pawn
     * 
     * @return String displaying information of the pawn
     */
    
    @Override
    public String toString(){
        return("{\nposition:"+this.getPosition()+",\n"+"id:"+this.getId()+",\n"+"availableFences:"+this.getAvailableFences()+",\n"+"startingSide:"+this.getStartingSide()+",\n"+"color:"+this.getColor()+",\n"+"}");
    }

    public Object clone() throws CloneNotSupportedException {
        Pawn clone = new Pawn(this.getPosition(),this.getId(), this.getAvailableFences(), this.getStartingSide(), this.getColor(),this.getBoard(), this.getPlayer());
        return clone;
    }
}
