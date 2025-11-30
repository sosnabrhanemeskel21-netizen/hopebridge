import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HomeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String name = request.getParameter("name");
        if (name == null || name.trim().isEmpty()) {
            name = "Guest";
        }
        
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Welcome - Project1</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; background-color: #f4f4f4; }");
        out.println(".container { background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        out.println("h1 { color: #333; }");
        out.println(".info { color: #666; margin: 20px 0; }");
        out.println("a { color: #007bff; text-decoration: none; }");
        out.println("a:hover { text-decoration: underline; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>Welcome, " + name + "!</h1>");
        out.println("<div class='info'>");
        out.println("<p><strong>Current Time:</strong> " + new Date() + "</p>");
        out.println("<p><strong>Server Info:</strong> " + getServletContext().getServerInfo() + "</p>");
        out.println("</div>");
        out.println("<p><a href='index.jsp'>Go to Home Page</a></p>");
        out.println("<p><a href='home?name=Developer'>Try with a name parameter</a></p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

