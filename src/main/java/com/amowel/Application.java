package com.amowel;


import com.amowel.configuration.shell.Bootstrap;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = new SpringApplicationBuilder()
				.sources(Application.class)
				.logStartupInfo(false)
				.bannerMode(Banner.Mode.CONSOLE)
				.run(args);
		new Bootstrap(args,context).run();
	}


}
