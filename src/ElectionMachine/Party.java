package ElectionMachine;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a party in an OPL election.
 * Determines which candidates within the party recieve the allocated seats.
 */
public class Party {
    /**
     * Party's name
     */
    private final String name;

    /**
     * Party's total number of votes
     */
    private int totalVotes;
    /**
     * returns the number of the seats party has
     */
    public int getPartySeats() {
        return partySeats;
    }

    /**
     * Party's number of allocated seats
     */
    private int partySeats;
     /**
     * returns the total Number of candidates in the array
     */
    public int getTotalCandidates() {
        return candidates.length;
    }

    /**
     * Party's candidates
     */
    private CandidateOPL[] candidates;

    /**
     * Party's remaining votes following quota division
     */
    private double remainingVotes;


    /**
     * Constructor for party, assigns party name
     *
     * @param name The party's name
     */
    public Party(String name) {
        this.name = name;
//        candidates = new CandidateOPL[0];
    }
     /**
     * This to dynamically handel array size
     */
    public void initArray(int size) {
        candidates = new CandidateOPL[size];
    }

    /**
     * Increases the number of candidates by one
     */
    public void addCandidateCount() {
        candidates = new CandidateOPL[candidates.length + 1];
    }

    /**
     * Retrieves the party's name
     *
     * @return The party's name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the party's total number of votes
     *
     * @return Party's total number of votes
     */
    public int getTotalVotes() {
        return totalVotes;
    }

    /**
     * Adds a candidate to the party
     *
     * @param candidate A candidate
     */
    public void addCandidate(CandidateOPL candidate) {
        for (int i = 0; i < candidates.length; i++) {
            if (candidates[i] == null) {
                candidates[i] = candidate;
                totalVotes += candidate.getVotes();
                break;
            }
        }
    }

    /**
     * Assigns the remaining number of votes following
     * the first allocation of seats
     *
     * @param remainingVotes The remaining number of unaccounted for votes
     */
    public void setRemainingVotes(double remainingVotes) {
        this.remainingVotes = remainingVotes;
    }

    /**
     * Retrieves the remaining number of votes
     *
     * @return The number of remaining votes for party
     */
    public double getRemainingVotes() {
        return this.remainingVotes;
    }

    /**
     * Adds seats to the party
     *
     * @param numSeats The number of seats added to the party
     */
    public void addSeats(int numSeats) {
        partySeats += numSeats;
    }

    /**
     * Retrieves the winning candidates from the party
     * add also to initializes auditWriter to record the process and manage array size dynamically
     *
     * @return the candidates who won the election
     */
    public CandidateOPL[] getWinners() {
        randomizeOrder();
        sortCandidates();
        String result = "Party name: " + name + ", Candidates: " + candidates.length + ", Party seats: " + partySeats + ", Total votes: " + totalVotes + "\n";
        StringBuilder message = new StringBuilder(result);
        for (CandidateOPL c : candidates) {
            String info = c.getName() + ": " + c.getVotes() + ", ";
            message.append(info);
        }
       // Collects winners
        CandidateOPL[] winners = new CandidateOPL[partySeats];
        System.arraycopy(candidates, 0, winners, 0, winners.length);
        try {
            VotingMethod.auditWriter = new FileWriter("audit.csv");// 
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        VotingMethod.writeToFile(message.toString());
        return winners;
    }
    
    /**
     * Randomizes the order of the candidates
     * Ensures no bias between ties
     */
    private void randomizeOrder() {
        CandidateOPL[] tempArray = candidates;
        List<CandidateOPL> tempList = Arrays.asList(candidates);
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
                    CandidateOPL tempCand = candidates[j];
                    candidates[j] = candidates[j + 1];
                    candidates[j + 1] = tempCand;
                }
            }
        }
    }
}
