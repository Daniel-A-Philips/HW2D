import java.util.Arrays;
import java.util.Collections;

// Stores a single boolean answer for a True/False question. Because a T/F
// question is a Multiple Choice question, this is a MultipleChoiceAnswer whose
// single selected choice is either "True" or "False".
public class TrueFalseAnswer extends MultipleChoiceAnswer {
    private static final long serialVersionUID = 1L;

    public TrueFalseAnswer(String questionPrompt, boolean value) {
        super(questionPrompt, Arrays.asList("True", "False"),
                Collections.singletonList(value ? "True" : "False"));
    }

    public boolean getValue() {
        return !selectedChoices.isEmpty() && selectedChoices.get(0).equals("True");
    }

    @Override
    public void display() {
        System.out.println(questionPrompt);
        System.out.println("T/F");
        System.out.println("Answer: " + getAnswerSummary());
    }

    @Override
    public String getAnswerSummary() {
        return getValue() ? "T" : "F";
    }

    @Override
    public void modify(InputHelper input) {
        boolean v = input.getBoolean("Enter the answer (T/F): ");
        selectedChoices.clear();
        selectedChoices.add(v ? "True" : "False");
    }
}
