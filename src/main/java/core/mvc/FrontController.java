package core.mvc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
	
	private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
	private static final String DEFAULT_API_PREFIX = "api";
	
	private RequestMapping rm;

	@Override
	public void init() throws ServletException {
		rm = (RequestMapping)getServletContext().getAttribute(ServletContextLoader.DEFAULT_REQUEST_MAPPING);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String requestUri = req.getRequestURI();
		logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);
	
		Controller controller = rm.findController(urlExceptParameter(req.getRequestURI()));
		String viewName;
		try {
			logger.debug("controller : {}", controller);
			viewName = controller.execute(req, resp);
		} catch (Exception e) {
			throw new ServletException(e.getMessage());
		}
		movePage(req, resp, viewName);
	}

	void movePage(HttpServletRequest req, HttpServletResponse resp,
			String viewName) throws ServletException, IOException {
		if (viewName.startsWith(DEFAULT_API_PREFIX)) {
			sendApiResponse(req, resp);
		}
		
		if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
			resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
			return;
		}
		
		RequestDispatcher rd = req.getRequestDispatcher(viewName);
		rd.forward(req, resp);
	}
	
	String urlExceptParameter(String forwardUrl) {
		int index = forwardUrl.indexOf("?");
		if (index > 0) {
			return forwardUrl.substring(0, index);
		}
		
		return forwardUrl;
	}
	
	private String toJsonString(Object obj) {	
		if (obj == null) return null;
		Gson gson = new Gson();
		return gson.toJson(obj);
	}
	
	private void sendApiResponse(HttpServletRequest req, HttpServletResponse resp) {
		String jsonString = toJsonString(req.getAttribute("apiResponse"));
		PrintWriter out = null;

		resp.setContentType("application/json; charset=UTF-8");

		try {
			out = resp.getWriter();
			out.println(jsonString);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		out.close();			
		return;		
	}
}
