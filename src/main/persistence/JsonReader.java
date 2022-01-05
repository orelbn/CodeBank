package persistence;

import org.json.JSONArray;
import org.json.JSONObject;
import model.CodeSnippet;
import model.CodeBank;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads the code bank from JSON data stored in file
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads code bank from file and returns it.
    // throws IOException if an error occurs reading data from file
    public CodeBank read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCodeBank(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses CodeBank from JSON object and returns it
    private CodeBank parseCodeBank(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        CodeBank codeBank = new CodeBank();
        addCodeSnippets(codeBank, jsonObject);
        return codeBank;
    }

    // MODIFIES: Code Bank
    // EFFECTS: parses code snippets from JSON object and adds them to Code Bank
    private void addCodeSnippets(CodeBank cb, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("code snippets");
        for (Object json : jsonArray) {
            JSONObject nextCodeSnippet = (JSONObject) json;
            addCodeSnippet(cb, nextCodeSnippet);
        }
    }

    // MODIFIES: CodeBank
    // EFFECTS: parses code snippet from JSON object and adds it to the code bank
    private void addCodeSnippet(CodeBank cb, JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        String language = jsonObject.getString("coding language");
        String body = jsonObject.getString("code body");
        CodeSnippet codeSnippet = new CodeSnippet(title, language, body);
        cb.addSnippet(codeSnippet);
    }


}
