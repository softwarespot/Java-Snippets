package com.urlshort;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class DeleteServlet
 *
 * @author SoftwareSpot
 */
@WebServlet("/clear")
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String database = UrlShortStrings.EMPTY;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Set the response to write JSON
		response.setContentType(UrlShortStrings.WRITE_JSON);
		
		// Create a new data access object.
		final UrlDAO urlDAO = new UrlDAO(database);

		// Determine whether successful or not.
		final boolean isSuccess = urlDAO.clear();
		final String message = isSuccess ? UrlShortStrings.DELETE_SUCCESS : UrlShortStrings.DELETE_ERROR;

		final JSONObject json = new JSONObject();
		json.put(UrlShortStrings.JSON_KEY_MESSAGE, isSuccess ? null : message);
		json.put(UrlShortStrings.JSON_KEY_SUCCESS, isSuccess);

		final ArrayList<UrlShort> list = urlDAO.getUrlShorts();
		if (!list.isEmpty()) {
			for (final UrlShort us : list) {
				System.out.println(us);
				System.out.println();
			}
		}

		// PrintWriter for writing to the response
		final PrintWriter print = response.getWriter();

		// Print the json
		print.println(json);
		
		// Debugging only
		System.out.println(json);

		// Close the printing object.
		print.close();

		// Set the appropriate status code.
		response.setStatus(isSuccess ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NOT_FOUND);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		database = getServletContext().getRealPath(UrlShortStrings.DATABASE_PATH);
	}
}
