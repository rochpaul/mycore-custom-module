package custom.mycore.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CustomJSFilter implements Filter {

	FilterConfig filterConfig = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		servletResponse.setContentType("text/html");

		ServletOutputStream out = servletResponse.getOutputStream();

		out.println("<script type='text/javascript'>");
		RequestDispatcher dispatcher = servletRequest.getRequestDispatcher("/js/custom/HelloWorld.js");

		dispatcher.include(servletRequest, servletResponse);
		out.println("</script>");

		// out.println("<script src=\"/mir/js/custom/HelloWorld.js\"
		// type='text/javascript'>");

		filterChain.doFilter(servletRequest, servletResponse);
	}
}
