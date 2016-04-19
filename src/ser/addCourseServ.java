package ser;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet("/addCourseServ")
public class addCourseServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DataSource datasource = null;

	public void init() throws ServletException
	{
		try 
		{
	
			InitialContext ctx = new InitialContext();
			datasource = (DataSource)ctx.lookup("java:comp/env/jdbc/LiveDataSource");
		} 
		catch(Exception e) 
		{
			throw new ServletException(e.toString());
		}

	}
       
    public addCourseServ() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String requestType= request.getParameter("requestType");
		if (requestType == null) 
		{
			createDynPage(response, "Invalid request type");
		}
		
		if (requestType.equalsIgnoreCase("insert")) 
		{
			String title = request.getParameter("title");
			String name = request.getParameter("name");
			String semester = request.getParameter("semester");
			
			try 
			{
				Connection con = datasource.getConnection();
				
			    Statement stmt = con.createStatement();
			    StringBuilder x = new StringBuilder();
			   
			    String insertStmt = "INSERT INTO COURSES (title, professorsName, semester) VALUES (";
			    insertStmt += "'" + title + "',";
			    insertStmt += "'" + name + "',";
			    insertStmt += "'" + semester + "')";
			    
			    stmt.executeUpdate(insertStmt);
			    createDynPage(response, "Professor registration completed");
				stmt.close();
			
				con.close();
				
			} 
			catch(SQLException sqle) 
			{
				sqle.printStackTrace();
			}
			
		} 
		else 
		{
			createDynPage(response, "The request type parameter must be insert");
		}
	}
	
	private void createDynPage(HttpServletResponse response, String message) throws IOException 
	{
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>Enter student's information</title></head>");
		out.println("<body>");
		out.println("<p>" + message + "</p>");
		out.println("<a href=\"index.html\">Return home page</a>");
		out.println("</body></html>");
	}

}
