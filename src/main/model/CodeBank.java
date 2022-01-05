package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents a CodeBank with a list of stored code Snippets
public class CodeBank implements Writable {

    private final String name;
    private final List<CodeSnippet> codeSnippets;

    // EFFECTS: Creates a new empty CodeBank with the name MyCodeBank;
    public CodeBank() {
        this.name = "MyCodeBank";
        codeSnippets = new ArrayList<>();
    }

    // EFFECTS: returns the name of the code bank;
    public String getName() {
        return name;
    }

    // MODIFIES: this
    // EFFECTS: Adds a new code Snippet to the CodeBank,
    //          if a codeSnippet with the given title and language doesn't already exist in the CodeBank
    public Boolean addSnippet(CodeSnippet codeSnippet) {
        for (CodeSnippet codeSnippet1 : codeSnippets) {
            if (codeSnippet.getTitle().equals(codeSnippet1.getTitle())
                    && codeSnippet.getCodeLanguage().equalsIgnoreCase(codeSnippet1.getCodeLanguage())) {
                EventLog.getInstance().logEvent(new Event("Failed To Add Code Snippet"));
                return false;
            }
        }
        codeSnippets.add(codeSnippet);
        EventLog.getInstance().logEvent(new Event("Added Code Snippet: " + codeSnippet.getTitle()));
        return true;
    }

    // MODIFIES: this
    // EFFECTS: Removes a snippet from the CodeBank and returns true, if Snippet was not in codeBank returns false
    public Boolean removeSnippet(String title, String language) {
        if (codeSnippets.removeIf(codeSnippet -> codeSnippet.getTitle().equals(title)
                && codeSnippet.getCodeLanguage().equalsIgnoreCase(language))) {
            EventLog.getInstance().logEvent(new Event("Removed Code Snippet"));
            return true;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: Removes a snippet from the CodeBank if it is contained, otherwise do nothing.
    public void removeSnippet(CodeSnippet codeSnippet) {
        if (codeSnippets.remove(codeSnippet)) {
            EventLog.getInstance().logEvent(new Event("Removed Code Snippet: " + codeSnippet.getTitle()));
        }
    }


    // EFFECTS: Produces the codeSnippet with given title and a language, or produces null if not in the codeBank
    public CodeSnippet retrieveCode(String title, String language) {
        for (CodeSnippet codeSnippet : codeSnippets) {
            if (codeSnippet.getTitle().equals(title)
                    && codeSnippet.getCodeLanguage().equalsIgnoreCase(language)) {
                EventLog.getInstance().logEvent(new Event("Retrieved Code Snippet: " + codeSnippet));
                return codeSnippet;
            }
        }
        return null;
    }

    // EFFECTS: Produces the number of code snippets currently in the CodeBank
    public int length() {
        return codeSnippets.size();
    }

    // REQUIRES: Index to be < this.length()
    // EFFECTS: Produces the snippet at the given index or return an Error if out of bounds
    public CodeSnippet get(int index) {
        return codeSnippets.get(index);
    }

    // EFFECTS: Produces a list of code snippets starting with the given string
    public ArrayList<CodeSnippet> searchByTitle(String keywords) {
        ArrayList<CodeSnippet> snippetsFilteredByKeyWords = new ArrayList<>();
        for (CodeSnippet codeSnippet : codeSnippets) {
            if (codeSnippet.getTitle().startsWith(keywords)) {
                snippetsFilteredByKeyWords.add(codeSnippet);
            }

        }
        Event searchedCodeSnippet = new Event("Searched Code Snippet Bank By String: " + keywords);
        return snippetsFilteredByKeyWords;
    }

    // EFFECTS: Produces a list of code snippets using the given language
    public ArrayList<CodeSnippet> searchByLanguage(String language) {
        ArrayList<CodeSnippet> snippetsFilteredByLanguage = new ArrayList<>();
        for (CodeSnippet codeSnippet : codeSnippets) {
            if (codeSnippet.getCodeLanguage().equalsIgnoreCase(language)) {
                snippetsFilteredByLanguage.add(codeSnippet);
            }

        }
        EventLog.getInstance().logEvent(new Event("Searched Code Snippet By Language: " + language));
        return snippetsFilteredByLanguage;
    }

    // ATTRIBUTION: The 3 Methods below are based on methods found in JsonSerializationDemo
    // LINK TO CODE: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    // EFFECTS: returns an unmodifiable list of CodeSnippets in this Code Bank
    public List<CodeSnippet> getCodeSnippets() {
        return Collections.unmodifiableList(codeSnippets);
    }

    // EFFECTS: returns this code bank as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("code snippets", codeSnippetsToJson());
        return json;
    }

    // EFFECTS: returns codeSnippets in this code bank as a JSON array
    private JSONArray codeSnippetsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (CodeSnippet codeSnippet : codeSnippets) {
            jsonArray.put(codeSnippet.toJson());
        }

        return jsonArray;
    }

}

