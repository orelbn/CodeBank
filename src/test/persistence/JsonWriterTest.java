package persistence;



import model.CodeBank;
import model.CodeSnippet;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Class utilized for testing the JsonWriter class
public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            CodeBank codeBank = new CodeBank();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyCodeBank() {
        try {
            CodeBank codeBank = new CodeBank();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyCodeBank.json");
            writer.open();
            writer.write(codeBank);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyCodeBank.json");
            codeBank = reader.read();
            assertEquals("MyCodeBank", codeBank.getName());
            assertEquals(0, codeBank.length());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralCodeBank() {
        try {
            CodeBank codeBank = new CodeBank();
            codeBank.addSnippet(new CodeSnippet("Say Hi", "Python", "print(\"Hi\")"));
            codeBank.addSnippet(new CodeSnippet("Say Bye", "Java",
                    "System.out.println(\"Bye\")"));
            codeBank.addSnippet(new CodeSnippet("Say bye", "Java",
                    "System.out.println(\"bye\")"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralCodeBank.json");
            writer.open();
            writer.write(codeBank);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralCodeBank.json");
            codeBank = reader.read();
            assertEquals("MyCodeBank", codeBank.getName());
            List<CodeSnippet> codeSnippets = codeBank.getCodeSnippets();
            assertEquals(3, codeSnippets.size());
            checkCodeSnippet("Say Hi", "Python", "print(\"Hi\")", codeSnippets.get(0));
            checkCodeSnippet("Say Bye", "Java",
                    "System.out.println(\"Bye\")", codeSnippets.get(1));
            checkCodeSnippet("Say bye", "Java",
                    "System.out.println(\"bye\")", codeSnippets.get(2));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
