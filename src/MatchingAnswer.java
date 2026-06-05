import java.io.Serial;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Answer for a Matching question. Stores the columns presented and the
// left-to-right pairings the user chose.
public class MatchingAnswer extends Answer {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<String> leftColumn;
    private final List<String> rightColumn;
    private Map<String, String> pairs;

    public MatchingAnswer(String questionPrompt, List<String> leftColumn,
                          List<String> rightColumn, Map<String, String> pairs) {
        super(questionPrompt);
        this.leftColumn = new ArrayList<>(leftColumn);
        this.rightColumn = new ArrayList<>(rightColumn);
        this.pairs = new LinkedHashMap<>(pairs);
    }

    @Override
    public void display() {
        System.out.println(questionPrompt);
        for (int i = 0; i < leftColumn.size(); i++) {
            String left = (char) ('A' + i) + ") " + leftColumn.get(i);
            String right = (i < rightColumn.size()) ? (i + 1) + ") " + rightColumn.get(i) : "";
            System.out.println(left + "\t\t" + right);
        }
        System.out.println("Matches: " + getAnswerSummary());
    }

    @Override
    public String getAnswerSummary() {
        StringBuilder sb = new StringBuilder();
        int n = 0;
        for (Map.Entry<String, String> e : pairs.entrySet()) {
            int leftIdx = leftColumn.indexOf(e.getKey());
            int rightIdx = rightColumn.indexOf(e.getValue());
            String letter = (leftIdx >= 0) ? String.valueOf((char) ('A' + leftIdx)) : "?";
            String number = (rightIdx >= 0) ? String.valueOf(rightIdx + 1) : "?";
            sb.append(letter).append(" -> ").append(number);
            if (++n < pairs.size()) sb.append(", ");
        }
        return sb.toString();
    }

    @Override
    public void modify(InputHelper input) {
        Map<String, String> updated = new LinkedHashMap<>();
        for (int i = 0; i < leftColumn.size(); i++) {
            char letter = (char) ('A' + i);
            while (true) {
                String line = input.getString("Match for " + letter + " (e.g. " + letter + " 1): ").trim();
                String[] parts = line.split("\\s+");
                if (parts.length < 2) { System.out.println("Please enter a letter and a number."); continue; }
                if (parts[0].length() != 1) { System.out.println("First token should be a single letter."); continue; }
                int li = parts[0].toUpperCase().charAt(0) - 'A';
                if (li < 0 || li >= leftColumn.size()) { System.out.println("Letter out of range."); continue; }
                int ri;
                try { ri = Integer.parseInt(parts[1]) - 1; }
                catch (NumberFormatException e) { System.out.println("Second token should be a number."); continue; }
                if (ri < 0 || ri >= rightColumn.size()) { System.out.println("Number out of range."); continue; }
                updated.put(leftColumn.get(li), rightColumn.get(ri));
                break;
            }
        }
        this.pairs = updated;
    }

    public Map<String, String> getPairs() {
        return pairs;
    }

    public List<String> getLeftColumn() {
        return leftColumn;
    }

    public List<String> getRightColumn() {
        return rightColumn;
    }
}
