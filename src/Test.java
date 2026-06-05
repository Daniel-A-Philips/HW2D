import java.io.Serial;
import java.util.List;

// A Test is a Survey that also has an AnswerKey holding the correct answer for
// each question (essay questions have no correct answer and are not graded).
public class Test extends Survey {
    @Serial
    private static final long serialVersionUID = 1L;

    private final AnswerKey answerKey;

    public Test(String name) {
        super(name);
        this.answerKey = new AnswerKey();
    }

    // Display the test. When showAnswers is true, also print the correct answer
    // beneath each question.
    public void display(boolean showAnswers) {
        System.out.println("=== Test: " + getName() + " ===");
        List<Question> qs = getQuestions();
        if (qs.isEmpty()) {
            System.out.println("(This test has no questions yet.)");
            return;
        }
        for (int i = 0; i < qs.size(); i++) {
            System.out.println((i + 1) + ") " + qs.get(i).getPrompt());
            qs.get(i).display();
            if (showAnswers) {
                Answer correct = answerKey.getAnswer(i);
                if (correct != null) System.out.println("The correct answer is " + correct.getAnswerSummary());
                else System.out.println("(No auto-gradable answer - essay question)");
            }
            System.out.println();
        }
    }

    // Save/Load a Test through the tests directory (overriding Survey, which
    // would otherwise file it under surveys). All serialization lives in the FileManager.
    @Override
    public void save(String fileName) {
        try {
            new FileManager().saveTest(this, fileName);
        } catch (Exception e) {
            System.out.println("Could not save test: " + e.getMessage());
        }
    }

    public static Test loadTest(String fileName) {
        try {
            return new FileManager().loadTest(fileName);
        } catch (Exception e) {
            System.out.println("Could not load test: " + e.getMessage());
            return null;
        }
    }

    public void setCorrectAnswer(int index, Answer a) {
        answerKey.setAnswer(index, a);
    }

    public AnswerKey getAnswerKey() {
        return answerKey;
    }
}
