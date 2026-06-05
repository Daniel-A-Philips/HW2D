import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// The result of grading a Test. Every test is worth 100 points, divided equally
// among all questions. Essay questions are removed from the gradable points.
public class GradeResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String testName;
    private int correctCount;
    private int gradedCount;
    private int totalQuestions;
    private final List<String> ungradedPrompts;
    private double percentScore;

    public GradeResult(String testName) {
        this.testName = testName;
        this.ungradedPrompts = new ArrayList<>();
    }

    public void incCorrect() { correctCount++; }
    public void incGraded() { gradedCount++; }
    public void addUngraded(String prompt) { ungradedPrompts.add(prompt); }
    public void setTotalQuestions(int n) { this.totalQuestions = n; }

    public void finalizeScore() {
        double pointsPer = (totalQuestions == 0) ? 0 : 100.0 / totalQuestions;
        this.percentScore = correctCount * pointsPer;
    }

    public void display() {
        double pointsPer = (totalQuestions == 0) ? 0 : 100.0 / totalQuestions;
        double autoGradable = gradedCount * pointsPer;
        System.out.print("You received " + article(percentScore) + " " + fmt(percentScore) + " on the test. ");
        System.out.print("The test was worth 100 points, but only " + fmt(autoGradable)
                + " of those points could be auto-graded");
        if (ungradedPrompts.isEmpty()) {
            System.out.println(".");
        } else {
            int n = ungradedPrompts.size();
            System.out.println(" because there " + (n == 1 ? "was one essay question" : "were " + n + " essay questions") + ".");
        }
    }

    // Format a score: whole numbers print without a decimal, otherwise one decimal.
    private String fmt(double d) {
        if (Math.abs(d - Math.round(d)) < 1e-9) return String.valueOf(Math.round(d));
        return String.format("%.1f", d);
    }

    // Choose "a"/"an" based on how the number is read aloud (e.g. "an 80", "a 90").
    // Because why not
    private String article(double d) {
        String s = fmt(d);
        char c = s.charAt(0);
        if (c == '8' || s.startsWith("11") || s.startsWith("18")) return "an";
        return "a";
    }

    public double getPercentScore() { return percentScore; }
    public List<String> getUngradedPrompts() { return ungradedPrompts; }
    public String getTestName() { return testName; }
}
