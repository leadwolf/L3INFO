package fil.coo.answers;

public class TextAnswer extends SingleAnswer {

    public TextAnswer(String answer) {
        super(answer);
    }

    public String getPrompt() {
        return "(text)";
    }

    protected boolean checkUserAnswerIsValid(String userAnswer) {
        return false;
    }

}
