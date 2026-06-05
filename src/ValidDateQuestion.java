import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

// A question that asks for a valid calendar date in YYYY-MM-DD format.
// Supports either single or multiple responses.
public class ValidDateQuestion extends Question {
    @Serial
    private static final long serialVersionUID = 1L;

    public ValidDateQuestion(String prompt, int numResponses) {
        super(prompt, numResponses);
    }

    @Override
    public void display() {
        System.out.println(prompt);
        System.out.println("A date should be entered in the following format: YYYY-MM-DD"
                + (numResponses > 1 ? " (" + numResponses + " dates required)" : ""));
    }

    @Override
    public void modify(InputHelper input) {
        System.out.println("Current prompt: " + prompt);
        if (input.getBoolean("Do you wish to modify the prompt? (Y/N): ")) {
            this.prompt = input.getString("Enter a new prompt: ");
            System.out.println("Prompt updated.");
        }
        if (input.getBoolean("Do you wish to modify the number of expected responses (currently " + numResponses + ")? (Y/N): ")) {
            this.numResponses = input.getInt("Enter the new number of responses (1-10): ", 1, 10);
            System.out.println("Number of responses updated.");
        }
    }

    @Override
    public Answer take(InputHelper input) {
        display();
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < numResponses; i++) {
            String d = input.getDate("Date #" + (i + 1) + " (YYYY-MM-DD): ");
            dates.add(d);
        }
        return new DateAnswer(prompt, dates);
    }
}
