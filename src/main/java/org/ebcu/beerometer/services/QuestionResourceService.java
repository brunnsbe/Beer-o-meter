package org.ebcu.beerometer.services;

import java.util.*;
import java.util.concurrent.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.ebcu.beerometer.domain.*;
import org.ebcu.beerometer.database.*;
import org.ebcu.beerometer.resource.*;
import org.ebcu.beerometer.util.*;
import org.sql2o.Connection;

public class QuestionResourceService implements QuestionResource {

    private static final String          SQL_LOAD_QUESTIONS = "SELECT "
                                                                    + "Election_Question.Id, "
                                                                    + "Election_Question.Subject, "
                                                                    + "Election_Question.OrderNumber "
                                                                    + "FROM "
                                                                    + "Election_Question;";

    private static Map<String, Question> questions          = new ConcurrentHashMap<String, Question>();

    static {
        loadQuestionsFromDB();
    }

    @Override
    public String reloadDB() {
        loadQuestionsFromDB();
        return new String();
    }

    @Override
    public Response getQuestions() {
        ArrayList<Question> result = new ArrayList<Question>(questions.values());

        Collections.sort(result);
        return ResponseUtil.addCacheControl(result);
    }

    @Override
    public Response getQuestion(String id) {
        Question question = questions.get(id);
        if (question != null) {
            return ResponseUtil.addCacheControl(question);
        } else {
            throw new NotFoundException();
        }
    }

    private static void loadQuestionsFromDB() {
        List<Question> questionSet;
        try (Connection con = DBConnection.getConnection().open()) {
            questionSet = con.createQuery(SQL_LOAD_QUESTIONS).executeAndFetch(Question.class);
            for (Question question : questionSet) {
                questions.put(question.getId(), question);
            }
        }
    }
}
