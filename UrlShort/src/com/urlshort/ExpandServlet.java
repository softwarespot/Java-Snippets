package com.urlshort;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        // PrintWriter for writing to the response
        final PrintWriter print = response.getWriter();

        // Retrieve the shortid parameter e.g. GET /expand?shortid=ABCDE
        final String shortId = request.getParameter("shortid");

        // Create a new data access object.
        final UrlDAO urlDAO = new UrlDAO(database);

        // Determine whether successful or not.
        boolean isSuccess = false;
        if (shortId == null || shortId.isEmpty()) {
            print.println(UrlShortStrings.EXPAND_NULL_OR_EMPTY);
        } else if (!UrlShortHelper.isShortId(shortId)) {
            print.println(UrlShortStrings.EXPAND_INVALID_FORMAT);
        } else {
            final UrlShort urlShort = urlDAO.getFromShortId(shortId);
            if (urlShort == null) {
                print.println(UrlShortStrings.EXPAND_NOT_FOUND);
            } else {
                isSuccess = true;
                print.println(UrlShortStrings.SUCCESS_ONLY + urlShort.getUrl());
            }
        }

        print.println();
        final ArrayList<UrlShort> list = urlDAO.getUrlShorts();
        if (list.isEmpty()) {
            print.println(UrlShortStrings.DATABASE_EMPTY);
        } else {
            print.println(UrlShortStrings.DEMO);
            for (final UrlShort us : list) {
                print.println(us);
                print.println();
            }
        }

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
