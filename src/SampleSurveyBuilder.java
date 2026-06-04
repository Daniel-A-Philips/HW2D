import java.util.Arrays;

// Utility that builds a sample Survey containing at least one of every
// supported question type and saves it to disk. Run it once to generate a
// ready-made survey for demonstration and tabulation.
public class SampleSurveyBuilder {

    public static Survey build() {
        Survey survey = new Survey("Sample Course Feedback Survey");

        // True/False
        survey.addQuestion(new TrueFalseQuestion(
                "The course met on time for every scheduled session."));

        // Multiple Choice
        survey.addQuestion(new MultipleChoiceQuestion(
                "Which part of the course did you enjoy the most?",
                Arrays.asList("Lectures", "Labs", "Group projects", "Office hours"),
                1));

        // Short Answer
        survey.addQuestion(new ShortAnswerQuestion(
                "In a few words, describe the pace of the course.", 60, 1));

        // Essay
        survey.addQuestion(new EssayQuestion(
                "What suggestions do you have for improving this course?", 1));

        // Date
        survey.addQuestion(new ValidDateQuestion(
                "On what date did you complete the final project?", 1));

        // Matching
        survey.addQuestion(new MatchingQuestion(
                "Match each topic to the week it was covered.",
                Arrays.asList("Inheritance", "Polymorphism", "Serialization"),
                Arrays.asList("Week 2", "Week 3", "Week 5")));

        return survey;
    }

    public static void main(String[] args) {
        Survey survey = build();
        survey.save(survey.getName());
        System.out.println("Sample survey \"" + survey.getName() + "\" created and saved.");
    }
}
