package ElectionMachine;

/**
 * Represents a candidate in an MPO election
 */
public class CandidateMPO extends Candidate {
    /**
     * Constructor for Candidate MPO object, initializes member variables
     *
     * @param fullName The name of the candidate
     */
    public CandidateMPO(String fullName) {
        name = fullName;
        votes = 0;
    }
}
