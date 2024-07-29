package ElectionMachine;

/**
 * Abstract candidate class
 */
public abstract class Candidate {
    /**
     * Candidate's name
     */
    protected String name;

    /**
     * Candidate's party's name
     */
    protected String partyName;
    
    /**
     * Set the number of vote in the "testGetWinners" function
     */

    public void setVotes(int votes) {
        this.votes = votes;
    }

    /**
     * Candidate's number of votes
     */
    protected int votes;

    /**
     * Retrieves the candidate's name
     *
     * @return Candidate's name
     */
    public String getName(){
        return name;
    }

    /**
     * Retrieves the candidate's number of votes
     *
     * @return number of candidate's votes
     */
    public int getVotes(){
        return votes;
    }

    /**
     * Adds a vote to the candidate's total number of votes
     */
    public void addVote(){
        votes+=1;
    }
}
