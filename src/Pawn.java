public class Pawn {
	private Point position;
    private int id;
    private int availableFences;
    private Side startingSide;
    private Color color;
    private Board board;


    public Pawn(Point position, int id, int availableFences, Side startingSide, Color color, Board board){
        this.position = position;
        this.id = id;
        this.availableFences = availableFences;
        this.startingSide = startingSide;
        this.color = color;
        this.board = board;
    }

    public Pawn(int id, Side startingSide, Color color, Board board){
        this.position = new Point(board.getNbCols()/board.getGame().getNbPlayers(), board.getNbRows()/board.getGame().getNbPlayers());
        this.id = id;
        this.availableFences = board.getGame().getNbFences()/board.getGame().getNbPlayers();
        this.startingSide = startingSide;
        this.color = color;
        this.board = board;
    }
    
    public Point getPosition() {
        return position;
    }

    public void setPosition(Point positition){
        this.position = positition;
    }

    public int getId() {
        return id;
    }

    public int getAvailableFences() {
        return this.availableFences;
    }

    public void setAvailableFences(int availableFences) {
        this.availableFences = availableFences;
    }

    public Side getStartingSide() {
        return startingSide;
    }

    public Color getColor() {
        return color;
    }

    public Board getBoard() {
        return board;
    }

    public void placeFence(){
        this.setAvailableFences(this.getAvailableFences()-1);
    }

    public void move(Point position){
        this.setPosition(position);
    }
}
