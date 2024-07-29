import static org.junit.Assert.*;

import ElectionMachine.OPL;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

public class MPOTest {

    @Test
    public void testMPOConstructor() {
        String[] electionData = {
                "OPL", "5", "Royce(D),Bob(R),Jerry(I),Lincoln(L),Timothy(G)", "6", "1", "1,2,,,"
        };
        OPL opl = new OPL(electionData);

        assertEquals(5, opl.candidates.length);
        assertEquals(1, opl.numBallots);
        assertEquals(6, opl.numSeats);
        String[] expectedNames = {"Royce(D)", "Bob(R)", "Jerry(I)", "Lincoln(L)", "Timothy(G)"};
        String[] expectedParties = {"D", "R", "I", "L", "G"};
        for (int i = 0; i < expectedNames.length; i++) {
            assertEquals(expectedNames[i], opl.candidates[i].getName());
            assertEquals(expectedParties[i], opl.candidates[i].getParty());
        }
    }

    @Test
    public void testCreateBallots() {
        String[] electionData = {
                "OPL", "5", "Royce(D),Bob(R),Jerry(I),Lincoln(L),Timothy(G)", "6", "5",
                "1,,,,", ",1,,,", ",,1,,", ",,,1,", "1,,,,"
        };
        OPL opl = new OPL(electionData);

        assertEquals(5, opl.ballots.length);
        String[] expectedCandidates = {"Royce(D)", "Bob(R)", "Jerry(I)", "Lincoln(L)", "Royce(D)"};
        for (int i = 0; i < expectedCandidates.length; i++) {
            assertNotNull(opl.ballots[i]);
            assertEquals(expectedCandidates[i], opl.ballots[i].getCandidate().getName());
        }
    }

    @Test
    public void testAssignBallots() {
        String[] electionData = {
                "OPL", "5", "Royce(D),Bob(R),Jerry(I),Lincoln(L),Timothy(G)", "6", "5",
                "1,,,,", ",1,,,", ",,1,,", ",,,1,", "1,,,,"
        };
        OPL opl = new OPL(electionData);
        opl.assignBallots();

        int[] expectedVotes = {2, 1, 1, 1, 0};
        for (int i = 0; i < expectedVotes.length; i++) {
            assertEquals(expectedVotes[i], opl.candidates[i].getVotes());
        }
    }

    @Test
    public void testProcessElection() throws IOException {
        String[] electionData = {
                "OPL", "3", "Royce(D),Bob(R),Jerry(I)", "2", "5",
                "1,,", ",1,", ",,1", "1,,", ",1,"
        };

        OPL opl = new OPL(electionData);
        opl.processElection();

        String expectedOutcome = "Winner is Royce(D), Bob(R), ";
        File file = new File("audit.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) content.append(line).append("\n");
            assertTrue("The expected outcome was not found in the audit file.", content.toString().contains(expectedOutcome));
        }
    }
}
