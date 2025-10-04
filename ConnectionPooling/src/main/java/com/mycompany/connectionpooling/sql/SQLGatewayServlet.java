package com.mycompany.connectionpooling.sql;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class SQLGatewayServlet extends HttpServlet {

  // Lấy cấu hình DB từ ENV (Render) -> fallback về localhost khi chạy máy cá nhân
  private static final String URL  = System.getenv().getOrDefault(
      "JDBC_URL", "jdbc:postgresql://localhost:5432/postgres?sslmode=disable");
  private static final String USER = System.getenv().getOrDefault("DB_USER", "postgres");
  private static final String PASS = System.getenv().getOrDefault("DB_PASS", "postgres");

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.getRequestDispatcher("/index.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("text/html; charset=UTF-8");
    PrintWriter out = response.getWriter();

    String sql = request.getParameter("sql");
    if (sql == null || sql.isBlank()) {
      out.println("<p style='color:red'>SQL rỗng!</p>");
      return;
    }

    out.println("<h2>The SQL Gateway</h2>");
    out.println("<form method='post' action='sqlGateway'>");
    out.printf("<textarea name='sql' rows='6' cols='70'>%s</textarea><br/>", sql);
    out.println("<button type='submit'>Execute</button>");
    out.println("</form>");
    out.println("<h3>SQL result:</h3>");

    try {
      Class.forName("org.postgresql.Driver"); // driver có trong pom
      try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
           Statement st = conn.createStatement()) {

        boolean hasRS = st.execute(sql);
        if (hasRS) {
          try (ResultSet rs = st.getResultSet()) {
            ResultSetMetaData md = rs.getMetaData();
            int n = md.getColumnCount();
            out.println("<table border='1' cellspacing='0' cellpadding='6'><tr>");
            for (int i = 1; i <= n; i++) out.printf("<th>%s</th>", md.getColumnLabel(i));
            out.println("</tr>");
            while (rs.next()) {
              out.println("<tr>");
              for (int i = 1; i <= n; i++) out.printf("<td>%s</td>", String.valueOf(rs.getObject(i)));
              out.println("</tr>");
            }
            out.println("</table>");
          }
        } else {
          out.printf("<p>Update count: %d</p>", st.getUpdateCount());
        }
      }
    } catch (Exception e) {
      out.printf("<pre style='color:red'>%s</pre>", e.toString());
      e.printStackTrace(out);
    }
  }
}
