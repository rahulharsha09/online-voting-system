package com.example.rtp2;

import java.io.*;
import java.sql.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int voter_id = Integer.parseInt(request.getParameter("voter_id"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Student",
                    "root",
                    "rahulharsha@9"
            );

            PreparedStatement ps = con.prepareStatement(
                    "SELECT u.id FROM users u " +
                            "LEFT JOIN votes v ON u.id = v.id " +
                            "WHERE u.id=? AND u.Name=? AND u.Password=? AND v.id IS NULL"
            );

            ps.setInt(1, voter_id);
            ps.setString(2, username);
            ps.setString(3, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

//                int id = rs.getInt("id");

                HttpSession session = request.getSession();
                session.setAttribute("voter_id", voter_id);

                response.sendRedirect("candidate.jsp");

            } else {

                out.println("<script>");
                out.println("alert('Invalid Login OR You Already Voted');");
                out.println("window.location='login.jsp';");
                out.println("</script>");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}