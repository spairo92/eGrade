package ser;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/readCourses")
public class readCourses extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DataSource datasource = null;

	public void init() throws ServletException{
		try {
	
			InitialContext ctx = new InitialContext();
			datasource = (DataSource)ctx.lookup("java:comp/env/jdbc/LiveDataSource");
		} catch(Exception e) {
			throw new ServletException(e.toString());
		}

	}
       
    public readCourses() {
        super();
    }


    protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
	throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>Courses Table</title></head>");
		out.println("<body>");

		try {
			Connection con = datasource.getConnection();
			Statement stmt = con.createStatement();
	
			out.println("<table border=\"1\">");
			out.println("<tr>");
			out.println("<th>Course Title</th>");
			out.println("<th>Professor's Name</th>");
			out.println("<th>Semester</th>");
			out.println("</tr>");


			ResultSet rs = stmt.executeQuery("SELECT * FROM COURSES");
			while(rs.next()) {
				String title = rs.getString("title");
				String professorsName = rs.getString("professorsName");
				String semester = rs.getString("semester");
				String htmlRow = createHTMLRow(title, professorsName, semester);
				out.println(htmlRow);

			}
			rs.close();
			con.close();
		} catch(Exception e) {
			out.println("Database connection problem");
		}
		out.println("<a href=\"index.html\">Return home page</a>");
		out.println("</body>");
		out.println("</html>");

	}

	private String createHTMLRow(String title, String profName, String semester) {
		String row = "<tr>";
		row  += "<td>" + title + "</td>";
		row  += "<td>" + profName + "</td>";
		row  += "<td>" + semester + "</td>";
		row +="</tr>";
		return row;

	}
}
