import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Tabulates a set of responses for a Survey or Test. Each question type is
// summarized differently per the assignment spec.
public class Tabulator {

    public void tabulateSurvey(Survey s, List<Response> responses) {
        System.out.println("=== Tabulation for survey: " + s.getName() + " ===");
        tabulateCommon(s.getQuestions(), responses);
    }

    public void tabulateTest(Test t, List<Response> responses) {
        System.out.println("=== Tabulation for test: " + t.getName() + " ===");
        tabulateCommon(t.getQuestions(), responses);
    }

    private void tabulateCommon(List<Question> qs, List<Response> responses) {
        if (responses.isEmpty()) { System.out.println("No responses to tabulate."); return; }
        for (int i = 0; i < qs.size(); i++) {
            Question q = qs.get(i);
            List<Answer> ans = new ArrayList<>();
            for (Response r : responses) {
                List<Answer> ra = r.getAnswers();
                if (i < ra.size()) ans.add(ra.get(i));
            }
            System.out.println();
            System.out.println((i + 1) + ") " + q.getPrompt());
            System.out.println(summarize(q, ans));
        }
    }

    // Order matters: check the more specific subclass first.
    private String summarize(Question q, List<Answer> ans) {
        if (q instanceof TrueFalseQuestion) return tallyChoices(q, ans);
        if (q instanceof MultipleChoiceQuestion) return tallyChoices(q, ans);
        if (q instanceof ShortAnswerQuestion) return tallyResponses(ans);
        if (q instanceof ValidDateQuestion) return tallyDates(ans);
        if (q instanceof EssayQuestion) return listEssays(ans);
        if (q instanceof MatchingQuestion) return tallyMatching((MatchingQuestion) q, ans);
        return "(no tabulation available)";
    }

    // True/False or Multiple Choice: count the responses for each choice.
    private String tallyChoices(Question q, List<Answer> ans) {
        MultipleChoiceQuestion mc = (MultipleChoiceQuestion) q;
        List<String> choices = mc.getChoices();
        int[] counts = new int[choices.size()];
        for (Answer a : ans) {
            if (a instanceof MultipleChoiceAnswer) {
                for (String sel : ((MultipleChoiceAnswer) a).getSelectedChoices()) {
                    int i = choices.indexOf(sel);
                    if (i >= 0) counts[i]++;
                }
            }
        }
        boolean tf = (q instanceof TrueFalseQuestion);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < choices.size(); i++) {
            String label = tf ? choices.get(i) : String.valueOf((char) ('A' + i));
            sb.append(label).append(": ").append(counts[i]);
            if (i < choices.size() - 1) sb.append("\n");
        }
        return sb.toString();
    }

    // Short Answer: count occurrences of each distinct response.
    private String tallyResponses(List<Answer> ans) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Answer a : ans)
            if (a instanceof EssayAnswer)
                for (String r : ((EssayAnswer) a).getResponses())
                    counts.merge(r, 1, Integer::sum);
        if (counts.isEmpty()) return "(no responses)";
        StringBuilder sb = new StringBuilder();
        int n = 0;
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            sb.append(e.getKey()).append("  ").append(e.getValue());
            if (++n < counts.size()) sb.append("\n");
        }
        return sb.toString();
    }

    // Date: count occurrences of each distinct date (date on one line, count next).
    private String tallyDates(List<Answer> ans) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Answer a : ans)
            if (a instanceof DateAnswer)
                for (String d : ((DateAnswer) a).getDates())
                    counts.merge(d, 1, Integer::sum);
        if (counts.isEmpty()) return "(no responses)";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            sb.append(e.getKey()).append("\n").append(e.getValue()).append("\n");
        }
        return sb.toString().trim();
    }

    // Essay: simply list all of the answers.
    private String listEssays(List<Answer> ans) {
        StringBuilder sb = new StringBuilder();
        for (Answer a : ans)
            if (a instanceof EssayAnswer)
                for (String r : ((EssayAnswer) a).getResponses())
                    sb.append(r).append("\n");
        return sb.length() == 0 ? "(no essays)" : sb.toString().trim();
    }

    // Matching: count each distinct permutation submitted, then show count + permutation.
    private String tallyMatching(MatchingQuestion q, List<Answer> ans) {
        List<String> left = q.getLeftColumn();
        List<String> right = q.getRightColumn();
        StringBuilder header = new StringBuilder();
        for (int i = 0; i < left.size(); i++) {
            header.append((char) ('A' + i)).append(") ").append(left.get(i));
            if (i < right.size()) header.append("  ").append(i + 1).append(") ").append(right.get(i));
            header.append("\n");
        }
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Answer a : ans) {
            if (a instanceof MatchingAnswer) {
                Map<String, String> pairs = ((MatchingAnswer) a).getPairs();
                StringBuilder perm = new StringBuilder();
                for (int i = 0; i < left.size(); i++) {
                    String r = pairs.get(left.get(i));
                    int rIdx = (r == null) ? -1 : right.indexOf(r);
                    perm.append((char) ('A' + i)).append(" ").append(rIdx >= 0 ? (rIdx + 1) : "?");
                    if (i < left.size() - 1) perm.append("\n");
                }
                counts.merge(perm.toString(), 1, Integer::sum);
            }
        }
        StringBuilder sb = new StringBuilder(header.toString());
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            sb.append(e.getValue()).append("\n").append(e.getKey()).append("\n");
        }
        return sb.toString().trim();
    }
}
