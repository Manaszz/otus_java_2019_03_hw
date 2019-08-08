package ru.otus.kchu.webservice.servlets;

import ru.otus.kchu.PageGenerator;
import ru.otus.kchu.dbservice.DBService;

import javax.crypto.spec.PSource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class AdmPage extends HttpServlet {
    private  DBService dbService;
    public AdmPage(DBService dbService) {
        this.dbService =dbService;
    }
    private Map paramMap =new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String userName = request.getUserPrincipal().getName();

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        runPage( response,userName,0,"");
//        String resultAsString = getBodyString(userName,0,"");
//        PrintWriter printWriter = response.getWriter();
//        printWriter.print(resultAsString);
//        printWriter.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String userName = request.getUserPrincipal().getName();
        runPage( response,userName,response.getStatus(),(String)request.getAttribute("user"));
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
//        String resultAsString = getBodyString(userName,response.getStatus(),(String)request.getAttribute("user") );
//        PrintWriter printWriter = response.getWriter();
//        runPage(request, response);
//        printWriter.print(resultAsString);
//        printWriter.flush();

    }
    private void runPage(HttpServletResponse response,String userName, int src,String usrNew ) throws  IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("userName", userName);
        pageVariables.put("message", ((src==0)?"":((src== HttpServletResponse.SC_OK)?"<B>User "+usrNew+" created! <B>":"Error. Check user data(role?)")));
        response.getWriter().println(PageGenerator.instance().getPage("admpage.html", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);


    }


    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        pageVariables.put("pathInfo",   Optional.ofNullable(request.getPathInfo() ).orElse(""));
        pageVariables.put("sessionId", request.getSession().getId());
        pageVariables.put("parameters", request.getParameterMap().toString());
        return pageVariables;
    }

    private String getBodyString(String userName, int src,String usrNew) {
        return "<!DOCTYPE html>" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                    "<head>" +
                    "    <meta charset=\"UTF-8\"/>" +
                    "    <title>Adminka</title>" +
                    "</head>" +
                    "<body>" +
                    "<p> Security info page: " + userName + " </p>"+
                    "<p>Create user:</p>" +
                    "<form action='/admdata/create' method=\"POST\">" +
                    "    Login: <input type=\"text\" name=\"login\"/> <BR> " +
                    "    Password: <input type=\"password\" name=\"pass\"/><BR>" +
                    "    Role: <input type=\"text\" name=\"role\"/><BR>" +
                    "    <input type=\"submit\" value=\"Create\"><BR>" +
                    "</form>" +
                    "<BR>" +((src==0)?"":((src== HttpServletResponse.SC_OK)?"<B>User "+usrNew+" created! <B>":"Error. Check user data(role?)"))+
                    "<Div><a href = /infoPage.html> Список пользователей </a></div>" +
                    "</body>" +
                    "</html>";
    }
}