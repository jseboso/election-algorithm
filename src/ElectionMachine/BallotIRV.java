package ElectionMachine;

/**
 * Represents a ballot in an IRV election
 */
public class BallotIRV extends Ballot{
    /**
     * choices keeps track of the ballots candidates in an array ranked from highest preference to lowest
    * */
    CandidateIRV[] choices;
    /**
     * Number of candidates ranked on the ballot
     * */
    int numCandidates;
    /**
     * The highest ranked candidate
     * */
    public int currentChoice;
    
    /**
     * Constructor for ballot object, initializes an array of candidates voted, depending on the number of candidates.
     * Initializes the currentChoice which holds the index for the current candidate choice.
     * @param num_cands number of candidates candidate
     */
    public BallotIRV(int num_cands) {
        numCandidates = num_cands;
        choices = new CandidateIRV[numCandidates];
        currentChoice = 0;
    }

    /**
     * Gets the ballot's array of candidates
     *
     * @return The ballot's array of candidates
     */
    public CandidateIRV[] getCandidates() {
        return choices;
    }
    
    /**
     * Gets the ballot's currently selected candidate
     *
     * @return The ballot's currently selected candidate
     */
    public CandidateIRV getCandidate() {
        return choices[currentChoice];
    }
    
     /**
     * Sets the candidate in the candidates array at the desired index
     *
      * @param index sets the ballots with the correct candidate
      * @param candidate the candidate to set
     */
    public void setCandidate(int index, CandidateIRV candidate) {
        choices[index] = candidate;
    }

    /**
     * Sets the next index value for current choice by incrementing 1
     */
    public void setNextChoice() {
        if (currentChoice < choices.length - 1) {
            currentChoice = currentChoice + 1;
        }
    }
    
    /**
     * Gets the ballot's  next candidate to be selected
     *
     * @return The ballot's next candidate to be selected
     */
    public CandidateIRV getNextChoice() {
        if (currentChoice + 1 < choices.length) {
            return choices[currentChoice + 1];
        } else {
            return null;
        }
    }

}
