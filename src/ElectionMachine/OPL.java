package ElectionMachine;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Open Party Listing class and its methods to process an
 * OPL election and retrieve the winning candidates
 */
public class OPL extends VotingMethod {
    /**
     * Number of seats up for election
     */
    public int numSeats;

    /**
     * Array of candidates participating in the election
     */
    public CandidateOPL[] candidates;

    /**
     * Array of parties participating in the election
     */
    public Party[] parties;
    /**
     * A list of BallotOPL objects
     */
    public BallotOPL[] ballots;


    /**
     * Constructor initializes member variables to prepare for an OPL election process
     *
     * @param electionData information gathered from csv input
     */
    public OPL(String[] electionData) {
        numCandidates = Integer.parseInt(electionData[1]);
        candidates = new CandidateOPL[numCandidates];
        numSeats = Integer.parseInt(electionData[3]);
        numBallots = Integer.parseInt(electionData[4]);
        ballots = new BallotOPL[numBallots];

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
        createCandidates(electionData[2]);
        createParties();
        createBallots(electionData);
    }

    /**
     * Initialises the candidates list adding candidateOPL objects to list
     *
     * @param candidateInfo contains information about candidates in the election
     */
    public void createCandidates(String candidateInfo) {
        String[] candidateList = candidateInfo.split(",");
        for (int i = 0; i < candidateList.length; i++) {
            candidates[i] = new CandidateOPL(candidateList[i]);
        }
    }

    /**
     * Creates party objects from input file information
     * Assigns each party their respective number of candidates
     */
    public void createParties() {
        // Create parties from Candidate information
        Party[] tempParties = new Party[numCandidates];
        int partyIndex = 0;
        for (int i = 0; i < numCandidates; i++) {
            boolean alreadyExists = false;
            for (int j = 0; j < partyIndex; j++) {
                if (candidates[i].getParty().equals(tempParties[j].getName())) {
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) {
                tempParties[partyIndex] = new Party(candidates[i].getParty());
                partyIndex++;
            }
        }
        int partyCount = 0;
        // assigns amount of parties
        for (Party tempParty : tempParties) {
            if (tempParty != null) {
                partyCount++;
            }
        }
        parties = new Party[partyCount];
        System.arraycopy(tempParties, 0, parties, 0, partyCount);
        // Assigns amount of candidates for each party
        for (int i = 0; i < numCandidates; i++) {
            for (Party party : parties) {
                if (candidates[i].getParty().equals(party.getName())) {
                    party.addCandidateCount();
                }
            }
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
            CandidateOPL choice = null;
            for (int j = 0; j < lineVotes.length; j++) {
                if (lineVotes[j].equals("1")) {
                    choice = candidates[j];
                }
            }
            ballots[ballotIndex] = new BallotOPL(choice);
            ballotIndex++;
        }
    }

    /**
     * Determines outcome of election utilizing helper methods
     */
    public void processElection() {
        assignBallots();
        assignCandidates();
        for (Party party : parties) {
            writeToFile("Party: " + party.getName() + ", Total votes: " + party.getTotalVotes());
        }

        System.out.println("Processing Election...");
        writeToFile("\nCalculating outcome: START");
        String winner = calculateOutcome();
        writeToFile("Calculating outcome: FINISHED\n");

        StringBuilder results = new StringBuilder("Results: ");
        String winnerMsg = "Winner is " + winner;
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
     * Assigns candidates to each party
     */
    public void assignCandidates() {
        for (int i = 0; i < numCandidates; i++) {
            for (Party party : parties) {
                if (candidates[i].getParty().equals(party.getName())) {
                    party.addCandidate(candidates[i]);
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
        assignSeats();
        StringBuilder output = new StringBuilder();
        for (Party party : parties) {
            CandidateOPL[] winners = party.getWinners();
            for (CandidateOPL winner : winners) {
                output.append(winner.getName()).append(", ");
            }
        }
        return output.toString();
    }

    /**
     * Assigns seats to each party based on largest remainder formula
     */
    public void assignSeats() {
        writeToFile("Parties: " + parties.length);
        int seatsRemaining = numSeats;
        double quota = ((double) numBallots) / ((double) numSeats);
        for (Party party : parties) {
            // Assign seats based upon the quota

            double newSeats = (double) party.getTotalVotes() / quota;
            double remainingVotes = (double) party.getTotalVotes() % quota;
            party.addSeats((int) newSeats);
            party.setRemainingVotes(remainingVotes);
            seatsRemaining -= (int) newSeats;
        }
        writeToFile("Remaining seats:" + seatsRemaining);

        sortByRemainder();
        // assign remaining seats
        for (int i = 0; i < seatsRemaining; i++) {
            parties[i].addSeats(1);
            writeToFile("1 seat assigned to " + parties[i].getName());
        }
    }

    /**
     * Sorts parties based on their remaining votes
     * following quota division via bubble sort
     */
    public void sortByRemainder() {
        for (int i = 0; i < parties.length; i++) {
            for (int j = 0; j < parties.length - 1; j++) {
                if (parties[j + 1].getRemainingVotes() > parties[j].getRemainingVotes()) {
                    Party tempParty = parties[j];
                    parties[j] = parties[j + 1];
                    parties[j + 1] = tempParty;
                }
            }
        }
    }
}


