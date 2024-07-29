package ElectionMachine;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.round;

/**
 * Multiple Popularity Only class and its methods to process an
 * MPO election and retrieve the winning candidates
 */
public class MPO extends VotingMethod {
    /**
     * Number of seats up for election
     */
    public int numSeats;

    /**
     * Array of candidates participating in the election
     */
    public CandidateMPO[] candidates;

    /**
     * A list of BallotMPO objects
     */
    public BallotMPO[] ballots;


    /**
     * Constructor initializes member variables to prepare for an OPL election process
     *
     * @param electionData information gathered from csv input
     */
    public MPO(String[] electionData) {
        numCandidates = Integer.parseInt(electionData[2]);
        candidates = new CandidateMPO[numCandidates];
        numSeats = Integer.parseInt(electionData[1]);
        numBallots = Integer.parseInt(electionData[4]);
        ballots = new BallotMPO[numBallots];

        try {
            auditWriter = new FileWriter("audit.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Number of candidates: " + numCandidates);
        writeToFile("Number of candidates: " + numCandidates);
        System.out.println("Number of seats: " + numSeats);
        writeToFile("Number of seats: " + numSeats);
        System.out.println("Number of ballots: " + numBallots);
        writeToFile("Number of ballots: " + numBallots);
        createCandidates(electionData[3]);
        createBallots(electionData);
    }

    /**
     * Initialises the candidates list adding candidateOPL objects to list
     *
     * @param candidateInfo contains information about candidates in the election
     */
    public void createCandidates(String candidateInfo) {
        System.out.println(candidateInfo);
        String[] candidateList = candidateInfo.split("],");
        for (int i = 0; i < candidateList.length; i++) {
            candidateList[i] = candidateList[i].replace("[", "");
            candidates[i] = new CandidateMPO(candidateList[i]);
        }
    }

    /**
     * Creates ballot objects from input file information
     *
     * @param electionData information gathered from csv input
     */
    public void createBallots(String[] electionData) {
        // Creates ballots from input file data
        int ballotIndex = 0;
        for (int i = 5; i < electionData.length; i++) {
            String[] lineVotes = electionData[i].split(",");
            CandidateMPO choice = null;
            for (int j = 0; j < lineVotes.length; j++) {
                if (lineVotes[j].equals("1")) {
                    choice = candidates[j];
                }
            }
            ballots[ballotIndex] = new BallotMPO(choice);
            ballotIndex++;
        }
    }

    /**
     * Determines outcome of election utilizing helper methods
     */
    public void processElection() {
        assignBallots();
        for (CandidateMPO candidate : candidates) {
            writeToFile("Party: " + candidate.getName() + ", Total votes: " + candidate.getVotes());
        }

        System.out.println("Processing Election...");
        writeToFile("\nCalculating outcome: START");
        String winner = calculateOutcome();
        writeToFile("Calculating outcome: FINISHED\n");

        StringBuilder results = new StringBuilder("Results: ");
        String winnerMsg = "Winner(s) are " + winner;
        results.append(winnerMsg).append("\n");

        System.out.println(results);
        writeToFile(results.toString());

        // Closes file writer
        try {
            auditWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Assigns votes to each candidate
     */
    public void assignBallots() {
        for (int i = 0; i < numBallots; i++) {
            for (int j = 0; j < numCandidates; j++) {
                if (ballots[i].getCandidate().getName().equals(candidates[j].getName())) {
                    candidates[j].addVote();
                }
            }
        }
    }

    /**
     * Retrieves winners from each respective party
     *
     * @return Winners of the election
     */
    public String calculateOutcome() {
        randomizeOrder();
        sortCandidates();
        String allStats = "Voting percentages: ";
        for (int i = 0; i < numCandidates; i++) {
            allStats += "("+candidates[i].getName()+" - ";
            double percentVotes = (candidates[i].getVotes()/(double)numBallots)*100;
            String formattedPercent = String.format("%.2f",percentVotes);
            allStats+=(formattedPercent+"% of Votes) ");
        }
        writeToFile(allStats);
        System.out.println(allStats);


        StringBuilder output = new StringBuilder();
        for (int i = 0; i < numSeats; i++) {
            output.append(candidates[i].getName());
            double percentVotes = (candidates[i].getVotes()/(double)numBallots)*100;
            String formattedPercent = String.format("%.2f",percentVotes);
            output.append(" ("+formattedPercent+"% of Votes)");
            output.append(", ");

        }
        return output.toString();
    }

    /**
     * Randomizes the order of the candidates
     * Ensures no bias between ties
     */
    private void randomizeOrder() {
        CandidateMPO[] tempArray = candidates;
        List<CandidateMPO> tempList = Arrays.asList(candidates);
        Collections.shuffle(tempList);
        tempList.toArray(tempArray);
    }

    /**
     * Sorts the candidates based on votes via bubble sort
     */
    private void sortCandidates() {
        for (int i = 0; i < candidates.length; i++) {
            for (int j = 0; j < candidates.length - 1; j++) {
                if (candidates[j + 1].getVotes() > candidates[j].getVotes()) {
                    CandidateMPO tempCand = candidates[j];
                    candidates[j] = candidates[j + 1];
                    candidates[j + 1] = tempCand;
                }
            }
        }
    }
}


