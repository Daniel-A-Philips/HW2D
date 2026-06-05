import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

// Answer for an Essay question. Each entry is one paragraph/response.
// ShortAnswerAnswer subclasses this.
public class EssayAnswer extends Answer {
    @Serial
    private static final long serialVersionUID = 1L;

    protected List<String> responses;

    public EssayAnswer(String questionPrompt, List<String> responses) {
        super(questionPrompt);
        this.responses = new ArrayList<>(responses);
    }

    @Override
    public void display() {
        System.out.println(questionPrompt);
        if (responses.size() > 1) {
            for (int i = 0; i < responses.size(); i++) {
                System.out.println("  " + (char) ('A' + i) + ") " + responses.get(i));
            }
        } else if (!responses.isEmpty()) {
            System.out.println("Answer: " + responses.get(0));
        }
    }

    @Override
    public String getAnswerSummary() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < responses.size(); i++) {
            sb.append(responses.get(i));
            if (i < responses.size() - 1) sb.append(" | ");
        }
        return sb.toString();
    }

    @Override
    public void modify(InputHelper input) {
        List<String> updated = new ArrayList<>();
        for (int i = 0; i < responses.size(); i++) {
            updated.add(input.getString("Response #" + (i + 1) + ": "));
        }
        this.responses = updated;
    }

    public List<String> getResponses() {
        return responses;
    }
}
