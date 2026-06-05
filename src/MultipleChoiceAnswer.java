import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

// Answer class for a Multiple Choice question. Stores the choices that were
// presented and the choice(s) the user selected. TrueFalseAnswer subclasses this.
public class MultipleChoiceAnswer extends Answer {
    @Serial
    private static final long serialVersionUID = 1L;

    protected List<String> choices;
    protected List<String> selectedChoices;

    public MultipleChoiceAnswer(String questionPrompt, List<String> choices, List<String> selectedChoices) {
        super(questionPrompt);
        this.choices = new ArrayList<>(choices);
        this.selectedChoices = new ArrayList<>(selectedChoices);
    }

    @Override
    public void display() {
        System.out.println(questionPrompt);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < choices.size(); i++) {
            sb.append((char) ('A' + i)).append(") ").append(choices.get(i));
            if (i < choices.size() - 1) sb.append("  ");
        }
        System.out.println(sb);
        System.out.println("Answer: " + getAnswerSummary());
    }

    @Override
    public String getAnswerSummary() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedChoices.size(); i++) {
            int index = choices.indexOf(selectedChoices.get(i));
            if (index >= 0) sb.append((char) ('A' + index)).append(") ").append(choices.get(index));
            else sb.append("?");
            if (i < selectedChoices.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    @Override
    public void modify(InputHelper input) {
        int need = selectedChoices.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < choices.size(); i++) sb.append((char) ('A' + i)).append(") ").append(choices.get(i)).append("  ");
        System.out.println(sb.toString().trim());
        List<String> updated = new ArrayList<>();
        for (int i = 0; i < need; i++) {
            while (true) {
                String resp = input.getString(
                        "Enter choice #" + (i + 1) + " (A-" + (char) ('A' + choices.size() - 1) + "): ")
                        .trim().toUpperCase();
                if (resp.length() != 1) { System.out.println("Please enter a single letter."); continue; }
                int index = resp.charAt(0) - 'A';
                if (index < 0 || index >= choices.size()) { System.out.println("That choice is out of range."); continue; }
                if (updated.contains(choices.get(index))) { System.out.println("Already selected. Pick another."); continue; }
                updated.add(choices.get(index));
                break;
            }
        }
        this.selectedChoices = updated;
    }

    public List<String> getSelectedChoices() {
        return selectedChoices;
    }

    public List<String> getChoices() {
        return choices;
    }
}
