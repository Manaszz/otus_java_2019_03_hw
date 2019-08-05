package ru.otus.kchu.webservice.servlets;

import com.google.gson.Gson;
import ru.otus.kchu.dao.User;
import ru.otus.kchu.dbservice.DBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AdmData extends HttpServlet {
    private static final Gson gson = new Gson();
    DBService dbService;

    public AdmData(DBService dbServ) {
        this.dbService=dbServ;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] requestParams = request.getPathInfo().split("/");
        String dataParam = "defaultValue";
        if (requestParams.length == 2) {
            dataParam = requestParams[1];
        }

        System.out.println("Params all:");
        for (String parm : requestParams) System.out.println(parm);

        System.out.println("requIest params:" + dataParam);
        switch (dataParam) {
            case "create":
                createUser(request, response);
                break;
            case "list":
                getUsers(request, response);
                break;
            default:
                String resultAsString = gson.toJson("from server:" + dataParam);
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter printWriter = response.getWriter();
                printWriter.print(resultAsString);
                printWriter.flush();
        }


    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String[] requestParams = request.getPathInfo().split("/");
        String dataParam = "defaultValue";
        if (requestParams.length == 2) {
            dataParam = requestParams[1];
        }
        switch (dataParam) {
            case "create":
                createUser(request, response);
                break;
            case "list":
                getUsers(request, response);
                break;
            default:
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST,"Unknown parametr");
            return;
        }

//        request.getRequestDispatcher("/infoPage.html").forward(request, response);

    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");
        String role = request.getParameter("role");

        if (login == null || pass == null || (!role.equals("user")&& !role.equals("admin"))) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST,"wrong user info");
            request.getRequestDispatcher("/adm").forward(request, response);
            return;
        }

        User newUsr = new User(login,pass, role);
        try {
            dbService.create(newUsr);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("new user id:" +newUsr.getId());
        response.setContentType("application/json");

        response.setStatus(HttpServletResponse.SC_OK);
        request.setAttribute("user", login);
        request.getRequestDispatcher("/adm").forward(request, response);
    }

    private void getUsers(HttpServletRequest request, HttpServletResponse response) throws  IOException {

        List<User> userList = dbService.GetEntities(User.class);
        String resultAsString = gson.toJson(userList);
        System.out.println(resultAsString);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resultAsString);
        printWriter.flush();
    }
}
