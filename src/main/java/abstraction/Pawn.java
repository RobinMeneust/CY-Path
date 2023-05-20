package abstraction;

/**
 * This class corresponds to all elements that are related to a pawn in a CY-PATH game.
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class Pawn {
    /**
     * State the abstrac.Pawn's class attributes
     */

	private Point position;
    private int id;
    private int availableFences;
    private Side startingSide;
    private ColorPawn color;
    private Board board;
    private Player player;

    /**
     * Create a point from its position, id and color. It is necessary to take into
     * account the number of remaining cells of the game board, its starting point,
     * the game board and a player.
     * 
     * @param position        (abstrac.Point) : coordinates of the current pawn
     * @param id              (int) : id of the pawn
     * @param availableFences (int) : number of barriers still available for a
     *                        player
     * @param startingSide    (abstrac.Side) : side of the game board from which the pawn
     *                        leaves
     * @param color           (Color) : color of a pawn
     * @param board           (abstrac.Board) : current board game
     * @param player          (abstrac.Player) : the player who has the pawn
     */

    public Pawn(Point position, int id, int availableFences, Side startingSide, ColorPawn color, Board board, Player player){
        this.position = position;
        this.id = id;
        this.availableFences = availableFences;
        this.startingSide = startingSide;
        this.color = color;
        this.board = board;
        this.player = player;
    }

    /**
     * Create a point from its id and color. It is necessary to take into
     * account its starting point, the game board and a player.
     * 
     * @param id           (int) : id of the pawn
     * @param startingSide (abstrac.Side) : side of the game board from which the pawn
     * @param color        (Color) : color of a pawn
     * @param board        (abstrac.Board) : current board game
     * @param player       (abstrac.Player) : the player who has the pawn
     */

    public Pawn(int id, Side startingSide, ColorPawn color, Board board, Player player){
        this.position = board.getGrid().getCenterOfSide(startingSide);
        this.id = id;
        this.availableFences = board.getGame().getNbFences()/board.getGame().getNbPlayers();
        this.startingSide = startingSide;
        this.color = color;
        this.board = board;
        this.player = player;
    }
    
    /**
     * Accessor to recover the position of a pawn
     * 
     * @return (abstrac.Point) position
     */

    public Point getPosition() {
        return position;
    }

    /**
     * Accessor to assign the position of a pawn to the current pawn
     * 
     * @param positition (abstrac.Point)
     */

    public void setPosition(Point positition){
        this.position = positition;
    }

    /**
     * Accessor to recover the id of the current pawn
     * 
     * @return (int) id
     */

    public int getId() {
        return id;
    }

    /**
     * Accessor to recover the number of the available fences of the game board
     * 
     * @return (int) availableFences
     */

    public int getAvailableFences() {
        return this.availableFences;
    }

    /**
     * Accessor to assign the number of the available fences of the game board
     * 
     * @param availableFences (int)
     */

    public void setAvailableFences(int availableFences) {
        this.availableFences = availableFences;
    }

    /**
     * Accessor to recover the initial starting side of the pawn
     * 
     * @return (abstrac.Side) startingSide
     */

    public Side getStartingSide() {
        return startingSide;
    }

    /**
     * Accessor to recover the color of a pawn
     * 
     * @return (Color) color
     */

    public ColorPawn getColor() {
        return color;
    }

    /**
     * Accessor to recover the board game of a CY-PATH game
     * 
     * @return (abstrac.Board) board
     */

    public Board getBoard() {
        return board;
    }

    /**
     * Accessor to retrieve the player associated with the current pawn
     * 
     * @return (abstrac.Player) player
     */

    public Player getPlayer(){
        return player;
    }

    /**
     * A procedure that updates the number of remaining fences to be placed
     */

    public void placeFence(){
        this.setAvailableFences(this.getAvailableFences()-1);
    }

    /**
     * A procedure to update the current point position
     * 
     * @param position (abstrac.Point)
     */

    public void move(Point position){
        this.setPosition(position);
    }

    /**
     * Redefine 'toString' method of the Object class to display informations about
     * a pawn
     * 
     * @return (String) : a text
     */
    
    @Override
    public String toString(){
        return("{\nposition:"+this.getPosition()+",\n"+"id:"+this.getId()+",\n"+"availableFences:"+this.getAvailableFences()+",\n"+"startingSide:"+this.getStartingSide()+",\n"+"color:"+this.getColor()+",\n"+"}");
    }
}
