public class Player {
	private String username;

    private int nbWins;

    public Player(String username){
        this.username = username;
        this.nbWins = 0;
    }


    public String getUsername() {
        return username;
    }

    public int getNbWins() {
        return nbWins;
    }

    public void setNbWins(int nbWin){
        this.nbWins = nbWin;
    }

    @Override
    public String toString(){
        return this.getUsername();
    }
}
