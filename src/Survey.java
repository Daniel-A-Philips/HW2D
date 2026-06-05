import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// A Survey is a named collection of Question objects. It can also keep the
// Responses gathered for it (in-memory during a session).
public class Survey implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<Question> questions;
    private final List<Response> responses;

    public Survey(String name) {
        this.name = name;
        this.questions = new ArrayList<>();
        this.responses = new ArrayList<>();
    }

    public void addQuestion(Question q) {
        if (q != null) questions.add(q);
    }

    public void removeQuestion(int index) {
        if (index >= 0 && index < questions.size()) questions.remove(index);
    }

    public void modifyQuestion(int index, InputHelper input) {
        if (index >= 0 && index < questions.size()) questions.get(index).modify(input);
    }

    public void addResponse(Response r) {
        if (r != null) responses.add(r);
    }

    // Display the entire survey, numbering each question.
    public void display() {
        System.out.println("=== Survey: " + name + " ===");
        if (questions.isEmpty()) { System.out.println("(This survey has no questions yet.)"); return; }
        for (int i = 0; i < questions.size(); i++) {
            System.out.println((i + 1) + ") " + questions.get(i).getPrompt());
            questions.get(i).display();   // type-specific bit (polymorphism)
            System.out.println();
        }
    }

    // Take the survey, asking each question and returning a Response. Each
    // answer is also aggregated onto its Question and onto this Survey.
    public Response take(InputHelper input) {
        Response response = new Response(name);
        System.out.println("Now taking: " + name);
        for (int i = 0; i < questions.size(); i++) {
            System.out.println();
            System.out.println("Question " + (i + 1) + " of " + questions.size() + ":");
            Answer a = questions.get(i).take(input);
            response.addAnswer(a);
            questions.get(i).addAnswer(a);
        }
        addResponse(response);
        return response;
    }

    // Save/Load via the FileManager so all serialization lives in one place.
    public void save(String fileName) {
        try {
            new FileManager().saveSurvey(this, fileName);
        } catch (Exception e) {
            System.out.println("Could not save survey: " + e.getMessage());
        }
    }

    public static Survey load(String fileName) {
        try {
            return new FileManager().loadSurvey(fileName);
        } catch (Exception e) {
            System.out.println("Could not load survey: " + e.getMessage());
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<Response> getResponses() {
        return responses;
    }
}
