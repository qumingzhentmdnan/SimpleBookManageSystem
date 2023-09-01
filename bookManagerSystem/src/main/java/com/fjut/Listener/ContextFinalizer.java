package com.fjut.Listener;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

@WebListener
public class ContextFinalizer implements ServletContextListener{

    public void contextInitialized(ServletContextEvent sce) {}

    public void contextDestroyed(ServletContextEvent sce) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        while (drivers.hasMoreElements()) {
            try {
                d = drivers.nextElement();
                DriverManager.deregisterDriver(d);
            } catch (SQLException ex) {
            }
        }
            // 注意：mysql8版本的jar好像shutdown方法私有了，只能调用checkedShutdown或uncheckedShutdown
            AbandonedConnectionCleanupThread.checkedShutdown();
    }
}