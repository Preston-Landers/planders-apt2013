package connexus;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
 
/**
 * May not need this - possibly remove
 *  Was for registering objectify stuff
 * @author Preston
 *
 */
public class ConnexusServletContextListener implements ServletContextListener{
 
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("ConnexusServletContextListener destroyed");
	}
 
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("ConnexusServletContextListener started");	
	}
}

