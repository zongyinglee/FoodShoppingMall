package com.main.controller;

import com.main.utils.SqlHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/active")
public class ActiveServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sql = "update user set state = 1 where code = ? ";
        String[] p={
                request.getParameter("activeCode")
        };
        SqlHelper.doUpdate(sql,p);
        response.sendRedirect(request.getContextPath()+"/home/view?menu=home");
    }
}
