package ru.otus;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.domain.server.UsersWebServer;
import ru.otus.domain.service.ClientService;
import ru.otus.domain.service.TemplateProcessor;
import ru.otus.domain.service.UserAuthService;
import ru.otus.domain.servlet.AuthorizationFilter;
import ru.otus.domain.servlet.LoginServlet;
import ru.otus.domain.servlet.UsersApiServlet;
import ru.otus.domain.servlet.UsersServlet;
import ru.otus.util.FileSystemHelper;

import java.util.Arrays;


public final class WebServerWithFilterBasedSecurity implements UsersWebServer {

    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final UserAuthService authService;
    private final ClientService clientService;
    private final Gson gson;
    private final TemplateProcessor templateProcessor;
    private Server server;

    public WebServerWithFilterBasedSecurity(UserAuthService authService, ClientService clientService, Gson gson,
                                            TemplateProcessor templateProcessor) {
        this.authService = authService;
        this.clientService = clientService;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
    }

    @Override
    public void start() throws Exception {
        if (server == null) {
            server = new Server(8080);
        }
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {
        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/users", "/api/user"));

        server.setHandler(handlers);
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new UsersServlet(clientService, templateProcessor)), "/users");
        servletContextHandler.addServlet(new ServletHolder(new UsersApiServlet(clientService, gson)), "/api/user");
        return servletContextHandler;
    }
}
