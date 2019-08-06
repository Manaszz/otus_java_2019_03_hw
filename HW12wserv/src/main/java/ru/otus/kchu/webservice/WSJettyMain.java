package ru.otus.kchu.webservice;

import org.eclipse.jetty.security.*;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import ru.otus.kchu.dao.User;
import ru.otus.kchu.dbservice.DBService;
import ru.otus.kchu.webservice.filters.SimpleFilter;
import ru.otus.kchu.webservice.servlets.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class WSJettyMain {
    private final static int PORT = 8080;
    private DBService dbService;

public WSJettyMain(DBService dbService) throws Exception {
        this.dbService = dbService;
    }

    public void start() throws Exception {
        Server server = createServer(PORT);
        server.start();
        server.join();
    }

    public Server createServer(int port) throws MalformedURLException {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.addServlet(new ServletHolder(new PublicInfo(dbServ)), "/publicInfo");
//        context.addServlet(new ServletHolder(new PrivateInfo(dbServ)), "/privateInfo");
        context.addServlet(new ServletHolder(new AdmPage(dbService)), "/adm");
        context.addServlet(new ServletHolder(new AdmData(dbService)), "/admdata/*");

        context.addFilter(new FilterHolder(new SimpleFilter()), "/*", null);

        Server server = new Server(port);
        server.setHandler(new HandlerList(context));

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{createResourceHandler(), createSecurityHandler(context)});
        server.setHandler(handlers);
        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});

        URL fileDir = WSJettyMain.class.getClassLoader().getResource("static");
        if (fileDir == null) {
            throw new RuntimeException("File Directory not found");
        }
        resourceHandler.setResourceBase(fileDir.getPath());
        return resourceHandler;
    }

    private SecurityHandler createSecurityHandler(ServletContextHandler context)  {
        List<ConstraintMapping> mappingList = new ArrayList<>();
        mappingList.add( getConstraintMapping("auth", true, new String[]{"user", "admin"},"/*"));
        mappingList.add( getConstraintMapping("adm", true, new String[]{"admin"},"/adm/*"));

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        //как декодировать стороку с юзером:паролем https://www.base64decode.org/
        security.setAuthenticator(new BasicAuthenticator());

        URL propFile = null;
        try {
            propFile = getPropFile();

            HashLoginService lServ = new HashLoginService("MyRealm", propFile.getPath());
            lServ.setUserStore( getDBUsersStore());
    //        security.setLoginService(new HashLoginService("MyRealm", propFile.getPath()));
            security.setLoginService(lServ);
            security.setHandler(new HandlerList(context));
            security.setConstraintMappings(mappingList);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return security;
    }

    private ConstraintMapping getConstraintMapping(String name,boolean auth,String[] roles, String path) {
        Constraint constraint = new Constraint();
        constraint.setName(name);
        constraint.setAuthenticate(auth);
        constraint.setRoles(roles);
        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec(path);
        mapping.setConstraint(constraint);
        return mapping;
    }

    private URL getPropFile() throws MalformedURLException {
        URL propFile = null;
        File realmFile = new File("./realm.properties");
        if (realmFile.exists()) {
            propFile = realmFile.toURI().toURL();
        }
        if (propFile == null) {
            System.out.println("local realm config not found, looking into Resources");
            propFile = WSJettyMain.class.getClassLoader().getResource("realm.properties");
        }

        if (propFile == null) {
            throw new RuntimeException("Realm property file not found");
        }
        return propFile;
    }

    private UserStore getDBUsersStore() {
        UserStore usrs = new UserStore();
        try {
            dbService.create(new User("admin","pass", "admin"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (User user : dbService.GetEntities(User.class)) {
                    usrs.addUser(user.getName(), Credential.getCredential(user.getPassword()), new String[] { user.getRole() });
        }
//        usrs.addUser("admin", Credential.getCredential("pass"), new String[] { "admin" });
        return usrs;
    }
}
