package com.example.rtp2;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/VerifyServlet")
public class VerifyServlet extends HttpServlet {

    private static final String URL = "jdbc:mysql://localhost:3306/Student";
    private static final String USER = "root";
    private static final String PASSWORD = "rahulharsha@9";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String userOtp = request.getParameter("otp");

        HttpSession session = request.getSession();
        String actualOtp = (String) session.getAttribute("otp");

        if (actualOtp != null && actualOtp.equals(userOtp)) {

            // ✅ Get user data from session
            String name = (String) session.getAttribute("name");
            String email = (String) session.getAttribute("email");
            String password = (String) session.getAttribute("password");
            String fatherName = (String) session.getAttribute("fatherName");
            String dob = (String) session.getAttribute("dob");
            String gender = (String) session.getAttribute("gender");


            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

                String sql = "INSERT INTO users (Name, Email,Password, FatherName, DOB, Gender) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.setString(4, fatherName);
                ps.setString(5, dob);
                ps.setString(6, gender);


                int rows = ps.executeUpdate();

                if (rows > 0) {
                    out.println("<script>");
                    out.println("alert('Registration Successful 🎉');");
                    out.println("window.location='home.jsp';");
                    out.println("</script>");
                } else {
                    out.println("<script>");
                    out.println("alert('Registration Failed');");
                    out.println("window.location='register.jsp';");
                    out.println("</script>");
                }

                con.close();

                // ✅ Clear session after success
                session.invalidate();

            } catch (Exception e) {
                e.printStackTrace();
                out.println("<script>alert('Database Error!');window.location='register.jsp';</script>");
            }

        } else {
            out.println("<script>");
            out.println("alert('Invalid OTP ❌');");
            out.println("window.location='verify.jsp';");
            out.println("</script>");
        }
    }
}