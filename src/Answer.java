import java.io.Serial;
import java.io.Serializable;

// Abstract base class for all answer types.
// Each Answer remembers the prompt of the question it answered, so a
// Response can be displayed in human-readable form even after loading.
public abstract class Answer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected String questionPrompt;

    public Answer(String questionPrompt) {
        this.questionPrompt = questionPrompt;
    }

    // Display this answer (prompt + the user's response) to the screen.
    public abstract void display();

    // Re-prompt the user to change this answer in place.
    // Used when editing a Tests correct answer.
    public abstract void modify(InputHelper input);

    // A short, answer-only string (no prompt). Used for correct-answer
    // display on a test and as a label during tabulation/grading.
    public abstract String getAnswerSummary();

    public String getQuestionPrompt() {
        return questionPrompt;
    }
}
