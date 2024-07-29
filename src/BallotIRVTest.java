import ElectionMachine.BallotIRV;
import ElectionMachine.CandidateIRV;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BallotIRVTest {

    private BallotIRV ballot;
    private BallotIRV ballotnull;

    @Before
    public void setup() {
        ballot = new BallotIRV(3);
    }

    @Test
    public void testBallotIRVNotNull() {
        assertNotNull(ballot);
        assertNotNull(ballot.getCandidates());
        assertNull(ballot.getCandidate());
    }  @Test

    public void testBallotIRVNull() {
        assertNull(ballotnull);
    }

    @Test
    public void testGetCandidateNull() {
        assertNotNull(ballot.getCandidates());
        assertEquals(3, ballot.getCandidates().length);
        for (int i = 0; i < ballot.getCandidates().length - 1; i++) {
            assertNull(ballot.getCandidate());
            ballot.setNextChoice();
        }
    }

    @Test
    public void testGetCandidateExist() {
        CandidateIRV candidate = new CandidateIRV("Bob (I)");
        ballot.setCandidate(0, candidate);
        assertNotNull(ballot.getCandidate());
    }

    @Test
    public void testSetAndGetCandidate() {
        CandidateIRV candidate = new CandidateIRV("Rosen (R)");
        ballot.setCandidate(0, candidate);
        assertEquals(candidate, ballot.getCandidates()[0]);
        assertEquals(candidate.getName(), ballot.getCandidates()[0].getName());
        assertEquals(candidate.getVotes(), ballot.getCandidates()[0].getVotes());
    }

    @Test
    public void testSetNextChoice() {
        int initialChoice = ballot.currentChoice;
        ballot.setNextChoice();
        assertEquals(initialChoice + 1, ballot.currentChoice);
    }

    @Test
    public void testGetNextChoice() {
        CandidateIRV candidate1 = new CandidateIRV("Rosen (R)");
        ballot.setCandidate(0, candidate1);
        CandidateIRV candidate2 = new CandidateIRV("Rosen (R)");
        ballot.setCandidate(1, candidate2);
        assertNotNull(ballot.getCandidate());
        assertNotNull(ballot.getNextChoice());
        ballot.setNextChoice();
        assertNull(ballot.getNextChoice());
    }

    @Test
    public void testGetNextChoiceAtEnd() {
        for (int i = 0; i < ballot.getCandidates().length - 1; i++) {
            ballot.setNextChoice();
        }
        assertNull(ballot.getNextChoice());
    }
}
