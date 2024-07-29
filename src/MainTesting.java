import ElectionMachine.Main;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class MainTesting {
    @Before
    public void setup() {
    }

    @Test
    public void readFileTest(){
        String input = "mainTest1.csv";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        String[] expected_output1 = {
                "\uFEFFOPL,,,,,", "6,,,,,", "Biden (D),Sanders (D),Trump (R),DeSantis (R),Ramaswamy (R),Kennedy (I)",
                "3,,,,,", "12,,,,,", "1,,,,,", "1,,,,,", "1,,,,,", "1,,,,,", "1,,,,,", "1,,,,,",
                ",1,,,,", ",,1,,,", ",,1,,,", ",,,1,,", ",,,1,,", ",,,,,1"
        };
        assertEquals(Main.readFile(),expected_output1);

        input = "mainTest2.csv";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        String[] expected_output2 = {
                "IR", "4", "Rosen (D), Kleinberg (R), Chou (I), Royce (L)", "6",
                "1,3,4,2", "1,,2,", "1,2,3,", "3,2,1,4", ",,1,2", ",,,1"
        };
        assertEquals(Main.readFile(),expected_output2);

        input = "mainTest3.csv";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        String[] expected_output3 = {};
        assertEquals(Main.readFile(),expected_output3);
    }
}