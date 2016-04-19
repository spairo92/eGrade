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

@WebServlet("/readProfessorServ")
public class readProfessorServ extends HttpServlet {
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
       
    public readProfessorServ() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String requestType= request.getParameter("requestType");
		PrintWriter out = response.getWriter();
		try
		{
			Connection con = datasource.getConnection();
			Statement stmt = con.createStatement();
			
			requestType.equalsIgnoreCase("insert"); 
			String username = request.getParameter("username");
			ResultSet rs = stmt.executeQuery("SELECT USERNAME FROM PROFESSORS WHERE USERNAME=" + username );
		
			int count=0;
			if (rs.next())
		        count = rs.getInt(1);	        
//		    if (count == 0){
//		        return false;}
//		    else
//		        {return true;}
			
			if(count==0)
			{
				out.println("<p align=center><br>");
				out.println("<a href=\"professor2.html\">Add Student's Grade</a>");
				out.println("<a href=\"readCourses\">Show Courses File</a></p>");
			}
			else
			{		
				out.println("You are not registered, inform secretary to register you");
			}
			rs.close();
			con.close();
		}
		catch(Exception e) 
		{
			out.println("Database connection problem");
		}
	}
	

}
