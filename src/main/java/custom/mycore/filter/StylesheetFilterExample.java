package custom.mycore.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import custom.mycore.filter.xsltutil.BufferedHttpResponseWrapper;
import custom.mycore.filter.xsltutil.StylesheetCache;
import org.apache.log4j.Logger;

/**
 * Use xslt stylesheet with servlet filter
 */
public class StylesheetFilterExample implements Filter {

	private static Logger LOGGER = Logger.getLogger(StylesheetFilterExample.class);

	private FilterConfig filterConfig;
	private String xsltFileName;

	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;

		/*
		 * to do: set xslt path as init-param and get it from filterConfig
		 * 
		 * -> Modify MCRAutoDeploy for set init-param elements in
		 * web-fragment.xml
		 */
		String xsltPath = "/xslt/HelloWorld.xsl";

		// if (xsltPath == null) {
		// throw new UnavailableException(
		// "xsltpath is missing, please check init-param in web-fragment.xml");
		// }

		/*
		 * convert relative path
		 */
		this.xsltFileName = filterConfig.getServletContext().getRealPath(xsltPath);

		/*
		 * Is File existing?
		 */
		if (this.xsltFileName == null || !new File(this.xsltFileName).exists()) {

			LOGGER.error("Unable to find xlt Stylesheet: " + this.xsltFileName);
		}
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		if (!(res instanceof HttpServletResponse)) {

			LOGGER.error("This Filter is only compatible with HTTP");
			throw new ServletException("response is not an instance of HttpServletResponse");
		}

		BufferedHttpResponseWrapper responseWrapper = new BufferedHttpResponseWrapper((HttpServletResponse) res);
		chain.doFilter(req, responseWrapper);

		/*
		 * be sure to write output into respone wrapper outputstream
		 */
		byte[] origXML = responseWrapper.getBuffer();
		if (origXML == null || origXML.length == 0) {

			chain.doFilter(req, res);
			return;
		}
		
		ServletOutputStream output = responseWrapper.getOutputStream();

		try {

			/*
			 * xsl transformation process
			 */
			Transformer trans = StylesheetCache.newTransformer(this.xsltFileName);
			LOGGER.info("Start to transform stylsheet: " + this.xsltFileName);
			
			ByteArrayInputStream origXMLIn = new ByteArrayInputStream(origXML);
			Source xmlSource = new StreamSource(origXMLIn);

			ByteArrayOutputStream resultBuf = new ByteArrayOutputStream();
			
			trans.transform(xmlSource, new StreamResult(resultBuf));
			LOGGER.info("Transformation process complete.");
			
			res.setContentLength(resultBuf.size());
			//res.setContentType("text/html");
			res.getOutputStream().write(resultBuf.toByteArray());
			res.flushBuffer();
		} catch (TransformerException te) {

			LOGGER.error("Error in transformation process of xslt sheet");
			throw new ServletException(te);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}