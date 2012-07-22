package clear4j.web;

import javax.servlet.ServletContextEvent;

import clear4j.Clear;

public class StartupListener implements javax.servlet.ServletContextListener  {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Clear.start();
	}
	
}
