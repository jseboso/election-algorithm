package ElectionMachine;

/**
 * Represents a candidate in an OPL election
 */
public class CandidateOPL extends Candidate {
    /**
     * Constructor for Candidate OPL object, initializes member variables
     *
     * @param fullName The name of the candidate
     */
    public CandidateOPL(String fullName) {
        name = fullName;
        String[] nameSplit = fullName.split("\\(");
        partyName = String.valueOf(nameSplit[1].charAt(0));
        votes=0;
    }

    /**
     * Retrieves the candidate's party
     *
     * @return The candidate's party
     */
    public String getParty() {
        return partyName;
    }
}
