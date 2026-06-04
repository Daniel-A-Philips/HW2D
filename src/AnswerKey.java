import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Holds the correct answers for a Test, aligned by question index. A null entry
// means that question has no auto-gradable answer (e.g. an essay).
public class AnswerKey implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Answer> correctAnswers;

    public AnswerKey() {
        this.correctAnswers = new ArrayList<>();
    }

    public void setAnswer(int i, Answer a) {
        while (correctAnswers.size() <= i) correctAnswers.add(null);
        correctAnswers.set(i, a);
    }

    public Answer getAnswer(int i) {
        if (i < 0 || i >= correctAnswers.size()) return null;
        return correctAnswers.get(i);
    }

    public List<Answer> getAnswers() {
        return correctAnswers;
    }

    public void display() {
        for (int i = 0; i < correctAnswers.size(); i++) {
            Answer a = correctAnswers.get(i);
            System.out.println((i + 1) + ") " + (a == null ? "(none)" : a.getAnswerSummary()));
        }
    }
}
