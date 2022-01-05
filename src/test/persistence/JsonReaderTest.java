package persistence;


import model.CodeBank;
import model.CodeSnippet;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


// Class utilized for testing the JsonReader class
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            CodeBank codeBank = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyCodeBank() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyCodeBank.json");
        try {
            CodeBank codeBank = reader.read();
            assertEquals("MyCodeBank", codeBank.getName());
            assertEquals(0, codeBank.length());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralCodeBank() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralCodeBank.json");
        try {
            CodeBank codeBank = reader.read();
            assertEquals("MyCodeBank", codeBank.getName());
            List<CodeSnippet> codeSnippets = codeBank.getCodeSnippets();
            assertEquals(3, codeSnippets.size());
            checkCodeSnippet("Say Hi", "Python", "print(\"Hi\")", codeSnippets.get(0));
            checkCodeSnippet("Say Bye", "Java",
                    "System.out.println(\"Bye\")", codeSnippets.get(1));
            checkCodeSnippet("Say bye", "Java",
                    "System.out.println(\"bye\")", codeSnippets.get(2));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
