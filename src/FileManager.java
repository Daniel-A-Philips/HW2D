import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Handles all serialization/deserialization of Survey, Test, and Response
// objects to and from disk. All paths are relative so the project works
// regardless of where it is unpacked, and the platform file separator is used
// implicitly via the File(parent, child) constructor.
public class FileManager {
    public static final String SURVEY_DIR = "surveys";
    public static final String TEST_DIR = "tests";
    public static final String RESPONSE_DIR = "responses";

    public FileManager() {
        ensureDir(SURVEY_DIR);
        ensureDir(TEST_DIR);
        ensureDir(RESPONSE_DIR);
    }

    private void ensureDir(String dir) {
        File f = new File(dir);
        if (!f.exists()) f.mkdirs(); // Used for checking if the file actually exists
    }

    // Surveys
    public void saveSurvey(Survey s, String fileName) throws IOException {
        if (s == null) throw new IOException("No survey to save.");
        writeObject(s, new File(SURVEY_DIR, ensureSer(fileName)));
    }

    public Survey loadSurvey(String fileName) throws IOException, ClassNotFoundException {
        Object obj = readObject(new File(SURVEY_DIR, fileName));
        if (obj instanceof Survey) return (Survey) obj;
        throw new IOException("File does not contain a Survey object.");
    }

    // Tests
    public void saveTest(Test t, String fileName) throws IOException {
        if (t == null) throw new IOException("No test to save.");
        writeObject(t, new File(TEST_DIR, ensureSer(fileName)));
    }

    public Test loadTest(String fileName) throws IOException, ClassNotFoundException {
        Object obj = readObject(new File(TEST_DIR, fileName));
        if (obj instanceof Test) return (Test) obj;
        throw new IOException("File does not contain a Test object.");
    }

    // Responses
    public void saveResponse(Response r, String fileName) throws IOException {
        if (r == null) throw new IOException("No response to save.");
        writeObject(r, new File(RESPONSE_DIR, ensureSer(fileName)));
    }

    public Response loadResponse(String fileName) throws IOException, ClassNotFoundException {
        Object obj = readObject(new File(RESPONSE_DIR, fileName));
        if (obj instanceof Response) return (Response) obj;
        throw new IOException("File does not contain a Response object.");
    }

    // Lists
    public List<String> listSurveys() { return listFiles(SURVEY_DIR); }
    public List<String> listTests() { return listFiles(TEST_DIR); }
    public List<String> listResponses() { return listFiles(RESPONSE_DIR); }



    // Helpers
    private void writeObject(Object o, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(o);
        }
    }

    private Object readObject(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return ois.readObject();
        }
    }

    private List<String> listFiles(String dir) {
        File folder = new File(dir);
        if (!folder.exists() || !folder.isDirectory()) return new ArrayList<>();
        String[] names = folder.list((d, name) -> name.toLowerCase().endsWith(".ser"));
        if (names == null) return new ArrayList<>();
        List<String> list = new ArrayList<>(Arrays.asList(names));
        Collections.sort(list);
        return list;
    }

    private String ensureSer(String fileName) {
        String safe = sanitize(fileName);
        if (!safe.toLowerCase().endsWith(".ser")) safe += ".ser";
        return safe;
    }

    // Remove unwanted file name additions (not really necessary but good practice)
    private String sanitize(String name) {
        if (name == null || name.trim().isEmpty()) return "untitled";
        return name.trim().replace(" ", "_");
    }
}
