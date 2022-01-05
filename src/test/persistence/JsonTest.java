package persistence;



import model.CodeSnippet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Provides a method to be used for testing in the JsonReaderTest and JsonWriterTest classes.
public class JsonTest {

    protected void checkCodeSnippet(String title, String language, String codeBody, CodeSnippet codeSnippet) {
        assertEquals(title, codeSnippet.getTitle());
        assertEquals(language, codeSnippet.getCodeLanguage());
        assertEquals(codeBody, codeSnippet.getSnippetCode());
    }
}
