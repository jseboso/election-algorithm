package ElectionMachine;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Prompts the user for an input file and begins the election algorithm
 * to determine winners
 */
public class Main {
    /**
     * Prompts the user to enter a filename and retrieves the file's data
     *
     * @return Contents of the input file
     */
    public static String[] readFile() {
        Scanner nameScanner = new Scanner(System.in);
        System.out.println("Please enter the name of the file to input:");
        Scanner s = null;
        File myFile = null;
        // Attempts to open file
        while (s == null) {
            String filename = nameScanner.next();
            myFile = new File(filename);
            try {
                s = new Scanner(myFile);
            } catch (Exception e) {
                System.out.println("Invalid filename input! Try again");
            }
        }
        nameScanner.close();

        // Counts amount of lines in file
        int numLines = 0;
        while(s.hasNextLine()){
            numLines++;
            s.nextLine();
        }
        s.close();
        String[] electionData = new String[numLines];
        Scanner infoReader;
        try {
            infoReader = new Scanner(myFile);
        } catch (Exception e) {
            return null;
        }
        int index = 0;
        while(infoReader.hasNextLine()){
            electionData[index] = infoReader.nextLine();
            index++;
        }
        infoReader.close();
        return electionData;
    }

    /**
     * Runs the specified voting algorithm class methods
     *
     * @param electionData input file contents containing election info
     */
    public static void runVoting(String[] electionData) {
        String votingMethod = electionData[0];
        System.out.println("Voting method is "+votingMethod);
        if (votingMethod.equals("OPL")) {
            OPL myOPL = new OPL(electionData);
            myOPL.processElection();
        } else if (votingMethod.equals("IR")) {
            IRV myIRV = new IRV(electionData);
            myIRV.processElection();
        } else if (votingMethod.equals("MPO")) {
            MPO myMPO = new MPO(electionData);
            myMPO.processElection();
        }
    }

    /**
     * Main method that starts the system
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        String[] electionData = readFile(); //electionDate might be null
        System.out.println(Arrays.toString(electionData));
        runVoting(electionData != null ? electionData : new String[0]);
    }
}
