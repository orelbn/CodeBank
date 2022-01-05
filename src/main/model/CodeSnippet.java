package model;

import org.json.JSONObject;
import persistence.Writable;

import javax.swing.*;

// Represents a code snippet, having a title, code language, and Code body
public class CodeSnippet implements Writable {

    private final String snippetTitle;
    private final String codeLanguage;
    private String snippetCode;

    // EFFECTS: Constructs a new CodeSnippet object with the given title, language and body.
    public CodeSnippet(String title, String language, String body) {

        snippetTitle = title;
        codeLanguage = language;
        snippetCode = body;
    }

    // EFFECTS: Provides the title of the code snippet
    public String getTitle() {
        return snippetTitle;
    }

    // EFFECTS: provides the language in which the code snippet is written in
    public String getCodeLanguage() {
        return codeLanguage;
    }

    // EFFECTS: provides the body of the code snippet
    public String getSnippetCode() {
        return snippetCode;
    }

    // ATTRIBUTION: The method below is based on a method found in JsonSerializationDemo
    // LINK TO CODE: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    @Override
    // EFFECTS: returns Code Snippet as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", snippetTitle);
        json.put("coding language", codeLanguage);
        json.put("code body", snippetCode);
        return json;
    }

    @Override
    // EFFECTS: returns a String containing the title and the coding language of the codeSnippet
    public String toString() {
        return snippetTitle + " (" + codeLanguage + ")";
    }

    // MODIFIES: this
    // EFFECTS: Changes the body of the code snippet to the given String
    public void setText(String codeBody) {
        snippetCode = codeBody;
    }
}
