package ru.otus.kchu.webservice.servlets;

import ru.otus.kchu.dbservice.DBService;

import javax.crypto.spec.PSource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AdmPage extends HttpServlet {
    DBService dbService;
    public AdmPage(DBService dbService) {
        this.dbService =dbService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String userName = request.getUserPrincipal().getName();
        String resultAsString = getBodyString(userName,0,"");

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resultAsString);
        printWriter.flush();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String userName = request.getUserPrincipal().getName();
        String resultAsString = getBodyString(userName,response.getStatus(),(String)request.getAttribute("user") );

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resultAsString);
        printWriter.flush();
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
                    "<form action='/AdmData/create' method=\"POST\">" +
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