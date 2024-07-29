import ElectionMachine.Candidate;
import ElectionMachine.CandidateOPL;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CandidateOPLTest {

    private Candidate candidateopl;

    @Before
    public void setup() {
        candidateopl = new CandidateOPL("Bob (I)");
    }

    @Test
    public void testGetName() {
        assertEquals("Bob (I)",  candidateopl.getName());
    }

    @Test
    public void testGetVotes() {
        assertEquals(0,  candidateopl.getVotes());
    }

    @Test
    public void testAddVote() {
        candidateopl.addVote();
        assertEquals(1,  candidateopl.getVotes());
        
    }

//    private class CandidateOPL extends Candidate{
//        public CandidateOPL(String fullName) {
//            this.name = fullName;
//            String[] nameSplit = fullName.split("\\(");
//            this.partyName = String.valueOf(nameSplit[1].charAt(0));
//            this.votes = 0;
//        }
//
//    }

    
}
