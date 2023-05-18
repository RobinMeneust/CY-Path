public class Pawn {
	private Point position;
    private int id;
    private int availableFences;
    private Side startingSide;
    private ColorPawn color;
    private Board board;

    private Player player;


    public Pawn(Point position, int id, int availableFences, Side startingSide, ColorPawn color, Board board, Player player){
        this.position = position;
        this.id = id;
        this.availableFences = availableFences;
        this.startingSide = startingSide;
        this.color = color;
        this.board = board;
        this.player = player;
    }

    public Pawn(int id, Side startingSide, ColorPawn color, Board board, Player player){
        this.position = board.getGrid().getCenterOfSide(startingSide);
        this.id = id;
        this.availableFences = board.getGame().getNbFences()/board.getGame().getNbPlayers();
        this.startingSide = startingSide;
        this.color = color;
        this.board = board;
        this.player = player;
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

    public ColorPawn getColor() {
        return color;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer(){
        return player;
    }

    public void placeFence(){
        this.setAvailableFences(this.getAvailableFences()-1);
    }

    public void move(Point position){
        this.setPosition(position);
    }

    @Override
    public String toString(){
        return("{\nposition:"+this.getPosition()+",\n"+"id:"+this.getId()+",\n"+"availableFences:"+this.getAvailableFences()+",\n"+"startingSide:"+this.getStartingSide()+",\n"+"color:"+this.getColor()+",\n"+"}");
    }
}
