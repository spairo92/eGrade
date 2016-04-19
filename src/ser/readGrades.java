package ser;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet("/readGrades")
public class readGrades extends HttpServlet {
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
	
    public readGrades() {
        super();
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String requestType= request.getParameter("requestType");
		
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>Student's grade</title></head>");
		out.println("<body>");
		
		requestType.equalsIgnoreCase("insert");
		String regNr = request.getParameter("registrationNumber");
			
			
		//----------------------------------------------------------------------------
		
		try 
		{
			Connection con = datasource.getConnection();
			Statement stmt = con.createStatement();
			
			out.println("<h3>Student's Grades:</h3>");
			out.println("<table border=\"1\">");
			out.println("<tr>");
			out.println("<th>Registration Number</th>");
			out.println("<th>Course</th>");
			out.println("<th>Grade</th>");
			out.println("</tr>");

			ResultSet rs = stmt.executeQuery("SELECT * FROM GRADES WHERE registrationNumber=" + regNr );
			while(rs.next()) {
				String registrationNumber = rs.getString("registrationNumber");
				String course = rs.getString("course");
				String grade = rs.getString("grade");
				String htmlRow = createHTMLRow(registrationNumber, course, grade);
				out.println(htmlRow);
			}
			rs.close();
			con.close();
		} 
		catch(Exception e) 
		{
			out.println("Database connection problem");
		}
		//----------------------------------------------------------------------------
		out.println("</body></html>");
	}
	
	
	private String createHTMLRow(String regNum, String course, String grade) 
	{
		String row = "<tr>";
		row  += "<td>" + regNum + "</td>";
		row  += "<td>" + course + "</td>";
		row  += "<td>" + grade + "</td>";
		row +="</tr>";
		return row;

	}

}
