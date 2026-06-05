import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

// A Multiple Choice question with a configurable number of choices and a
// configurable number of expected responses (single or multiple).
public class MultipleChoiceQuestion extends Question {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<String> choices;

    public MultipleChoiceQuestion(String prompt, List<String> choices, int numResponses) {
        super(prompt, numResponses);
        this.choices = (choices == null) ? new ArrayList<>() : new ArrayList<>(choices);
    }

    @Override
    public void display() {
        System.out.println(prompt);
        if (numResponses > 1) System.out.println("(Please select " + numResponses + " choices.)");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < choices.size(); i++) {
            sb.append((char) ('A' + i)).append(") ").append(choices.get(i));
            if (i < choices.size() - 1) sb.append("  ");
        }
        System.out.println(sb);
    }

    @Override
    public void modify(InputHelper input) {
        System.out.println("Current prompt: " + prompt);
        if (input.getBoolean("Do you wish to modify the prompt? (Y/N): ")) {
            this.prompt = input.getString("Enter a new prompt: ");
            System.out.println("Prompt updated.");
        }
        if (input.getBoolean("Do you wish to modify choices? (Y/N): ")) {
            while (true) {
                System.out.println("Current choices:");
                for (int i = 0; i < choices.size(); i++) {
                    System.out.println("  " + (char) ('A' + i) + ") " + choices.get(i));
                }
                int index = input.getInt(
                        "Which choice do you want to modify? (1-" + choices.size() + ", 0 to finish): ",
                        0, choices.size());
                if (index == 0) break;
                String newVal = input.getString("Enter the new value for choice " + (char) ('A' + index - 1) + ": ");
                choices.set(index - 1, newVal);
                System.out.println("Choice updated.");
                if (!input.getBoolean("Modify another choice? (Y/N): ")) break;
            }
        }
        if (input.getBoolean("Do you wish to modify the number of expected responses? (Y/N): ")) {
            this.numResponses = input.getInt(
                    "Enter the number of expected responses (1-" + choices.size() + "): ",
                    1, choices.size());
            System.out.println("Number of responses updated.");
        }
    }

    @Override
    public Answer take(InputHelper input) {
        display();
        List<String> selected = new ArrayList<>();
        for (int i = 0; i < numResponses; i++) {
            while (true) {
                String response = input.getString(
                        "Enter choice #" + (i + 1) + " (A-" + (char) ('A' + choices.size() - 1) + "): ")
                        .trim().toUpperCase();
                if (response.length() != 1) { System.out.println("Please enter a single letter."); continue; }
                int index = response.charAt(0) - 'A';
                if (index < 0 || index >= choices.size()) { System.out.println("That choice is out of range."); continue; }
                if (selected.contains(choices.get(index))) {
                    System.out.println("You already selected that choice. Pick a different one.");
                    continue;
                }
                selected.add(choices.get(index));
                break;
            }
        }
        return new MultipleChoiceAnswer(prompt, choices, selected);
    }

    public List<String> getChoices() {
        return choices;
    }

    // Replace a single choice (matches setChoice in the UML).
    public void setChoice(int index, String choice) {
        if (index >= 0 && index < choices.size()) choices.set(index, choice);
    }
}
