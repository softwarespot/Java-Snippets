package com.urlshort;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RedirectServlet
 * 
 * @author SoftwareSpot
 */
@WebServlet("/")
public class RedirectServlet extends HttpServlet {
	private static String database = UrlShortStrings.EMPTY;
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RedirectServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Retrieve the shortid parameter e.g. root/ABCDE
		final String shortId = request.getServletPath().replace("/", UrlShortStrings.EMPTY);

		if (shortId == null || shortId.isEmpty()) {
			System.out.println(UrlShortStrings.REDIRECT_NULL_OR_EMPTY);
		} else if (!UrlShortHelper.isShortId(shortId)) {
			System.out.println(UrlShortStrings.REDIRECT_INVALID_FORMAT			);
		} else {
			// Create a new data access object.
			final UrlDAO urlDAO = new UrlDAO(database);
			final UrlShort urlShort = urlDAO.getFromShortId(shortId);
			if (urlShort == null) {
				System.out.println(UrlShortStrings.REDIRECT_NOT_FOUND);
			} else {
				System.out.println(UrlShortStrings.SUCCESS_ONLY + urlShort.getUrl());
				response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
				response.sendRedirect(urlShort.getUrl());
				return;
			}
		}

		// Redirect back to the main page.
		response.sendRedirect(UrlShortStrings.EMPTY);

		// request.getRequestURI()
		// request.getServletPath()
	}

	@Override
	public void init() throws ServletException {
		super.init();
		database = getServletContext().getRealPath(UrlShortStrings.DATABASE_PATH);
	}
}
