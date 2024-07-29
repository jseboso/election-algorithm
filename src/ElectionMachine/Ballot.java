package ElectionMachine;

/**
 * Represents a ballot used in an election
 */
abstract class Ballot {

    /**
     * Retrieves the ballot's selected candidate
     */
    abstract Candidate getCandidate();
}
