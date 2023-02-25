package com.bence.mate.spring.config;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	// Here we register all our static web pages.
	// It also possible to create controllers
	// classes for this with one method.
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/courses").setViewName("courses");
		registry.addViewController("/about").setViewName("about");
	}

	// Global CORS Configuration without @CrossOrigin
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}
