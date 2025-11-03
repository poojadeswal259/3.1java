CREATE DATABASE schooldb;

USE schooldb;

CREATE TABLE Attendance (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    StudentID VARCHAR(20),
    Date DATE,
    Status VARCHAR(10)
);


<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Attendance Form</title>
</head>
<body>
    <h2>Student Attendance Portal</h2>

    <form action="AttendanceServlet" method="post">
        <label>Student ID:</label>
        <input type="text" name="studentId" required><br><br>

        <label>Date:</label>
        <input type="date" name="date" required><br><br>

        <label>Status:</label>
        <select name="status" required>
            <option value="Present">Present</option>
            <option value="Absent">Absent</option>
        </select><br><br>

        <input type="submit" value="Submit Attendance">
    </form>
</body>
</html>


  import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {

    // Database credentials
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/schooldb";
    private static final String JDBC_USER = "root";     // change as per your setup
    private static final String JDBC_PASS = "12345";    // change as per your setup

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String studentId = request.getParameter("studentId");
        String date = request.getParameter("date");
        String status = request.getParameter("status");

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to DB
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            // Insert record into Attendance table
            String sql = "INSERT INTO Attendance (StudentID, Date, Status) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, studentId);
            ps.setString(2, date);
            ps.setString(3, status);

            int rows = ps.executeUpdate();

            // Confirmation message
            out.println("<html><body>");
            if (rows > 0) {
                out.println("<h2>Attendance Recorded Successfully!</h2>");
                out.println("<p><b>Student ID:</b> " + studentId + "</p>");
                out.println("<p><b>Date:</b> " + date + "</p>");
                out.println("<p><b>Status:</b> " + status + "</p>");
            } else {
                out.println("<h3 style='color:red;'>Failed to record attendance.</h3>");
            }
            out.println("<br><a href='attendance.jsp'>Go Back</a>");
            out.println("</body></html>");

            ps.close();
            conn.close();

        } catch (Exception e) {
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace(out);
        }
    }
}
