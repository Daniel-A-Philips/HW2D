import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Abstract base class for all question types in a Survey or Test
public abstract class Question implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected String prompt;
    protected int numResponses;
    // Answers collected for this question (populated in-memory when a survey/test is taken).
    protected List<Answer> answers;

    public Question(String prompt, int numResponses) {
        this.prompt = prompt;
        this.numResponses = Math.max(numResponses, 1);
        this.answers = new ArrayList<>();
    }

    // Display this question to the screen.
    public abstract void display();

    // Allow the user to modify this question (prompt and/or type-specific fields).
    public abstract void modify(InputHelper input);

    // Have the user take (answer) this question, returning an Answer.
    public abstract Answer take(InputHelper input);

    // Record an answer that was given to this question.
    public void addAnswer(Answer a) {
        if (a != null) answers.add(a);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getNumResponses() {
        return numResponses;
    }

    public void setNumResponses(int numResponses) {
        this.numResponses = Math.max(numResponses, 1);
    }
}
