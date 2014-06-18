package next.controller;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.QuestionDao;
import next.model.Question;
import next.support.db.ConnectionManager;
import core.mvc.Controller;

public class ListController implements Controller {	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection conn = ConnectionManager.getConnection();

		QuestionDao questionDao = new QuestionDao(conn);
		List<Question> questions;
		
		questions = questionDao.findAll();
		
		if(request.getRequestURI().startsWith("/api")) {
			request.setAttribute("apiResponse", questions);			
			return "api"; 
		} else {
			request.setAttribute("questions", questions);
			return "list.jsp";
		}
	}
}
