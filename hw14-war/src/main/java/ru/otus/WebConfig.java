package ru.otus;

import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import ru.otus.data.HibernateUtils;
import ru.otus.domain.model.Client;

@Configuration
@ComponentScan
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private static final String DB_MIGRATION_SCRIPT_DIR = "/db/migration";
    private static final String STATIC_DIR = "/WEB-INF/static/";
    private static final String TEMPLATES_DIR = "/WEB-INF/templates/";
    private static final String NO_HANDLER_VIEW_NAME = "noHandlerView";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private final ApplicationContext applicationContext;

    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/no-handler-view").setViewName(NO_HANDLER_VIEW_NAME);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations(STATIC_DIR);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix(TEMPLATES_DIR);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(true);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    @Bean
    public org.hibernate.cfg.Configuration hibernateConfig() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure(HIBERNATE_CFG_FILE);
        flywayMigrations(configuration);
        return configuration;
    }

    @Bean
    public SessionFactory hibernateSessionFactory() {
        return HibernateUtils.buildSessionFactory(hibernateConfig(), Client.class);
    }

    private void flywayMigrations(org.hibernate.cfg.Configuration configuration) {
        var flyway = Flyway.configure()
                .dataSource(
                        configuration.getProperty("hibernate.connection.url"),
                        configuration.getProperty("hibernate.connection.username"),
                        configuration.getProperty("hibernate.connection.password")
                )
                .locations("classpath:".concat(DB_MIGRATION_SCRIPT_DIR))
                .load();
        flyway.migrate();
    }
}
