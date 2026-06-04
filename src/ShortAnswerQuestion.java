import java.util.ArrayList;
import java.util.List;

// A Short Answer question. It is an Essay question that additionally enforces
// a character limit, so it is implemented as a subclass of EssayQuestion.
public class ShortAnswerQuestion extends EssayQuestion {
    private static final long serialVersionUID = 1L;

    private int charLimit;

    public ShortAnswerQuestion(String prompt, int charLimit, int numResponses) {
        super(prompt, numResponses);
        this.charLimit = (charLimit < 1) ? 50 : charLimit;
    }

    @Override
    public void display() {
        System.out.println(prompt);
        System.out.println("(Short answer, max " + charLimit + " characters"
                + (numResponses > 1 ? ", " + numResponses + " responses required" : "") + ")");
    }

    @Override
    public void modify(InputHelper input) {
        System.out.println("Current prompt: " + prompt);
        if (input.getBoolean("Do you wish to modify the prompt? (Y/N): ")) {
            this.prompt = input.getString("Enter a new prompt: ");
            System.out.println("Prompt updated.");
        }
        if (input.getBoolean("Do you wish to modify the character limit (currently " + charLimit + ")? (Y/N): ")) {
            this.charLimit = input.getInt("Enter the new character limit (1-1000): ", 1, 1000);
            System.out.println("Character limit updated.");
        }
        if (input.getBoolean("Do you wish to modify the number of expected responses (currently " + numResponses + ")? (Y/N): ")) {
            this.numResponses = input.getInt("Enter the new number of responses (1-10): ", 1, 10);
            System.out.println("Number of responses updated.");
        }
    }

    @Override
    public Answer take(InputHelper input) {
        display();
        List<String> responses = new ArrayList<>();
        for (int i = 0; i < numResponses; i++) {
            while (true) {
                String r = input.getString("Response #" + (i + 1) + ": ");
                if (r.length() > charLimit) {
                    System.out.println("Too long: please keep it under " + charLimit + " characters.");
                    continue;
                }
                responses.add(r);
                break;
            }
        }
        return new ShortAnswerAnswer(prompt, responses);
    }

    public int getCharLimit() {
        return charLimit;
    }
}
