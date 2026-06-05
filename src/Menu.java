import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Console menu controller. Drives the whole application: it lets the user
// build, display, save, load, take, modify, tabulate (and, for tests, grade)
// Surveys and Tests. Survey and Test share most handlers via an isTest flag.
public class Menu {

    private Survey currentSurvey;   // the survey currently in memory (if any)
    private Test currentTest;       // the test currently in memory (if any)
    private final FileManager fileManager;
    private final InputHelper input;

    public Menu() {
        this.fileManager = new FileManager();
        this.input = new InputHelper();
    }

    // ----------------------------------------------------------------- run loop
    public void run() {
        System.out.println("Welcome to the Survey & Test System");
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("Main Menu");
            System.out.println("1) Survey");
            System.out.println("2) Test");
            System.out.println("3) Quit");
            int choice = input.getInt("Select an option: ", 1, 3);
            switch (choice) {
                case 1: surveyMenu(); break;
                case 2: testMenu(); break;
                case 3: running = false; break;
            }
        }
        System.out.println("Goodbye!");
        input.close();
    }

    // ----------------------------------------------------------------- survey menu
    private void surveyMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("Survey Menu");
            System.out.println("1) Create a new Survey");
            System.out.println("2) Display an existing Survey");
            System.out.println("3) Load an existing Survey");
            System.out.println("4) Save the current Survey");
            System.out.println("5) Take the current Survey");
            System.out.println("6) Modify the current Survey");
            System.out.println("7) Tabulate a Survey");
            System.out.println("8) Return to previous menu");
            int choice = input.getInt("Select an option: ", 1, 8);
            switch (choice) {
                case 1: handleCreate(false); break;
                case 2: handleDisplay(false, false); break;
                case 3: handleLoad(false); break;
                case 4: handleSave(false); break;
                case 5: handleTake(false); break;
                case 6: handleModify(false); break;
                case 7: handleTabulate(false); break;
                case 8: back = true; break;
            }
        }
    }

    // ----------------------------------------------------------------- test menu
    private void testMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("Test Menu");
            System.out.println("1) Create a new Test");
            System.out.println("2) Display an existing Test without correct answers");
            System.out.println("3) Display an existing Test with correct answers");
            System.out.println("4) Load an existing Test");
            System.out.println("5) Save the current Test");
            System.out.println("6) Take the current Test");
            System.out.println("7) Modify the current Test");
            System.out.println("8) Tabulate a Test");
            System.out.println("9) Grade a Test");
            System.out.println("10) Return to previous menu");
            int choice = input.getInt("Select an option: ", 1, 10);
            switch (choice) {
                case 1: handleCreate(true); break;
                case 2: handleDisplay(true, false); break;
                case 3: handleDisplay(true, true); break;
                case 4: handleLoad(true); break;
                case 5: handleSave(true); break;
                case 6: handleTake(true); break;
                case 7: handleModify(true); break;
                case 8: handleTabulate(true); break;
                case 9: handleGrade(); break;
                case 10: back = true; break;
            }
        }
    }

    // ----------------------------------------------------------------- create
    private void handleCreate(boolean isTest) {
        String name = input.getString("Enter a name for the new " + (isTest ? "test" : "survey") + ": ");
        Survey target;
        if (isTest) {
            Test t = new Test(name);
            currentTest = t;
            target = t;
        } else {
            Survey s = new Survey(name);
            currentSurvey = s;
            target = s;
        }

        boolean adding = true;
        while (adding) {
            System.out.println();
            System.out.println("Add a Question");
            System.out.println("1) True/False");
            System.out.println("2) Multiple Choice");
            System.out.println("3) Short Answer");
            System.out.println("4) Essay");
            System.out.println("5) Date");
            System.out.println("6) Matching");
            System.out.println("7) Done adding questions");
            int choice = input.getInt("Select a question type: ", 1, 7);
            Question q = null;
            switch (choice) {
                case 1: q = buildTrueFalse(); break;
                case 2: q = buildMultipleChoice(); break;
                case 3: q = buildShortAnswer(); break;
                case 4: q = buildEssay(); break;
                case 5: q = buildDate(); break;
                case 6: q = buildMatching(); break;
                case 7: adding = false; break;
            }
            if (q != null) {
                target.addQuestion(q);
                if (isTest) captureCorrectAnswer((Test) target, q);
                System.out.println("Question added.");
            }
        }
        System.out.println("Finished building " + (isTest ? "test" : "survey") + " \"" + name + "\".");
    }

    // For a test, record the correct answer for the question just added. Pure
    // essay questions (an EssayQuestion that is not a ShortAnswerQuestion) are
    // not auto-gradable, so no key is stored for them.
    private void captureCorrectAnswer(Test test, Question q) {
        int index = test.getQuestions().size() - 1;
        if (q instanceof EssayQuestion && !(q instanceof ShortAnswerQuestion)) {
            System.out.println("(Essay questions are not auto-graded, so no correct answer is stored.)");
            test.setCorrectAnswer(index, null);
            return;
        }
        System.out.println("Now enter the correct answer for this question:");
        Answer correct = q.take(input);
        test.setCorrectAnswer(index, correct);
    }

    // ----------------------------------------------------------------- display
    private void handleDisplay(boolean isTest, boolean showAnswers) {
        if (isTest) {
            if (currentTest == null) {
                System.out.println("You must have a test loaded first. Use Create or Load.");
                return;
            }
            currentTest.display(showAnswers);
        } else {
            if (currentSurvey == null) {
                System.out.println("You must have a survey loaded first. Use Create or Load.");
                return;
            }
            currentSurvey.display();
        }
    }

    // ----------------------------------------------------------------- load
    private void handleLoad(boolean isTest) {
        List<String> files = isTest ? fileManager.listTests() : fileManager.listSurveys();
        if (files.isEmpty()) {
            System.out.println("There are no saved " + (isTest ? "tests" : "surveys") + " to load.");
            return;
        }
        String chosen = pickFromList(files, "Select a " + (isTest ? "test" : "survey") + " to load:");
        if (chosen == null) return;
        if (isTest) {
            Test t = Test.loadTest(chosen);
            if (t != null) { currentTest = t; System.out.println("Loaded test \"" + t.getName() + "\"."); }
        } else {
            Survey s = Survey.load(chosen);
            if (s != null) { currentSurvey = s; System.out.println("Loaded survey \"" + s.getName() + "\"."); }
        }
    }

    // ----------------------------------------------------------------- save
    private void handleSave(boolean isTest) {
        if (isTest) {
            if (currentTest == null) {
                System.out.println("You must have a test loaded first. Use Create or Load.");
                return;
            }
            currentTest.save(currentTest.getName());
            System.out.println("Saved test \"" + currentTest.getName() + "\".");
        } else {
            if (currentSurvey == null) {
                System.out.println("You must have a survey loaded first. Use Create or Load.");
                return;
            }
            currentSurvey.save(currentSurvey.getName());
            System.out.println("Saved survey \"" + currentSurvey.getName() + "\".");
        }
    }

    // ----------------------------------------------------------------- take
    private void handleTake(boolean isTest) {
        Survey target = isTest ? currentTest : currentSurvey;
        if (target == null) {
            System.out.println("You must have a " + (isTest ? "test" : "survey") + " loaded first. Use Create or Load.");
            return;
        }
        if (target.getQuestions().isEmpty()) {
            System.out.println("This " + (isTest ? "test" : "survey") + " has no questions yet.");
            return;
        }
        Response response = target.take(input);
        // Persist the response so it can be tabulated / graded later (required).
        String responseFile = target.getName() + "_response_" + System.currentTimeMillis();
        response.save(responseFile);
        System.out.println("Your response has been recorded and saved.");
    }

    // ----------------------------------------------------------------- modify
    private void handleModify(boolean isTest) {
        Survey target = isTest ? currentTest : currentSurvey;
        if (target == null) {
            System.out.println("You must have a " + (isTest ? "test" : "survey") + " loaded first. Use Create or Load.");
            return;
        }
        List<Question> qs = target.getQuestions();
        if (qs.isEmpty()) {
            System.out.println("This " + (isTest ? "test" : "survey") + " has no questions to modify.");
            return;
        }
        System.out.println("Questions:");
        for (int i = 0; i < qs.size(); i++) {
            System.out.println((i + 1) + ") " + qs.get(i).getPrompt());
        }
        int index = input.getInt("Which question do you want to modify? (1-" + qs.size() + "): ", 1, qs.size()) - 1;
        target.modifyQuestion(index, input);
        System.out.println("Question updated.");

        if (isTest) {
            Test test = (Test) target;
            Question q = qs.get(index);
            boolean pureEssay = (q instanceof EssayQuestion) && !(q instanceof ShortAnswerQuestion);
            if (!pureEssay && input.getBoolean("Do you also want to update the correct answer for this question? (Y/N): ")) {
                System.out.println("Enter the new correct answer:");
                test.setCorrectAnswer(index, q.take(input));
                System.out.println("Correct answer updated.");
            }
        }
    }

    // ----------------------------------------------------------------- tabulate
    private void handleTabulate(boolean isTest) {
        List<String> files = isTest ? fileManager.listTests() : fileManager.listSurveys();
        if (files.isEmpty()) {
            System.out.println("There are no saved " + (isTest ? "tests" : "surveys") + " to tabulate.");
            return;
        }
        String chosen = pickFromList(files, "Select a " + (isTest ? "test" : "survey") + " to tabulate:");
        if (chosen == null) return;

        Tabulator tabulator = new Tabulator();
        if (isTest) {
            Test t = Test.loadTest(chosen);
            if (t == null) return;
            List<Response> responses = loadResponsesFor(t.getName());
            if (responses.isEmpty()) { System.out.println("No responses have been recorded for this test yet."); return; }
            tabulator.tabulateTest(t, responses);
        } else {
            Survey s = Survey.load(chosen);
            if (s == null) return;
            List<Response> responses = loadResponsesFor(s.getName());
            if (responses.isEmpty()) { System.out.println("No responses have been recorded for this survey yet."); return; }
            tabulator.tabulateSurvey(s, responses);
        }
    }

    // ----------------------------------------------------------------- grade
    private void handleGrade() {
        List<String> tests = fileManager.listTests();
        if (tests.isEmpty()) {
            System.out.println("There are no saved tests to grade.");
            return;
        }
        String chosenTest = pickFromList(tests, "Select a test to grade:");
        if (chosenTest == null) return;
        Test test = Test.loadTest(chosenTest);
        if (test == null) return;

        List<String> responseFiles = responseFileNamesFor(test.getName());
        if (responseFiles.isEmpty()) {
            System.out.println("No responses have been recorded for this test yet.");
            return;
        }
        String chosenResponse = pickFromList(responseFiles, "Select a response to grade:");
        if (chosenResponse == null) return;
        Response response = Response.load(chosenResponse);
        if (response == null) return;

        GradeResult result = new Grader().grade(test, response);
        System.out.println();
        result.display();
    }

    // ----------------------------------------------------------------- helpers
    // Load every saved Response whose survey/test name matches the given name.
    private List<Response> loadResponsesFor(String name) {
        List<Response> matches = new ArrayList<>();
        for (String fileName : fileManager.listResponses()) {
            Response r = Response.load(fileName);
            if (r != null && name.equals(r.getSurveyName())) matches.add(r);
        }
        return matches;
    }

    // Return the response file names (not the objects) that belong to a survey/test.
    private List<String> responseFileNamesFor(String name) {
        List<String> matches = new ArrayList<>();
        for (String fileName : fileManager.listResponses()) {
            Response r = Response.load(fileName);
            if (r != null && name.equals(r.getSurveyName())) matches.add(fileName);
        }
        return matches;
    }

    // Print a numbered list and return the chosen entry (or null if cancelled).
    private String pickFromList(List<String> items, String header) {
        System.out.println(header);
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ") " + items.get(i));
        }
        System.out.println("0) Cancel");
        int choice = input.getInt("Select an option: ", 0, items.size());
        if (choice == 0) return null;
        return items.get(choice - 1);
    }

    // ----------------------------------------------------------------- builders
    private Question buildTrueFalse() {
        String prompt = input.getString("Enter the True/False question prompt: ");
        return new TrueFalseQuestion(prompt);
    }

    private Question buildMultipleChoice() {
        String prompt = input.getString("Enter the multiple choice question prompt: ");
        int count = input.getInt("How many choices? (2-10): ", 2, 10);
        List<String> choices = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            choices.add(input.getString("Choice " + (char) ('A' + i) + ": "));
        }
        int numResponses = input.getInt("How many choices should the responder select? (1-" + count + "): ", 1, count);
        return new MultipleChoiceQuestion(prompt, choices, numResponses);
    }

    private Question buildShortAnswer() {
        String prompt = input.getString("Enter the short answer question prompt: ");
        int charLimit = input.getInt("Character limit? (1-1000): ", 1, 1000);
        int numResponses = input.getInt("How many responses are required? (1-10): ", 1, 10);
        return new ShortAnswerQuestion(prompt, charLimit, numResponses);
    }

    private Question buildEssay() {
        String prompt = input.getString("Enter the essay question prompt: ");
        int numResponses = input.getInt("How many responses (paragraphs) are required? (1-10): ", 1, 10);
        return new EssayQuestion(prompt, numResponses);
    }

    private Question buildDate() {
        String prompt = input.getString("Enter the date question prompt: ");
        int numResponses = input.getInt("How many dates are required? (1-10): ", 1, 10);
        return new ValidDateQuestion(prompt, numResponses);
    }

    private Question buildMatching() {
        String prompt = input.getString("Enter the matching question prompt: ");
        int count = input.getInt("How many items to match? (2-10): ", 2, 10);
        List<String> left = new ArrayList<>();
        List<String> right = new ArrayList<>();
        System.out.println("Enter the left column items (lettered A, B, C ...):");
        for (int i = 0; i < count; i++) {
            left.add(input.getString("Left " + (char) ('A' + i) + ": "));
        }
        System.out.println("Enter the right column items (numbered 1, 2, 3 ...):");
        for (int i = 0; i < count; i++) {
            right.add(input.getString("Right " + (i + 1) + ": "));
        }
        return new MatchingQuestion(prompt, left, right);
    }
}
