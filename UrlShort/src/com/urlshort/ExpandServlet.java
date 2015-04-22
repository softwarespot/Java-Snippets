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
 * Servlet implementation class ExpandServlet
 *
 * @author SoftwareSpot
 */
@WebServlet("/expand")
public class ExpandServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String database = UrlShortStrings.EMPTY;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExpandServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("EXPANDSERVLET");
		
		// Set the response to write JSON
		response.setContentType(UrlShortStrings.WRITE_JSON);

		// Retrieve the shortid parameter e.g. GET /expand?shortid=ABCDE
		final String shortId = request.getParameter("shortid");

		// Parameter(s)
		System.out.println("1. " + shortId);

		// Create a new data access object.
		final UrlDAO urlDAO = new UrlDAO(database);

		// Determine whether successful or not.
		boolean isSuccess = false;
		String message = null, url = null;
		if (shortId == null || shortId.isEmpty()) {
			message = UrlShortStrings.EXPAND_NULL_OR_EMPTY;
		} else if (!UrlShortHelper.isShortId(shortId)) {
			message = UrlShortStrings.EXPAND_INVALID_FORMAT;
		} else {
			final UrlShort urlShort = urlDAO.getFromShortId(shortId);
			if (urlShort == null) {
				message = UrlShortStrings.EXPAND_NOT_FOUND;
			} else {
				isSuccess = true;
				url = urlShort.getUrl();
			}
		}

		final JSONObject json = new JSONObject();
		json.put(UrlShortStrings.JSON_KEY_ID, shortId);
		json.put(UrlShortStrings.JSON_KEY_MESSAGE, message);
		json.put(UrlShortStrings.JSON_KEY_SUCCESS, isSuccess);
		json.put(UrlShortStrings.JSON_KEY_URL, url);

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
