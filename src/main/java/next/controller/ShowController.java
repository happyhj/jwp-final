package next.controller;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.support.db.ConnectionManager;
import core.mvc.Controller;
import core.mvc.FrontController;

public class ShowController implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(ShowController.class);
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection conn = ConnectionManager.getConnection();

		QuestionDao questionDao = new QuestionDao(conn);
		AnswerDao answerDao = new AnswerDao();
		Question question;
		List<Answer> answers;

		long questionId = Long.parseLong(request.getParameter("questionId"));

		question = questionDao.findById(questionId);
		answers = answerDao.findAllByQuestionId(questionId);
		
		request.setAttribute("question", question);
		request.setAttribute("answers", answers);
		return "show.jsp";
	}
}
