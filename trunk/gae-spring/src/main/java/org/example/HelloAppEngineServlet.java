package org.example;

import java.io.IOException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;

import com.itcommand.service.SwagItemServiceImpl;

public class HelloAppEngineServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(SwagItemServiceImpl.class);
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		log.debug("Hello Sam");
	}
}