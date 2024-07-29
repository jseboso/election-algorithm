import ElectionMachine.CandidateOPL;
import ElectionMachine.Party;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PartyTest {
    private Party party;

    @Before
    public void setup() {
        party = new Party("democrat");
    }

    @Test
    public void testPartyConstructor() {
        assertNotNull(party);
        assertEquals("democrat", party.getName());
    }

    @Test
    public void testAddCandidate() {
        CandidateOPL candidate1 = new CandidateOPL("Candidate 1 (A)");
        CandidateOPL candidate2 = new CandidateOPL("Candidate 2 (B)");
        CandidateOPL candidate3 = new CandidateOPL("Candidate 3 (C)");
        party.initArray(3);
        party.addCandidate(candidate1);
        party.addCandidate(candidate2);
        party.addCandidate(candidate3);
        assertEquals(3, party.getTotalCandidates());
    }

    @Test
    public void testGetTotalVotes() {
        assertEquals("the starting # of votes should be 0", 0, party.getTotalVotes());
    }

    @Test
    public void testSetAndGetRemainingVotes() {
        double remainingVotes = 40.75;
        party.setRemainingVotes(remainingVotes);
        assertEquals("Make sure that the number of votes left is set and checked", remainingVotes, party.getRemainingVotes(), 0.001);
    }

    @Test
    public void testAddSeats() {
        int initialSeats = 0;
        int seatsToAdd = 5;
        party.addSeats(initialSeats);
        party.addSeats(seatsToAdd);
        
        assertEquals(5, party.getPartySeats());
    }

    @Test
    public void testGetWinners() throws IOException {
        CandidateOPL candidate1 = new CandidateOPL("Candidate1(PartyA)");
        CandidateOPL candidate2 = new CandidateOPL("Candidate2(PartyB)");
        CandidateOPL candidate3 = new CandidateOPL("Candidate3(PartyC)");

        party.initArray(3);

        
        candidate1.setVotes(100);
        candidate2.setVotes(150);
        candidate3.setVotes(75);

       
        party.addCandidate(candidate1);
        party.addCandidate(candidate2);
        party.addCandidate(candidate3);

        
        party.addSeats(2);

        
        CandidateOPL[] winners = party.getWinners();

        
        assertEquals(2, winners.length);

        
        assertEquals(candidate2, winners[0]);
        assertEquals(candidate1, winners[1]);

    }

}
