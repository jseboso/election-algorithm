package ElectionMachine;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Abstract class that represents various voting methods
 * For this system, 2 types were implemented: IRV and OPL
 */
public abstract class VotingMethod {
    /**
     * File object that houses the audit file
     */
    static  FileWriter auditWriter;
    /**
     * List of all ballots in the election
     */
    public Ballot[] ballots;
    /**
     * The number of total candidates in the election
     */
    public int numCandidates;
    /**
     * The number of total ballots in the election
     */
    public int numBallots;


    /**
     * Will write to the current audit file
     *
     * @param line the line that is written to file
     */
    static void writeToFile(String line){
        try {
            auditWriter.write(line+"\n");
//            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred writing to the file.");
            e.printStackTrace();
        }
    }

    /**
     * Creates candidates from file input information
     *
     * @param candidateInfo Candidate information
     */
    abstract void createCandidates(String candidateInfo);

    /**
     * Creates ballot objects from input file information
     *
     * @param electionData Ballot information
     */
    abstract void createBallots(String[] electionData);

    /**
     * Determines the outcome of the election
     */
    abstract void processElection();

    /**
     * Assigns ballots to candidates
     */
    abstract void assignBallots();

}
