package custom.mycore;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.mycore.common.events.MCRStartupHandler.AutoExecutable;

/**
 *
 * Simple Startup Handler example for extension reason
 * 
 * To integrate this StartupHandler into startup process, use package +
 * classname for MCR.Startup.Class property in mycore.properties (MyCoRe home
 * directory)
 * 
 * MCR.Startup.Class=custom.mycore.CustomStartupHandler,...
 *
 */
public class CustomStartupHandler implements AutoExecutable {

	private static final String HANDLER_NAME = CustomStartupHandler.class.getName();

	private static final Logger LOGGER = LogManager.getLogger(CustomStartupHandler.class);

	@Override
	public String getName() {
		return HANDLER_NAME;
	}

	@Override
	public int getPriority() {

		return 0;
	}

	@Override
	public void startUp(ServletContext servletContext) {

		if (servletContext != null) {

			LOGGER.info("Startup custom");
		}
	}
}
