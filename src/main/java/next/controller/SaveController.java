package next.controller;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.dao.QuestionDao;
import next.model.Question;
import next.support.db.ConnectionManager;
import core.mvc.Controller;

public class SaveController implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(SaveController.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection conn = ConnectionManager.getConnection();

		Question question;
		QuestionDao questionDao = new QuestionDao(conn);
		
		// 쓰기요청 받은 글을 Question DTO 로 만듬
		String writer = request.getParameter("writer");
		String title = request.getParameter("title");
		String contents = request.getParameter("contents");
		
		question = new Question(writer, title, contents);
		// Question DAO 의 insert  메소드를 이용해서 DB에 글 추가
		questionDao.insert(question);
		// list.jsp로 redirect하기
		return "redirect:/list.next";
	}

}
