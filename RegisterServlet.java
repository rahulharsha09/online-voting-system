package com.example.rtp2;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.security.SecureRandom;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override  // ✅ Added - good practice
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name       = request.getParameter("name");
        String email      = request.getParameter("email");
        String password   = request.getParameter("password");
        String fatherName = request.getParameter("Father_name");
        String dob        = request.getParameter("dob");
        String gender     = request.getParameter("gender");


        try {
            LocalDate d = LocalDate.parse(dob);
            int age = Period.between(d, LocalDate.now()).getYears();

            if (age < 18) {
                out.println("<script>alert('Age must be 18+');window.location='register.jsp';</script>");
                return;
            }

            SecureRandom random = new SecureRandom();
            String otp = String.valueOf(100000 + random.nextInt(900000));

            HttpSession session = request.getSession();
            session.setAttribute("otp", otp);
            session.setAttribute("name", name);
            session.setAttribute("email", email);
            session.setAttribute("password", password);
            session.setAttribute("fatherName", fatherName);
            session.setAttribute("dob", dob);
            session.setAttribute("gender", gender);

            // ❌ Removed System.out.println — exposes OTP in server logs (security risk)

            boolean sent = EmailUtil.sendOTP(email, otp);

            if (sent) {
                response.sendRedirect("verify.jsp");
            } else {
                out.println("<script>alert('Failed to send OTP. Please try again.');window.location='register.jsp';</script>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('Error: " + e.getMessage() + "');window.location='register.jsp';</script>");
        }
    }
}