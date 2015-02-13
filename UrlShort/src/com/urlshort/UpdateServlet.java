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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        // PrintWriter for writing to the response
        final PrintWriter print = response.getWriter();

        // Retrieve the url parameter e.g. POST /shorten?url=http://www.example.com
        final String url = request.getParameter("url");

        // Create a new data access object.
        final UrlDAO urlDAO = new UrlDAO(database);

        // Determine whether successful or not.
        boolean isSuccess = false;
        if (url == null || url.isEmpty()) {
            print.println(UrlShortStrings.UPDATE_NULL_OR_EMPY);
        } else if (!UrlShortHelper.isValidUrl(url)) {
            print.println(UrlShortStrings.UPDATE_INVALID_FORMAT);
        } else {
            final UrlShort urlShort = urlDAO.insert(url);
            if (urlShort == null) {
                print.println(UrlShortStrings.UPDATE_NOT_INSERTED);
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
