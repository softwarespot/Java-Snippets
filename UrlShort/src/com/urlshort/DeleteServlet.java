package com.urlshort;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Create a new data access object.
		final UrlDAO urlDAO = new UrlDAO(database);
		if (urlDAO.clear()) {
			System.out.println(UrlShortStrings.DELETE_SUCCESS);
		} else {
			System.out.println(UrlShortStrings.DELETE_ERROR);
		}

		final ArrayList<UrlShort> list = urlDAO.getUrlShorts();
		if (!list.isEmpty()) {
			for (final UrlShort us : list) {
				System.out.println(us);
				System.out.println();
			}
		}
	}

	@Override
	public void init() throws ServletException {
		super.init();
		database = getServletContext().getRealPath(UrlShortStrings.DATABASE_PATH);
	}
}
