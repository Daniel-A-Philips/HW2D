import java.io.Serial;
import java.util.List;

// Answer for a Short Answer question. A short answer is an essay with a
// character limit, so this is a thin subclass of EssayAnswer.
public class ShortAnswerAnswer extends EssayAnswer {
    @Serial
    private static final long serialVersionUID = 1L;

    public ShortAnswerAnswer(String questionPrompt, List<String> responses) {
        super(questionPrompt, responses);
    }
}
