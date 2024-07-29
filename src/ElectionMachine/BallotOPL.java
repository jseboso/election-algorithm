package ElectionMachine;

/**
 * Represents a ballot in an OPL election
 */
public class BallotOPL extends Ballot{
    CandidateOPL choice;

    /**
     * Constructor for ballot object, initializes a chosen candidate
     *
     * @param candidate The ballot's specified candidate
     */
    public BallotOPL(CandidateOPL candidate) {
        choice = candidate;
    }

    /**
     * Retrieves the ballot's selected candidate
     *
     * @return The ballot's selected candidate
     */
    public CandidateOPL getCandidate() {
        return choice;
    }
}
