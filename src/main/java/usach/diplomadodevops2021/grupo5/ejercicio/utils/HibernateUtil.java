package usach.diplomadodevops2021.grupo5.ejercicio.utils;

import java.io.InputStream;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.util.ResourceUtils;

@SuppressWarnings("deprecation")
public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();
	 
    private static SessionFactory buildSessionFactory() 
    {
        try {
        	InputStream is = ClassLoader.class.getResourceAsStream("classpath:hibernate.cfg.xml");

        	return new AnnotationConfiguration().configure(
        			ResourceUtils.getFile("hibernate.cfg.xml")).buildSessionFactory();
 
        } 
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
 
    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
