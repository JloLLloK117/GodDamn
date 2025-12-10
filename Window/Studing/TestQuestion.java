
package Window.Studing;

import java.util.List;

public class TestQuestion {
    public final String question;
    public final List<String> answers;
    public final int correctAnswer;

    public TestQuestion(String question, List<String> answers, int correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }
}
