package ElectionMachine;

/**
 * Represents a candidate in an IRV election
 */
public class CandidateIRV extends Candidate{
    /**
     * Constructor for Candidate IRV object, initializes member variables
     *
     * @param fullName The name of the candidate
     */

     /**
     * Candidate's original number of votes
     */
    protected int oriVotes;

    public CandidateIRV(String fullName) {
        name = fullName;
        String[] nameSplit = fullName.split("\\(");
        partyName = String.valueOf(nameSplit[1].charAt(0));
        votes=0;
    }
    /**
     * Retrieves the candidate's number of votes before reallocation
     *
     * @return number of candidate's initial votes before reallocation
     */
    public int getOriVotes(){
        return oriVotes;
    }
    
     /**
     * Sets the candidate's number of votes before reallocation
     */
    public void setOriVote(int newVote){
        oriVotes = newVote;
    }

}
