package custom.mycore.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import custom.mycore.filter.util.GenericResponseWrapper;

public class SimpleFilterExample extends CustomGenericFilter {

	public void doFilter(final ServletRequest request, final ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		OutputStream out = response.getOutputStream();
		out.write(new String("<hr>Hello World from before<hr>").getBytes());
		GenericResponseWrapper wrapper = new GenericResponseWrapper((HttpServletResponse) response);
		chain.doFilter(request, wrapper);
		out.write(wrapper.getData());
		out.write(new String("<hr>Hello World from after<hr>").getBytes());
		out.close();
	}
}
