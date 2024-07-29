package ElectionMachine;

/**
 * Represents a ballot in an MPO election
 */
public class BallotMPO extends Ballot {
    CandidateMPO choice;

    /**
     * Constructor for ballot object, initializes a chosen candidate
     *
     * @param candidate The ballot's specified candidate
     */
    public BallotMPO(CandidateMPO candidate) {
        choice = candidate;
    }

    /**
     * Retrieves the ballot's selected candidate
     *
     * @return The ballot's selected candidate
     */
    public CandidateMPO getCandidate() {
        return choice;
    }
}
