package asd;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class
 */
@WebServlet({ "/Search", "/Homework/Search" })
public class Searchs extends HttpServlet {

	private static final long serialVersionUID = 1L;
	Search s;

	public void init() throws ServletException {
		ServletContext context = this.getServletContext();
		 s = new Search();
		 try {
		 s.readFile();
		 } catch (IOException | ParseException e) {
		 e.printStackTrace();
		 }
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		@SuppressWarnings("unchecked")
		ArrayList<String> result = request.getAttribute("result") == null ? new ArrayList<String>()
				: (ArrayList<String>) request.getAttribute("result");
		// System.out.print("the" + result != null);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("	<meta charset=\"UTF-8\">");
		out.println("	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />");
		out.println(" <meta name=\"description\" content=\"test search widget\" />");
		out.println("	<title>Simple stuff</title>");
		out.println("	<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css\">");

		out.println(" <script src=\"https://code.jquery.com/jquery-2.1.4.min.js\"></script>");

		out.println("</head>");
		out.println("<body >asdasd");
		out.println(" <script language=\"javascript\">");

		out.println("");

		out.println("$(document).ready(function() {");
		out.println(" 	var $searchButton = $('#s');");
		out.println("var $searchQuery = $('#searchBox').val();");

		out.println("var appId = ':OoOHrxezkeLz5IadDuqHWyExHwuP+kgVmm6TcGVuc4o';");
		out.println("	function getImage() {");

		out.println(" var azureKey = btoa(appId);");
		out.println(" var $searchQuery = $('#searchBox').val();");
		out.println(" 	var myUrl = 'https://api.datamarket.azure.com/Bing/Search/v1/Composite?Sources=%27image%27&$top=50&$format=json&Query=%27'");
		out.println(" 	+ $searchQuery + '%27';");
		out.println(" $.ajax({ method : 'post',url : myUrl,");
		out.println(" headers : {	'Authorization' : 'Basic ' + azureKey},");
		out.println("success : function(data) {");
		out.println("var randomIndex = Math.floor(Math.random() * 50);");
		out.println(" var imgLink = '<img width=\"500px\" src=\"'");
		out.println(" + data.d.results[0].Image[randomIndex].MediaUrl");
		out.println(" + '\" />';");
		out.println("$('#output').html(imgLink);},");
		out.println(" failure : function(err) {console.error(err);}");
		out.println(" });");
		out.println(" };");

		out.println(" $searchButton.click(function(e) {e.preventDefault();  getImage(); });});");
		out.println(" </script>");
		//
		// out.println("	<div class=\"page-header\">");
		// out.println("		<h1>Welcome <small></small>" + " !</h1>");
		// out.println("	</div>");
		out.println("	<form action=\"Search\" method=\"post\">");

		out.println("		<input value=\""
				+ "\" placeholder=\"Enter here\" type=\"text\" name=\"query\" id=\"searchBox\" />");
		out.println("		<br />");

		out.println("		<br />");
		out.println("		<input type=\"submit\" value=\"Search\" class=\"btn btn-primary\"  color=\"blue\"/>");
		out.println("		<br />");
		out.println(" <button id=\"s\" type=\"submit\">reandom</button>");
		out.println("	</form>");
		out.println("</div>");
		out.println("<div class=\"container\">");
		out.println(" <table class=\"table table-striped table-hover table-bordered\">");
		out.println("<tr>");
		out.println("<td>");
		out.println("url");
		out.println("</td>");
		out.println("<td>");
		out.println("tfidf");
		out.println("</td>");
		out.println("<td>");
		out.println("link");
		out.println("</td>");
		out.println("<td>");
		out.println("final scrore");
		out.println("</td>");
		out.println("<td>");
		out.println("proximity");
		out.println("</td>");
		out.println("</tr>");
		System.out.println("result size:" + result.size());
		if (result.size() > 0)
			for (String doc : result) {
				String[] str = doc.split(",");
				System.out.println(str[0]);
				out.println("<tr>");
				out.println("<td>");

				out.println("<a href=\"" + str[0] + "\">" + str[0] + "</a>");
				out.println("</td>");

				out.println("<td>");
				out.println(str[1]);
				out.println("</td>");

				out.println("<td>");
				out.println(str[2]);
				out.println("</td>");
				out.println("<td>");
				out.println(str[3]);
				out.println("</td>");
				out.println("<td>");
				out.println(str[4]);
				out.println("</td>");
				out.println("</tr>");
			}

		out.println("</table>");
		out.println("</div>");
		out.println("<div class=\"container\">");
		out.println("<main>");
//		out.println(" <form>");
//		out.println("<input id=\"searchBox\" type=\"text\" type=\"submit\" name=\"searchBox\" required/>");
//		out.println(" <button id=\"s\" type=\"submit\">Get Image</button>");
//		
//		
//	
//
//		out.println(" </form>");
		out.println(" <div id=\"output\"></div>");
		out.println("</main>");

		out.println(" ");

		out.println("</body>");
		out.println("</html>	");

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String query = request.getParameter("query");
		// System.out.println(query);
		if (query != null && query != "") {

			ArrayList<String> result = s.search(query);
			// System.out.println(result.size());

			request.setAttribute("result", result);
			doGet(request, response);
		}
	}

}
