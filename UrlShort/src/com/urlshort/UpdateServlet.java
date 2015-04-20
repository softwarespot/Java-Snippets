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
 * Servlet implementation class UpdateServlet
 *
 * @author SoftwareSpot
 */
@WebServlet("/shorten")
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String database = UrlShortStrings.EMPTY;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(UrlShortStrings.WRITE_JSON);

		// Retrieve the url parameter e.g. POST /shorten?url=http://www.example.com
		final String url = request.getParameter("url");

		// Create a new data access object.
		final UrlDAO urlDAO = new UrlDAO(database);

		// Determine whether successful or not.
		boolean isSuccess = false;
		String message;
		if (url == null || url.isEmpty()) {
			message = UrlShortStrings.UPDATE_NULL_OR_EMPY;
		} else if (!UrlShortHelper.isValidUrl(url)) {
			message = UrlShortStrings.UPDATE_INVALID_FORMAT;
		} else {
			final UrlShort urlShort = urlDAO.insert(url);
			if (urlShort == null) {
				message = UrlShortStrings.UPDATE_NOT_INSERTED;
			} else {
				isSuccess = true;
				message = urlShort.getShortId();
			}
		}

		final JSONObject json = new JSONObject();
		json.put(UrlShortStrings.JSON_KEY_ID, isSuccess ? message : null);
		json.put(UrlShortStrings.JSON_KEY_MESSAGE, isSuccess ? null : message);
		json.put(UrlShortStrings.JSON_KEY_SUCCESS, isSuccess);
		json.put(UrlShortStrings.JSON_KEY_URL, isSuccess ? url : null);

		final ArrayList<UrlShort> list = urlDAO.getUrlShorts();
		if (list.isEmpty()) {
			System.out.println(UrlShortStrings.DATABASE_EMPTY);
		} else {
			System.out.println(UrlShortStrings.DEMO);
			for (final UrlShort us : list) {
				System.out.println(us);
				System.out.println();
			}
		}

		// PrintWriter for writing to the response
		final PrintWriter print = response.getWriter();

		// Print the json
				print.println(json);

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
