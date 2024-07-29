package ElectionMachine;

import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Instant Runoff Voting class and it's methods that help process an IRV election
 * to retrieve winner of the election
 */
public class IRV extends ElectionMachine{
    /**
     * A list of current candidates that are updated throughout the election process
     */
    public ArrayList<CandidateIRV> candidates;

    /**
     * A list of eliminated candidates which get updated throughout the election process
     */
    ArrayList<CandidateIRV> eliminated;
    /**
     * A list of BallotIRV objects
     */
    public BallotIRV[] ballots;

    /**
     * File object that houses the audit file
     */
    public FileWriter invalidBallotsFile;
    public int numOfInvalidBallots = 0;

    /**
     * This constructor initializes member variables to prepare for IRV election process
     *
     * @param electionData information gathered from csv input
     */
    public IRV(String[] electionData){
        numCandidates = parseInt(electionData[1]);
        candidates = new ArrayList<>();
        eliminated = new ArrayList<>();
        numBallots = parseInt(electionData[3]);
        ballots = new BallotIRV[numBallots];
        try {
            auditWriter = new FileWriter("audit.csv");
            invalidBallotsFile = new FileWriter("invalidBallotsIRV.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Number of candidates: " + numCandidates);
        writeToFile("Number of candidates: " + numCandidates);
        System.out.println("Number of ballots: " + numBallots);
        writeToFile("Number of ballots: " + numBallots);

        createCandidates(electionData[2]);
        createBallots(electionData);

        System.out.println("Number of invalid ballots: " + numOfInvalidBallots);
        writeToFile("Number of invalid ballots: " + numOfInvalidBallots);
    }

    /**
     * Adds candidateIRV objects to candidates list according to csv data
     *
     * @param candidateInfo contains information about candidates in the election
     */
     void createCandidates(String candidateInfo) {
        String[] candidateList = candidateInfo.split(",");
        for (String s : candidateList) {
            candidates.add(new CandidateIRV(s));
        }
    }

    /**
     * Adds BallotIRV objects to ballots list according to csv data
     *
     * @param electionData contains information about each ballot in the election
     */
     void createBallots(String[] electionData) {
        int ballotIndex = 0; // index of the ballots
        for (int i = 4; i < electionData.length; i++) { // ballots from line 5th line and beyond
            String[] lineVotes = electionData[i].split(","); //splits one vote by comma
            if(isValidBallotIRV(lineVotes)){
                CandidateIRV choice;
                ballots[ballotIndex] = new BallotIRV(lineVotes.length);
                for (int j = 0; j < lineVotes.length; j++) {
                    choice = candidates.get(j); // finds the candidate choice at that index
                    if (!lineVotes[j].isEmpty()){
                        int ranking = parseInt(lineVotes[j]);
                        ballots[ballotIndex].setCandidate(ranking-1, choice);
                    }
                }
            }
            ballotIndex++;
        }
        numBallots -= numOfInvalidBallots;
    }

    /**
     * Helper function to add invalid ballots to csv file containing invalid ballots
     *
     * @param reason the reason why the ballot is invalid
     * @param invalidBallot string representing the invalid ballot
     */
    private void logInvalidBallot(String reason, String invalidBallot) {
        String invalidMessage = "Invalid Ballot (" + reason + "): " + invalidBallot;
        numOfInvalidBallots++;

        try {
            this.invalidBallotsFile.write(invalidMessage + "\n");
        } catch (IOException var5) {
            System.out.println("An error occurred writing to the file.");
            var5.printStackTrace();
        }

    }

    /**
     * This function determines whether IRV ballots are valid before adding to ballots datastructure
     *
     * @param ballot represent the ballot as a string[]
     */
    boolean isValidBallotIRV(String[] ballot){
        double half = numCandidates / 2.0;
        int rankedCandidates = 0;
        ArrayList<Integer> rankedCandidatesList = new ArrayList<Integer>();
        ArrayList<Integer> correctRankedCandidatesList = new ArrayList<Integer>();

        for (String candidate : ballot) { // count number of ranked candidates
            if (!candidate.isEmpty()) {
                rankedCandidatesList.add(Integer.valueOf(candidate)); // get ballots ranking
                rankedCandidates++;
                correctRankedCandidatesList.add(rankedCandidates); // add correct ranked list
            }
        }

        if(rankedCandidates < half){
            logInvalidBallot("not enough candidates ranked", Arrays.toString(ballot));
            return false;
        }

        rankedCandidatesList.sort(Comparator.naturalOrder()); // now make sure the rankings are good ie no duplicates/skipped rankings
        if(!rankedCandidatesList.equals(correctRankedCandidatesList)){
            logInvalidBallot("incorrect rankings", Arrays.toString(ballot));
            return false;
        }
        return true;
    }

    /**
     * Begins election process to find winner
     */
    public void processElection() {
        assignBallots();

        System.out.println("Processing Election...");
        writeToFile("\nCalculating outcome: START");
        System.out.println();
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-10s %-15s %-15s %-15s\n", "Round","Candidate Name", "Votes Owned", "Votes Changed");
        System.out.println("-----------------------------------------------------------------");
        Candidate winner = calculateOutcome();
        writeToFile("Calculating outcome: FINISHED\n");

        StringBuilder results = new StringBuilder("Results: ");
        String winnerMsg = "Winner is " + winner.getName();
        String totalMsg = "Total Votes " + winner.getVotes();
        results.append(winnerMsg).append(", ").append(totalMsg);

        System.out.println(results);
        writeToFile(results.toString());

        try {
            auditWriter.close();
            invalidBallotsFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds votes to each candidate based on highest ranked in ballot
     */
    public void assignBallots() {
        for (int i = 0; i < numBallots; i++) {
            for (int j = 0; j < numCandidates; j++) {
                if (ballots[i].getCandidate().getName().equals(candidates.get(j).getName())) {
                    candidates.get(j).addVote();
                }
            }
        }
    }

    /**
     * Gets the lowest ranked candidates
     * 
     * @return an ArrayList of the lowest ranked candidates
     */
    public ArrayList<CandidateIRV> findLowestCandidate(){
        int minVotes = candidates.get(0).getVotes();
        ArrayList<CandidateIRV> lowestRanked = new ArrayList<>();

        // Loop through the array
        for (int i = 0; i < numCandidates; i++) {
            if (candidates.get(i).getVotes() < minVotes)
                minVotes = candidates.get(i).getVotes();
        }

        // check for multiple low ranked candidates - add to list
        for (int i = 0; i < numCandidates; i++) {
            if(minVotes == candidates.get(i).getVotes()){
                lowestRanked.add((candidates.get(i)));
            }
        }
        if(lowestRanked.size() == candidates.size()){
            return null;
        }
        return lowestRanked;
    }

    /**
     * Eliminates a list of candidates in the IRV election
     * 
     * @param eliminatedCandidates an ArrayList of candidates to eliminate
     */
    public void eliminateCandidates(ArrayList<CandidateIRV> eliminatedCandidates) {
        for (CandidateIRV eliminatedCandidate : eliminatedCandidates) {
            reallocateVotes(eliminatedCandidate);
            eliminated.add(eliminatedCandidate);
            candidates.remove(eliminatedCandidate);
            writeToFile(eliminatedCandidate.getName() + " is successfully eliminated");
            numCandidates--;
        }
    }
    
    /**
     * Checks if the ballots who votes for the eliminated candidate can be reallocated
     * 
     * @param eliminatedCandidate an CandidateIRV object of candidate to eliminate
     */
    private void reallocateVotes(CandidateIRV eliminatedCandidate) {
        for (BallotIRV ballot : ballots) {
            if (eliminatedCandidate.getVotes() == 0) {
                break;
            }
            if (ballot.getCandidate() != null && ballot.getCandidate().getName().equals(eliminatedCandidate.getName())) {
                reallocateVote(ballot, eliminatedCandidate);
            }
        }
    } 

    /**
     * Reallocates the vote ballots who votes for the eliminated candidate
     * 
     * @param ballot an BallotIRV object of ballot that voted for the eliminated candidate
     * @param eliminatedCandidate an CandidateIRV object of candidate to eliminate
     */
    private void reallocateVote(BallotIRV ballot, CandidateIRV eliminatedCandidate) {
        for (int k = 0; k < ballot.choices.length; k++) {
            ballot.setNextChoice();
            if (!eliminated.contains(ballot.getCandidate())) {
                writeToFile("**Tried to distribute vote but this person has been eliminated.**");
                break;
            }
        }

        if (ballot.getCandidate() != null) {
            int votes = ballot.getCandidate().getVotes();
            ballot.getCandidate().addVote();
            int updatedVotes = ballot.getCandidate().getVotes();
            writeToFile("Transfer vote 1: " + eliminatedCandidate.getName() + " --> " + ballot.getCandidate().getName());
            writeToFile(ballot.getCandidate().getName() + ":" + votes + " --> " + updatedVotes);
        } else {
            writeToFile("Unable to distribute vote since no other candidate was ranked:(");
        }
    }

    /**
     * Display the calculation for the outcome
     * 
     * @param round the current round number for the IRV calculation process
     * @param eliminatedCandidates ArrayList of the eliminated candidates in the current round
     */
    private void displayCalculation(int round, ArrayList<CandidateIRV> eliminatedCandidates) {
        if (round == 1){
            for (int i = 0; i < candidates.size(); i++) {
                CandidateIRV cand = candidates.get(i);
                if (i == 0) {
                    System.out.printf("%-10s %-15s %-15d +%-15d \n", round, cand.getName(), cand.getVotes(), cand.getVotes());
                } else {
                    System.out.printf("%-10s %-15s %-15d +%-15d\n", "", cand.getName(), cand.getVotes(), cand.getVotes());
                }
            }
        }
        else{
            for (int i = 0; i < candidates.size(); i++) {
                CandidateIRV cand = candidates.get(i);
                if (i == 0) {
                    System.out.printf("%-10s %-15s %-15d +%-15d \n", round, cand.getName(), cand.getVotes(), (cand.getVotes() - cand.getOriVotes()));
                } else {
                    System.out.printf("%-10s %-15s %-15d +%-15d\n", "", cand.getName(), cand.getVotes(), (cand.getVotes() - cand.getOriVotes()));
                }
            }
        }
        if (eliminatedCandidates != null){
            for (CandidateIRV eliminatedCandidate : eliminatedCandidates) {
                System.out.printf("%-10s %-15s %-15d -%-15d\n", "", eliminatedCandidate.getName(), 0, (eliminatedCandidate.getVotes()));
            }
        }
        System.out.println("-----------------------------------------------------------------");
    }

     /**
     * Calculates the winner of the election
    * @return the candidate who won the election
     */
     public CandidateIRV calculateOutcome() {
        int counter = 1;
         while (true) {
            writeToFile("\nRound " + counter);

            for (CandidateIRV candidate : candidates) {
                candidate.setOriVote(candidate.getVotes());
            }

            

            StringBuilder candidatesInfo = new StringBuilder();
            candidatesInfo.append(candidates.size()).append(" candidates in the running!\n");
            for (int i = 0; i < candidates.size(); i++) {
                CandidateIRV candidate = candidates.get(i);
                String info = i +"| " + candidate.getName() + ", " + candidate.getVotes();
                candidatesInfo.append(info);
                candidatesInfo.append("\n");
            }
            writeToFile(candidatesInfo.toString());

            StringBuilder eliminatedCandidatesInfo = new StringBuilder("Eliminated candidates\n");
            if(eliminated.isEmpty()){
                eliminatedCandidatesInfo.append("0 eliminated candidates thus far");
            } else {
                for (int i = 0; i < eliminated.size(); i++) {
                    CandidateIRV candidate = eliminated.get(i);
                    String info = i +"| " + candidate.getName();
                    eliminatedCandidatesInfo.append(info);
                    if(i < eliminated.size() - 1 ){
                        eliminatedCandidatesInfo.append("\n");
                    }
                }
            }
            writeToFile(eliminatedCandidatesInfo.toString());


            for (CandidateIRV candidate : candidates) {
                if (candidate.getVotes() * 2 > numBallots) {
                    writeToFile("\nMajority found!");
                    displayCalculation(counter,null);
                    System.out.println();
                    System.out.println("\nMajority found!");
                    return candidate;
                }
            }
            writeToFile("\nMajority not found.");

            ArrayList<CandidateIRV> eliminatedCandidates = findLowestCandidate();
            // check for tie
            if(eliminatedCandidates == null) {
                int index = (int)(Math.random() * candidates.size());
                StringBuilder tieMessage = new StringBuilder("There is a tie. Winner will be randomly chosen: ");
                for(CandidateIRV candidate: candidates)  {
                    tieMessage.append(candidate.getName());
                    if(!candidate.getName().equals(candidates.get(numCandidates -1).getName())){
                        tieMessage.append(",");
                    }
                }
                writeToFile(tieMessage.toString());
                displayCalculation(counter, eliminatedCandidates);
                System.out.println();
                System.out.println(tieMessage);
                return candidates.get(index);
            }

            StringBuilder eliminatedMessage = new StringBuilder("These candidate(s) will be eliminated: ");
            for(CandidateIRV eliminatedCandidate: eliminatedCandidates)  {
                eliminatedMessage.append(eliminatedCandidate.getName());
                if(!eliminatedCandidate.getName().equals(eliminatedCandidates.get(eliminatedCandidates.size() -1).getName())){
                    eliminatedMessage.append(",");
                }
            }
            writeToFile(eliminatedMessage.toString());

            eliminateCandidates(eliminatedCandidates);
            displayCalculation(counter, eliminatedCandidates);
            counter++;
        }
    }
}