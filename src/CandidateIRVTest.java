import ElectionMachine.Candidate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CandidateIRVTest {

    private CandidateIRV candidateirv;

    @Before
    public void setup() {
        candidateirv = new CandidateIRV("Rosen (D)");
    }

    @Test
    public void testConstructorIRV() {
        assertNotNull(candidateirv);
    }

    @Test
    public void testGetNameIRV() {
        assertEquals("Rosen (D)",  candidateirv.getName());
    }
    
    @Test
    public void testGetVotesIRV() {
        assertEquals(0,  candidateirv.getVotes());
    }

    @Test
    public void testAddVoteIRV() {
        candidateirv.addVote();
        assertEquals(1,  candidateirv.getVotes());
    }

    private class CandidateIRV extends Candidate{
        public CandidateIRV(String fullName) {
            this.name = fullName;
            String[] nameSplit = fullName.split("\\(");
            this.partyName = String.valueOf(nameSplit[1].charAt(0));
            this.votes = 0;
        }
    
    }
    
}
