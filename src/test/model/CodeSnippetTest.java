package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// This class is utilized for testing the CodeSnippet class
public class CodeSnippetTest {

    CodeSnippet testSnippet1;


    @BeforeEach
    void setup() {
        testSnippet1 = new CodeSnippet("For-Loop", "Python",
                "for {item} in {collection}: ");
    }

    @Test
    void testGetTitle() {
        assertEquals("For-Loop", testSnippet1.getTitle());
    }

    @Test
    void testGetLanguage() {
        assertEquals("Python", testSnippet1.getCodeLanguage());
    }

    @Test
    void testGetSnippetCode() {
        assertEquals("for {item} in {collection}: ", testSnippet1.getSnippetCode());
    }

    @Test
    void testToString() {
        assertEquals("For-Loop (Python)", testSnippet1.toString());
    }

    @Test
    void testSetText() {
        testSnippet1.setText("Hello world");
        assertEquals("Hello world", testSnippet1.getSnippetCode());
        testSnippet1.setText("");
        assertEquals("", testSnippet1.getSnippetCode());
    }
}
