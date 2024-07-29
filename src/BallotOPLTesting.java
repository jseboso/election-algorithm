import ElectionMachine.BallotOPL;
import ElectionMachine.CandidateOPL;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BallotOPLTesting {

    private BallotOPL ballot1;
    private BallotOPL ballot2;

    @Before
    public void setup() {
        CandidateOPL myCandidate1 = new CandidateOPL("Joe Biden (D)");
        CandidateOPL myCandidate2 = new CandidateOPL("Donald Trump (R)");
        ballot1 = new BallotOPL(myCandidate1);
        ballot2 = new BallotOPL(myCandidate2);
    }

    @Test
    public void testGetCandidate() {
        assertNotNull(ballot1.getCandidate());
        assertEquals(ballot1.getCandidate().getName(), "Joe Biden (D)");
        assertEquals(ballot1.getCandidate().getVotes(), 0);
        assertEquals(ballot1.getCandidate().getParty(), "D");
    }

    @Test
    public void testGetParty() {
        assertEquals(ballot1.getCandidate().getParty(), "D");
        assertEquals(ballot2.getCandidate().getParty(), "R");
    }
}
