package com.urlshort;

final class UrlShortStrings {
	public static final String DATABASE_EMPTY = "Error: The database is empty";
	public static final String DATABASE_PATH = "/WEB-INF/data/urlshort.db";
	public static final String DELETE_ERROR = "Error: Deleting database table";
	public static final String DELETE_SUCCESS = "Success: Deleted database table";
	public static final String DEMO = "[DEMO] Datebase entries: ";
	public static final String EMPTY = "";
	public static final String EXPAND_INVALID_FORMAT = "Short id is an invalid format";
	public static final String EXPAND_NOT_FOUND = "Not found in the database";
	public static final String EXPAND_NULL_OR_EMPTY = "Short id is null or empty";
	public static final String JSON_KEY_ID = "id";
	public static final String JSON_KEY_MESSAGE = "message";
	public static final String JSON_KEY_SUCCESS = "success";
	public static final String JSON_KEY_URL = "url";
	public static final String REDIRECT_INVALID_FORMAT = "Error: Short id is an invalid format";
	public static final String REDIRECT_NOT_FOUND = EXPAND_NOT_FOUND;
	public static final String REDIRECT_NULL_OR_EMPTY = "Error: Short id is null or empty";
	public static final String UPDATE_INVALID_FORMAT = "Url is an invalid format";
	public static final String UPDATE_NOT_INSERTED = "Not inserted into the database";
	public static final String UPDATE_NULL_OR_EMPY = "Url is null or empty";
	public static final String WRITE_JSON = "application/json";
	public static final String WRITE_TEXT = "plain/text";
}
