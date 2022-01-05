package model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// This class is utilized for testing the CodeBank class
class CodeBankTest {

    CodeBank testCodeBank;
    CodeSnippet testSnippet1;
    CodeSnippet testSnippet2;
    CodeSnippet testSnippet3;
    CodeSnippet testSnippet4;

    @BeforeEach
    void setup() {
        testCodeBank = new CodeBank();
        testSnippet1 = new CodeSnippet("Say hi", "Python", "print(\"hi\")");
        testSnippet2 = new CodeSnippet("Say hi", "Java", "System.out.println(\"hi\")");
        testSnippet3 = new CodeSnippet("Say bye", "Java", "System.out.println(\"bye\")");
        testSnippet4 = new CodeSnippet("Wow", "Java", "wow");
    }

    @Test
    void testAddCodeAddACouple() {
        assertTrue(testCodeBank.addSnippet(testSnippet1));
        assertEquals(1, testCodeBank.length());
        assertEquals(testSnippet1, testCodeBank.get(0));
        assertTrue(testCodeBank.addSnippet(testSnippet2));
        assertEquals(2, testCodeBank.length());
        assertEquals(testSnippet2, testCodeBank.get(1));
    }

    @Test
    void testAddCodeSnippetAlreadyHaveOneWithSameTitleInCodeBank() {
        assertTrue(testCodeBank.addSnippet(testSnippet1));
        assertFalse(testCodeBank.addSnippet(testSnippet1));
        assertFalse(testCodeBank.addSnippet(testSnippet1));
        assertEquals(testSnippet1, testCodeBank.get(0));
        assertEquals(1, testCodeBank.length());
        assertFalse(testCodeBank.addSnippet(new CodeSnippet("Say hi", "Python", "print(\"bye\")")));
        assertTrue(testCodeBank.addSnippet(testSnippet2));
        assertFalse(testCodeBank.addSnippet(testSnippet2));
        assertEquals(testSnippet2, testCodeBank.get(1));
        assertEquals(2, testCodeBank.length());

    }

    @Test
    void testBothRemoveCodeSnippetMethodsOnlyOneSnippetInCodeBank() {
        testCodeBank.addSnippet(testSnippet1);
        assertEquals(1, testCodeBank.length());
        assertTrue(testCodeBank.removeSnippet(testSnippet1.getTitle(), testSnippet1.getCodeLanguage()));
        assertEquals(0, testCodeBank.length());
        testCodeBank.addSnippet(testSnippet1);
        assertEquals(1, testCodeBank.length());
        testCodeBank.removeSnippet(testSnippet1);
        assertEquals(0, testCodeBank.length());

    }

    @Test
    void testBothRemoveCodeSnippetMethodsFirstSnippetInCodeBank() {
        testCodeBank.addSnippet(testSnippet1);
        testCodeBank.addSnippet(testSnippet2);
        testCodeBank.addSnippet(testSnippet3);
        assertEquals(3, testCodeBank.length());
        assertTrue(testCodeBank.removeSnippet(testSnippet1.getTitle(), testSnippet1.getCodeLanguage()));
        assertEquals(2, testCodeBank.length());
        assertEquals(testCodeBank.get(0), testSnippet2);
        assertEquals(testCodeBank.get(1), testSnippet3);
        testCodeBank.addSnippet(testSnippet1);
        assertEquals(3, testCodeBank.length());
        testCodeBank.removeSnippet(testSnippet2);
        assertEquals(2, testCodeBank.length());
        assertEquals(testCodeBank.get(0), testSnippet3);
        assertEquals(testCodeBank.get(1), testSnippet1);

    }

    @Test
    void testBothRemoveCodeSnippetMethodsMiddleSnippetInCodeBank() {
        testCodeBank.addSnippet(testSnippet1);
        testCodeBank.addSnippet(testSnippet2);
        testCodeBank.addSnippet(testSnippet3);
        assertEquals(3, testCodeBank.length());
        assertTrue(testCodeBank.removeSnippet(testSnippet2.getTitle(), testSnippet2.getCodeLanguage()));
        assertEquals(2, testCodeBank.length());
        assertEquals(testCodeBank.get(0), testSnippet1);
        assertEquals(testCodeBank.get(1), testSnippet3);
        testCodeBank.addSnippet(testSnippet2);
        assertEquals(3, testCodeBank.length());
        testCodeBank.removeSnippet(testSnippet3);
        assertEquals(2, testCodeBank.length());
        assertEquals(testCodeBank.get(0), testSnippet1);
        assertEquals(testCodeBank.get(1), testSnippet2);


    }

    @Test
    void testBothRemoveCodeSnippetMethodsLastSnippetInCodeBank() {
        testCodeBank.addSnippet(testSnippet1);
        testCodeBank.addSnippet(testSnippet2);
        testCodeBank.addSnippet(testSnippet3);
        assertEquals(3, testCodeBank.length());
        assertTrue(testCodeBank.removeSnippet(testSnippet3.getTitle(), testSnippet3.getCodeLanguage()));
        assertEquals(2, testCodeBank.length());
        assertEquals(testCodeBank.get(0), testSnippet1);
        assertEquals(testCodeBank.get(1), testSnippet2);
        testCodeBank.addSnippet(testSnippet3);
        assertEquals(3, testCodeBank.length());
        testCodeBank.removeSnippet(testSnippet3);
        assertEquals(2, testCodeBank.length());
        assertEquals(testCodeBank.get(0), testSnippet1);
        assertEquals(testCodeBank.get(1), testSnippet2);

    }

    @Test
    void testBothRemoveCodeSnippetSnippetNotInTheCodeBank() {
        testCodeBank.addSnippet(testSnippet1);
        testCodeBank.addSnippet(testSnippet2);
        testCodeBank.addSnippet(testSnippet3);
        assertEquals(3, testCodeBank.length());
        assertFalse(testCodeBank.removeSnippet("Hello", "DrRacket"));
        testCodeBank.removeSnippet(new CodeSnippet("Hello", "DrRacket", "HI HI HI"));
        assertEquals(3, testCodeBank.length());
        assertEquals(testCodeBank.get(0), testSnippet1);
        assertEquals(testCodeBank.get(1), testSnippet2);
        assertEquals(testCodeBank.get(2), testSnippet3);

    }

    @Test
    void testRetrieveCodeSnippetOnlyOneSnippet() {
        testCodeBank.addSnippet(testSnippet1);
        CodeSnippet snippet1 = testCodeBank.retrieveCode(testSnippet1.getTitle(), testSnippet1.getCodeLanguage());
        assertEquals(testSnippet1, snippet1);
    }

    @Test
    void testRetrieveCodeSnippetRetrieveFirstMiddleAndLastSnippets() {
        testCodeBank.addSnippet(testSnippet1);
        testCodeBank.addSnippet(testSnippet2);
        testCodeBank.addSnippet(testSnippet3);
        CodeSnippet snippet1 = testCodeBank.retrieveCode(testSnippet1.getTitle(), testSnippet1.getCodeLanguage());
        CodeSnippet snippet2 = testCodeBank.retrieveCode(testSnippet2.getTitle(), testSnippet2.getCodeLanguage());
        CodeSnippet snippet3 = testCodeBank.retrieveCode(testSnippet3.getTitle(), testSnippet3.getCodeLanguage());
        assertEquals(testSnippet1, snippet1);
        assertEquals(testSnippet2, snippet2);
        assertEquals(testSnippet3, snippet3);
    }

    @Test
    void testRetrieveCodeSnippetNotInCodeBank() {
        assertTrue(testCodeBank.addSnippet(testSnippet1));
        assertNull(testCodeBank.retrieveCode("I am a barbie girl", "in a barbie world"));

    }

    @Test
    void testSearchByTitleNoSnippetsStored() {
        assertEquals(0, testCodeBank.searchByTitle("Hello").size());

    }

    @Test
    void testSearchByTitleNoSnippetsStoredStartingWithTheGivenTitle() {
        testCodeBank.addSnippet(testSnippet1);
        testCodeBank.addSnippet(testSnippet2);
        testCodeBank.addSnippet(testSnippet3);
        assertEquals(0, testCodeBank.searchByTitle("Hello").size());

    }

    @Test
    void testSearchByTitleOneSnippetStartingWithTheGivenTitle() {
        testCodeBank.addSnippet(testSnippet1);
        testCodeBank.addSnippet(testSnippet3);
        testCodeBank.addSnippet(testSnippet4);
        ArrayList<CodeSnippet> filteredList = testCodeBank.searchByTitle(testSnippet1.getTitle());
        assertEquals(1, filteredList.size());
        assertEquals(testSnippet1.getTitle(), filteredList.get(0).getTitle());
    }

    @Test
    void testSearchByTitleMultipleCodeSnippetsStartingWithTheGivenString() {
        testCodeBank.addSnippet(testSnippet1);
        testCodeBank.addSnippet(testSnippet3);
        testCodeBank.addSnippet(testSnippet4);
        ArrayList<CodeSnippet> filteredList = testCodeBank.searchByTitle(testSnippet1.getTitle());
        assertEquals(1, filteredList.size());
        assertEquals(testSnippet1.getTitle(), filteredList.get(0).getTitle());
        testCodeBank.addSnippet(testSnippet2);
        filteredList = testCodeBank.searchByTitle(testSnippet1.getTitle());
        assertEquals(testSnippet2.getTitle(), filteredList.get(1).getTitle());
        assertEquals(testSnippet1.getCodeLanguage(), filteredList.get(0).getCodeLanguage());
        assertEquals(testSnippet2.getCodeLanguage(), filteredList.get(1).getCodeLanguage());
        filteredList = testCodeBank.searchByTitle("Say");
        assertEquals(3, filteredList.size());
        assertEquals(testSnippet2.getSnippetCode(), filteredList.get(2).getSnippetCode());
    }

    @Test
    void testSearchByLanguageEmptyCodeBank() {
        assertEquals(0, testCodeBank.searchByLanguage("JavaScript").size());
    }

    @Test
    void testSearchByLanguageNoCodeWithTheGivenLanguage() {
        testCodeBank.addSnippet(testSnippet1);
        testCodeBank.addSnippet(testSnippet2);
        testCodeBank.addSnippet(testSnippet3);
        testCodeBank.addSnippet(testSnippet4);
        ArrayList<CodeSnippet> filteredList = testCodeBank.searchByLanguage("R");
        assertEquals(0, filteredList.size());

    }

    @Test
    void testSearchByLanguageOneSnippetWithTheGivenCodeLanguage() {
        testCodeBank.addSnippet(testSnippet1);
        testCodeBank.addSnippet(testSnippet2);
        testCodeBank.addSnippet(testSnippet3);
        testCodeBank.addSnippet(testSnippet4);
        ArrayList<CodeSnippet> filteredList = testCodeBank.searchByLanguage("Python");
        assertEquals(1, filteredList.size());
        assertEquals("Python", filteredList.get(0).getCodeLanguage());

    }

    @Test
    void testSearchByLanguageMultipleSnippetsWithTheGivenCodeLanguage() {
        testCodeBank.addSnippet(testSnippet1);
        testCodeBank.addSnippet(testSnippet2);
        testCodeBank.addSnippet(testSnippet3);
        testCodeBank.addSnippet(testSnippet4);
        ArrayList<CodeSnippet> filteredList = testCodeBank.searchByLanguage("Java");
        assertEquals(3, filteredList.size());
        assertEquals("Java", filteredList.get(0).getCodeLanguage());
        assertEquals("Java", filteredList.get(1).getCodeLanguage());
        assertEquals("Java", filteredList.get(2).getCodeLanguage());
        assertEquals("Say hi", filteredList.get(0).getTitle());
    }

    @Test
    void testGetCodeSnippetsNoStoredSnippets() {
        assertEquals(0, testCodeBank.getCodeSnippets().size());
        List<CodeSnippet> storedSnippets = testCodeBank.getCodeSnippets();
        assertEquals(0, storedSnippets.size());
    }


    @Test
    void testGetCodeSnippetsOneCodeSnippets() {
        testCodeBank.addSnippet(testSnippet1);
        assertEquals(1, testCodeBank.getCodeSnippets().size());
        List<CodeSnippet> storedSnippets = testCodeBank.getCodeSnippets();
        assertEquals(testSnippet1, storedSnippets.get(0));
    }

    @Test
    void testGetCodeSnippetsMultipleCodeSnippets() {
        testCodeBank.addSnippet(testSnippet1);
        testCodeBank.addSnippet(testSnippet2);
        testCodeBank.addSnippet(testSnippet3);
        testCodeBank.addSnippet(testSnippet4);
        assertEquals(4, testCodeBank.getCodeSnippets().size());
        List<CodeSnippet> storedSnippets = testCodeBank.getCodeSnippets();
        assertEquals(testSnippet1, storedSnippets.get(0));
        assertEquals(testSnippet2, storedSnippets.get(1));
        assertEquals(testSnippet3, storedSnippets.get(2));
        assertEquals(testSnippet4, storedSnippets.get(3));
    }
}