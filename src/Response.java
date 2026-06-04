import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// A Response is a set of Answers produced by taking a Survey or Test.
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private String surveyName;
    private String responderId;
    private List<Answer> answers;

    public Response(String surveyName) {
        this.surveyName = surveyName;
        this.answers = new ArrayList<>();
    }

    public void addAnswer(Answer a) {
        if (a != null) answers.add(a);
    }

    public void display() {
        System.out.println("=== Response to: " + surveyName + " ===");
        for (int i = 0; i < answers.size(); i++) {
            System.out.println((i + 1) + ") ");
            answers.get(i).display();
            System.out.println();
        }
    }

    // Save this response via the FileManager (delegates so all serialization
    // lives in one place). Satisfies the Response save/load design requirement.
    public void save(String fileName) {
        try {
            new FileManager().saveResponse(this, fileName);
        } catch (Exception e) {
            System.out.println("Could not save response: " + e.getMessage());
        }
    }

    public static Response load(String fileName) {
        try {
            return new FileManager().loadResponse(fileName);
        } catch (Exception e) {
            System.out.println("Could not load response: " + e.getMessage());
            return null;
        }
    }

    public void setResponderId(String id) {
        this.responderId = id;
    }

    public String getResponderId() {
        return responderId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
