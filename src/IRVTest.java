import ElectionMachine.CandidateIRV;
import ElectionMachine.IRV;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class IRVTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private IRV irv;

    @Before
    public void setup(){
        String[] electionData = new String[]
                {
                        "IR",
                        "4",
                        "Rosen (D), Kleinberg (R), Chou (I), Royce (L)",
                        "6",
                        "1,3,4,2",
                        "1,,2,",
                        "1,2,3,",
                        "3,2,1,4",
                        ",,1,2",
                        ",,,1"
                };
        irv = new IRV(electionData);
    }


    @Test
    public void testConstructor() {
        assertAll("num candidates and ballots",
                () -> assertEquals(4,irv.numCandidates),
                () -> assertEquals(6, irv.numBallots)
        );
    }


    @Test
    public void testCreateCandidates() {
        String[] candidatesList = new String[]{"Rosen","Kleinberg", "Chou", "Royce"};
        assertAll("Candidates are set",
                () -> assertEquals(4, irv.candidates.size()),
                ()-> {for(int i = 0; i<irv.candidates.size(); i++){
                        assertTrue(irv.candidates.get(i).getName().contains(candidatesList[i]));
                    }
                }
        );
    }

    @Test
    public void testCreateBallots() {
        assertAll("Ballots are set",
                () -> assertEquals(6, irv.ballots.length),
                ()-> {for(int i = 0; i<irv.ballots.length; i++){
                    assertNotNull(irv.ballots[i]);
                    }
                }
        );
    }
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testProcessElection() {
        irv.processElection();
        String result = """
                Processing Election...

                Majority found!
                Results: Winner is Rosen (D), Total Votes 4""";
        assertEquals(result, outContent.toString().trim());
    }
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testAssignBallots() {
        irv.assignBallots();
        int[] votes = new int[]{3,0,2,1};
        assertAll("Check if ballots are assigned to candidates",
                ()-> {for(int i = 0; i<irv.numCandidates; i++){
                    assertEquals(votes[i],irv.candidates.get(i).getVotes());
                }
            }
        );
    }

    @Test
    public void testFindLowestCandidate() {
        irv.assignBallots();
        ArrayList<CandidateIRV> lowest = irv.findLowestCandidate();
        assertAll("Check if ballots are assigned to candidates",
                ()-> assertNotNull(lowest),
                ()-> assertEquals(1, lowest.size()),
                ()-> assertEquals(0,lowest.get(0).getVotes())
        );
    }
}
    