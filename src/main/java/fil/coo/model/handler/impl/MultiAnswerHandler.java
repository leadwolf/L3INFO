package fil.coo.model.handler.impl;

import fil.coo.model.handler.AnswerHandler;
import fil.coo.model.impl.Answer;
import fil.coo.exception.InvalidAnswerException;
import fil.coo.model.impl.answers.MultiAnswer;

public class MultiAnswerHandler extends AnswerHandler {

    @Override
    protected Answer createSpecificAnswer(String answerText) throws InvalidAnswerException {
        return new MultiAnswer(answerText);
    }
}