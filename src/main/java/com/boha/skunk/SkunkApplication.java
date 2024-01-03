package com.boha.skunk;

import com.boha.skunk.util.E;
import com.google.firebase.database.annotations.NotNull;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Logger;

@SpringBootApplication
public class SkunkApplication implements ApplicationListener<ApplicationReadyEvent> {
	static final String mm = "\uD83E\uDD66\uD83E\uDD66\uD83E\uDD66 SkunkApplication \uD83E\uDD66";

	static final Logger logger = Logger.getLogger(SkunkApplication.class.getSimpleName());
	@Value("${educUrl}")
	private String educUrl;
	@Autowired
	private ServletContext servletContext;

	public static void main(String[] args) {
		logger.info(mm+" SkunkApplication starting ....");
		SpringApplication.run(SkunkApplication.class, args);
		logger.info(mm+" SkunkApplication started ok!");

	}

	@Override
	public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
		logger.info(mm+" SkunkApplication onApplicationEvent: timestamp: "
				+ event.getTimestamp());
		logger.info(mm+"servletContext path: \uD83D\uDD90\uD83C\uDFFD "+servletContext.getContextPath());
		showApis(event);
//		findAnnotatedClasses();
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			logger.info(E.PEAR + E.PEAR + E.PEAR + E.PEAR
					+ " Current IP address : " + ip.getHostAddress());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	public void findAnnotatedClasses() {

		ClassPathScanningCandidateComponentProvider scanner =
				new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Service.class));
		scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
//		scanner.addIncludeFilter(new AnnotationTypeFilter(ComponentScan.Filter.class));


		Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents("");

		for (org.springframework.beans.factory.config.BeanDefinition beanDefinition :
				beanDefinitions) {
			try {
				Class<?> annotatedClass = Class.forName(
						beanDefinition.getBeanClassName());
				logger.info(E.PEAR + E.PEAR + E.PEAR + E.PEAR
						+ " Service/Component/Filter Name : " + annotatedClass.getSimpleName());

			} catch (ClassNotFoundException e) {
				// Handle the exception if needed
			}
		}

	}


	@Override
	public boolean supportsAsyncExecution() {
		logger.info(mm+" SkunkApplication supportsAsyncExecution ....");

		return ApplicationListener.super.supportsAsyncExecution();
	}
	private void showApis(ApplicationReadyEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
		RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
				.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping
				.getHandlerMethods();

		logger.info(mm +
				" \uD83D\uDD35\uD83D\uDD35 Total Endpoints: " + map.size());

		List<String> pints = new ArrayList<>();
		for (HandlerMethod info : map.values()) {
			var pc = info.getMethod().getName();
			var pp = info.getMethod().getParameterCount();
			pints.add(pc + " - parameters: " + pp);
		}
		Collections.sort(pints);
		for (String pint : pints) {
			logger.info(mm + " \uD83D\uDD35\uD83D\uDD35 endPoint: " + pint);
		}
	}
	@Component
	public static class AppRunner implements ApplicationRunner {

		private final DataSource dataSource;

		public AppRunner(DataSource dataSource) {
			this.dataSource = dataSource;
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {
			logger.info(mm + "Database:  \uD83C\uDF4E "
					+ dataSource.getConnection().getMetaData().getURL() + "  \uD83C\uDF4E");
		}
	}
}
