package com.example.rtp2;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CandidateServlet")
public class CandidateServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int voter_id = Integer.parseInt(request.getParameter("voter_id"));
        String Party = request.getParameter("party");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Student",
                    "root",
                    "rahulharsha@9"
            );

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO votes(id,Party) VALUES (?,?)"

            );
            ps.setInt(1,voter_id);
            ps.setString(2, Party);

            int i = ps.executeUpdate();

            if (i > 0) {
                out.println("<script type='text/javascript'>");
                out.println("alert('Vote Submitted Successfully!');");
                out.println("window.location='home.jsp';"); // redirect page (optional)
                out.println("</script>");
                con.close();
            } else {
                out.println("<h2>Vote Failed</h2>");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}