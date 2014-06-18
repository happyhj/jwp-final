package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.AnswerDao;
import next.model.Answer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.Controller;

public class AddanswerController implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(AddanswerController.class);
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Answer answer;
		AnswerDao answerDao = new AnswerDao();
		
		// 쓰기요청 받은 글을 Answer DTO 로 만듬
		String writer = request.getParameter("writer");
		String contents = request.getParameter("contents");
		long questionId = Long.parseLong(request.getParameter("questionId"));
		
		answer = new Answer(writer, contents, questionId);
		
		// Answer DAO 의 insert  메소드를 이용해서 DB에 답변 추가
		answerDao.insert(answer);

		return "api";
	}

}
