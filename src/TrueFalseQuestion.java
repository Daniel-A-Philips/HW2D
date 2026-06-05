import java.io.Serial;
import java.util.Arrays;

// A True/False question. By definition it is a Multiple Choice question with
// two fixed choices (True/False) and a single response
public class TrueFalseQuestion extends MultipleChoiceQuestion {
    @Serial
    private static final long serialVersionUID = 1L;

    public TrueFalseQuestion(String prompt) {
        super(prompt, Arrays.asList("True", "False"), 1);
    }

    @Override
    public void display() {
        System.out.println(prompt);
        System.out.println("T/F");
    }

    @Override
    public void modify(InputHelper input) {
        System.out.println("Current prompt: " + prompt);
        if (input.getBoolean("Do you wish to modify the prompt? (Y/N): ")) {
            this.prompt = input.getString("Enter a new prompt: ");
            System.out.println("Prompt updated.");
        }
    }

    @Override
    public Answer take(InputHelper input) {
        display();
        // getBoolean already accepts T/F/true/false and re-prompts on bad input.
        boolean value = input.getBoolean("Your answer (T/F): ");
        return new TrueFalseAnswer(prompt, value);
    }
}
