package com.boha.skunk;

import com.boha.skunk.util.DirectoryUtils;
import com.boha.skunk.util.E;
import com.google.firebase.database.annotations.NotNull;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@SpringBootApplication
public class SkunkApplication implements ApplicationListener<ApplicationReadyEvent> {
	static final String mm = "\uD83E\uDD66\uD83E\uDD66\uD83E\uDD66 SkunkApplication \uD83E\uDD66";

	static final Logger logger = Logger.getLogger(SkunkApplication.class.getSimpleName());
	@Value("${educUrl}")
	private String educUrl;
	@Value("${spring.profiles.active}")
	private String activeProfile;

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
			DirectoryUtils.deleteFilesInDirectories();
			if (activeProfile.equalsIgnoreCase("dev")) {
				logger.info(E.PEAR + E.PEAR + E.PEAR + E.PEAR
						+ " Active Profile : Development");
			}
			if (activeProfile.equalsIgnoreCase("prod")) {
				logger.info(E.PEAR + E.PEAR + E.PEAR + E.PEAR
						+ " Active Profile : Production");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	@Autowired
	private Environment environment;

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

}
