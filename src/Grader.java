import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Computes a numeric grade for a Test given a Response. Essay questions are not
// auto-gradable and are excluded from the gradable points.
public class Grader {

    public GradeResult grade(Test t, Response r) {
        GradeResult result = new GradeResult(t.getName());
        List<Question> qs = t.getQuestions();
        List<Answer> given = r.getAnswers();
        result.setTotalQuestions(qs.size());

        for (int i = 0; i < qs.size(); i++) {
            Question q = qs.get(i);
            Answer correct = t.getAnswerKey().getAnswer(i);
            if (!isGradable(q, correct)) {
                result.addUngraded(q.getPrompt());   // e.g. an essay
                continue;
            }
            result.incGraded();
            Answer studentAns = (i < given.size()) ? given.get(i) : null;
            if (studentAns != null && isCorrect(correct, studentAns)) result.incCorrect();
        }
        result.finalizeScore();
        return result;
    }

    // A question is auto-gradable unless it is a pure essay (note: short answer
    // extends essay but IS gradable, so it is checked first).
    private boolean isGradable(Question q, Answer correct) {
        if (correct == null) return false;
        if (q instanceof ShortAnswerQuestion) return true;
        if (q instanceof EssayQuestion) return false;
        return true;
    }

    // Compare two answers of the same type for correctness.
    private boolean isCorrect(Answer correct, Answer given) {
        if (correct == null || given == null) return false;

        // TrueFalse and MultipleChoice (T/F is a MultipleChoiceAnswer): compare selected choices as a set.
        if (correct instanceof MultipleChoiceAnswer && given instanceof MultipleChoiceAnswer) {
            List<String> c = new ArrayList<>(((MultipleChoiceAnswer) correct).getSelectedChoices());
            List<String> g = new ArrayList<>(((MultipleChoiceAnswer) given).getSelectedChoices());
            Collections.sort(c); Collections.sort(g);
            return c.equals(g);
        }
        // Matching: the chosen pairings must match exactly.
        if (correct instanceof MatchingAnswer && given instanceof MatchingAnswer) {
            return ((MatchingAnswer) correct).getPairs().equals(((MatchingAnswer) given).getPairs());
        }
        // Date: compare the set of dates.
        if (correct instanceof DateAnswer && given instanceof DateAnswer) {
            List<String> c = new ArrayList<>(((DateAnswer) correct).getDates());
            List<String> g = new ArrayList<>(((DateAnswer) given).getDates());
            Collections.sort(c); Collections.sort(g);
            return c.equals(g);
        }
        // Short answer (essay subtype): compare normalized text responses.
        if (correct instanceof EssayAnswer && given instanceof EssayAnswer) {
            List<String> c = normalizeList(((EssayAnswer) correct).getResponses());
            List<String> g = normalizeList(((EssayAnswer) given).getResponses());
            return c.equals(g);
        }
        return normalize(correct.getAnswerSummary()).equals(normalize(given.getAnswerSummary()));
    }

    private List<String> normalizeList(List<String> in) {
        List<String> out = new ArrayList<>();
        for (String s : in) out.add(normalize(s));
        Collections.sort(out);
        return out;
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase().replaceAll("\\s+", " ");
    }
}
