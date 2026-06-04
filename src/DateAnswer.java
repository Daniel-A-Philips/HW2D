import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

// Answer for a Valid Date question. Stores one or more dates in YYYY-MM-DD form.
public class DateAnswer extends Answer {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<String> dates;

    public DateAnswer(String questionPrompt, List<String> dates) {
        super(questionPrompt);
        this.dates = new ArrayList<>(dates);
    }

    @Override
    public void display() {
        System.out.println(questionPrompt);
        System.out.println("A date should be entered in the following format: YYYY-MM-DD");
        for (int i = 0; i < dates.size(); i++) {
            if (dates.size() > 1) System.out.println("  " + (char) ('A' + i) + ") " + dates.get(i));
            else System.out.println("Answer: " + dates.get(i));
        }
    }

    @Override
    public String getAnswerSummary() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dates.size(); i++) {
            sb.append(dates.get(i));
            if (i < dates.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    @Override
    public void modify(InputHelper input) {
        List<String> updated = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            updated.add(input.getDate("Date #" + (i + 1) + " (YYYY-MM-DD): "));
        }
        this.dates = updated;
    }

    public List<String> getDates() {
        return dates;
    }
}
