package com.urlshort;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author SoftwareSpot
 */
public class UrlDAO {
	private static final String _ADDED = "added";
	private static final String _ID = "id";
	private static final String _URLSTRING = "urlstring";
	private static final String DATABASE_NAME = "urlshort";
	private static final String dateFormat = "yyyy/MM/dd HH:mm:ss";
	private static final String TABLE_NAME = "urlshort";
	private Connection connection = null;
	private final String connectionString;
	private final boolean isSQLiteDb;
	private String password;

	private String username;

	// Utilises: https://bitbucket.org/xerial/sqlite-jdbc
	public UrlDAO(String filepath) {
		// connectionString = "jdbc:sqlite::memory:";
		connectionString = "jdbc:sqlite:" + filepath.replace('\\', '/');
		isSQLiteDb = true;
	}

	public UrlDAO(String username, String password) {
		connectionString = "jdbc:mariadb://localhost/" + DATABASE_NAME;
		isSQLiteDb = false;
		setCredentials(username, password);
	}

	public boolean clear() {
		boolean isDropped = false;
		if (connect()) {
			try {
				isDropped = connection.createStatement().executeUpdate("DROP TABLE " + TABLE_NAME) > 0;
			} catch (final SQLException e) {
			} finally {
				close();
			}
			if (!isDropped) {
				isDropped = getUrlShorts().isEmpty();
			}
		}
		return isDropped;
	}

	public void close() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}

		} catch (final SQLException e) {
		}
		connection = null;
	}

	private boolean connect() {
		boolean isConnected = false;
		if (isValidCredentials()) { // Check again if the password and username aren't null.
			try {
				// Creating the driver object instance and opening a connection to the database.
				if (isSQLiteDb) {
					Class.forName("org.sqlite.JDBC");
					connection = DriverManager.getConnection(connectionString);
				} else {
					Class.forName("org.mariadb.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionString, username, password);
				}

				if (connection != null) {
					init(); // Create a new database and values if required.
					isConnected = true; // If this is assigned then assume there were no issues.
				}
			} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
				if (isSQLiteDb) {
					setCredentials(null, null); // Set the username and password to null.
				}
			} finally {
				if (!isConnected) {
					close();
				}
			}
		}

		return isConnected;
	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	private String getCurrentDate() {
		return new SimpleDateFormat(dateFormat).format(new Date());
	}

	// null indicates an error; otherwise, a UrlShort object.
	public UrlShort getFromShortId(String shortId) {
		UrlShort urlShort = null;
		if (connect()) {
			try {
				final PreparedStatement search = connection.prepareStatement("SELECT " + _ID + ", " + _URLSTRING + ", " + _ADDED + " FROM " + TABLE_NAME + " WHERE id = ?");
				final int id = UrlShortHelper.decode(shortId);
				search.setInt(1, id);
				final ResultSet results = search.executeQuery();

				if (results.next()) {
					urlShort = new UrlShort(id, shortId, results.getString(_URLSTRING), results.getString(_ADDED));
				}
			} catch (final SQLException e) {
			} finally {
				close();
			}
		}
		return urlShort;
	}

	// null indicates an error; otherwise, a UrlShort object.
	public UrlShort getFromUrl(String url) {
		UrlShort urlShort = null;
		if (connect()) {
			try {
				final PreparedStatement search = connection.prepareStatement("SELECT " + _ID + ", " + _URLSTRING + ", " + _ADDED + " FROM " + TABLE_NAME + " WHERE urlstring = ?");
				search.setString(1, url);
				final ResultSet results = search.executeQuery();

				if (results.next()) {
					final int id = results.getInt("id");
					urlShort = new UrlShort(id, UrlShortHelper.encode(id), results.getString(_URLSTRING), results.getString(_ADDED));
				}
			} catch (final SQLException e) {
			} finally {
				close();
			}
		}
		return urlShort;
	}

	// null indicates an error; otherwise, a list of UrlShort objects.
	public ArrayList<UrlShort> getUrlShorts() {
		ArrayList<UrlShort> urlShorts = null;
		if (connect()) {
			try {
				final Statement search = connection.createStatement();
				final ResultSet results = search.executeQuery("SELECT " + _ID + ", " + _URLSTRING + ", " + _ADDED + " FROM " + TABLE_NAME);

				urlShorts = new ArrayList<UrlShort>();
				while (results.next()) {
					final int id = results.getInt("id");
					final UrlShort urlShort = new UrlShort(id, UrlShortHelper.encode(id), results.getString(_URLSTRING), results.getString(_ADDED));
					urlShorts.add(urlShort);
				}
			} catch (final SQLException e) {
			} finally {
				close();
			}
		}
		return urlShorts;
	}

	private boolean init() {
		boolean isCreated = false;
		try {
			final Statement create = connection.createStatement();
			create.setQueryTimeout(30);

			final String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY, " + _URLSTRING + " TEXT UNIQUE NOT NULL, " + _ADDED + " TEXT NOT NULL)"; // http://www.sqlite.org/faq.html#q1
			isCreated = create.executeUpdate(sql) > 0; // Check if an edit occurred.

			// TESTING PURPOSES ONLY!
			// final String added = getCurrentDate(); // Get current date and time.
			// create.executeUpdate("INSERT INTO urlshort (id, urlstring, added) VALUES(NULL, '" + "http://www.java.com', '" + added + "')");
			// create.executeUpdate("INSERT INTO urlshort (id, urlstring, added) VALUES(NULL, '" + "http://www.google.com', '" + added + "')");
		} catch (final SQLException e) {
		}
		return isCreated;
	}

	// null indicates an error; otherwise, a UrlShort object.
	public UrlShort insert(String url) {
		UrlShort urlShort = null;
		if (UrlShortHelper.isValidUrl(url) && connect()) {
			boolean isInserted = false;
			try {
				connection.setAutoCommit(false);

				final PreparedStatement create = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" + _ID + ", " + _URLSTRING + ", " + _ADDED + ") VALUES(NULL, ?, ?)");
				create.setString(1, url);
				final String added = getCurrentDate(); // Get current date and time.
				create.setString(2, added);

				if (create.executeUpdate() > 0) { // Check if an edit occurred.
					connection.commit();
					isInserted = true;
				}
			} catch (final SQLException e) {
				try {
					connection.rollback();
				} catch (final SQLException e1) {
				}
			} finally {
				try {
					connection.setAutoCommit(true);
				} catch (final SQLException e) {
				} finally {
					close();
				}
			}

			if (isInserted) {
				urlShort = getFromUrl(url);
			}
		}
		return urlShort;
	}

	private boolean isValidCredentials() {
		return password != null && username != null || isSQLiteDb;
	}

	private void setCredentials(String username, String password) {
		this.password = password != null && !password.trim().isEmpty() ? password : null;
		this.username = username != null && !username.trim().isEmpty() ? username : null;
	}
}
