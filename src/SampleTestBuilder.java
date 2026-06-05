import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Utility that builds a sample Test containing at least one of every supported
// question type, fills in an answer key for the auto-gradable questions, and
// saves it to disk. The pure essay question is intentionally left ungraded.
public class SampleTestBuilder {

    public static Test build() {
        Test test = new Test("Sample Java Concepts Test");

        // 1) True/False
        TrueFalseQuestion tf = new TrueFalseQuestion(
                "In Java, a subclass inherits the public methods of its superclass.");
        test.addQuestion(tf);
        test.setCorrectAnswer(0, new TrueFalseAnswer(tf.getPrompt(), true));

        // 2) Multiple Choice
        MultipleChoiceQuestion mc = new MultipleChoiceQuestion(
                "Which keyword is used to inherit from a class in Java?",
                Arrays.asList("implements", "extends", "inherits", "super"),
                1);
        test.addQuestion(mc);
        test.setCorrectAnswer(1, new MultipleChoiceAnswer(
                mc.getPrompt(), mc.getChoices(), List.of("extends")));

        // 3) Short Answer
        ShortAnswerQuestion sa = new ShortAnswerQuestion(
                "What method must be overridden to print an object meaningfully?", 40, 1);
        test.addQuestion(sa);
        test.setCorrectAnswer(2, new ShortAnswerAnswer(
                sa.getPrompt(), List.of("toString")));

        // 4) Essay (not auto-graded)
        EssayQuestion essay = new EssayQuestion(
                "Explain the difference between an abstract class and an interface.", 1);
        test.addQuestion(essay);
        test.setCorrectAnswer(3, null);

        // 5) Date
        ValidDateQuestion date = new ValidDateQuestion(
                "On what date was the first version of Java publicly released?", 1);
        test.addQuestion(date);
        test.setCorrectAnswer(4, new DateAnswer(
                date.getPrompt(), List.of("1996-01-23")));

        // 6) Matching
        MatchingQuestion match = new MatchingQuestion(
                "Match each OOP concept to its short definition.",
                Arrays.asList("Encapsulation", "Inheritance", "Polymorphism"),
                Arrays.asList("Reusing a base class", "Hiding internal state", "One interface, many forms"));
        test.addQuestion(match);
        Map<String, String> pairs = new LinkedHashMap<>();
        pairs.put("Encapsulation", "Hiding internal state");
        pairs.put("Inheritance", "Reusing a base class");
        pairs.put("Polymorphism", "One interface, many forms");
        test.setCorrectAnswer(5, new MatchingAnswer(
                match.getPrompt(), match.getLeftColumn(), match.getRightColumn(), pairs));

        return test;
    }

    static void main(String[] args) {
        Test test = build();
        test.save(test.getName());
        System.out.println("Sample test \"" + test.getName() + "\" created and saved.");
    }
}
