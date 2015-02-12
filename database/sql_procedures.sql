-- DROP TABLE
DROP TABLE urlshort

-- CREATE TABLE
CREATE TABLE IF NOT EXISTS urlshort 
(
	id INTEGER PRIMARY KEY, 
	urlstring TEXT UNIQUE NOT NULL, 
	added TEXT NOT NULL
)

-- INSERT VALUE(S)
INSERT INTO urlshort (id, urlstring, added) VALUES (NULL, 'http://www.github.com/softwarespot', '2015/01/01 00:00:00')

-- TESTING ONLY
SELECT * FROM urlshort