<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Url Short Demo</title>

    <!--Mobile Specific Metas-->
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <!--Font-->
    <link href="//fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">

    <!--Stylesheets-->
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/normalize/3.0.2/normalize.min.css"/>
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/skeleton/2.0.4/skeleton.min.css"/>
    <style type="text/css">
        /*Body styling*/
        body {
            color: #222;
            font-family: 'Open Sans', sans-serif, Arial;
            height: 100%;
            position: relative;
        }

        .container {
            margin: 0;
            max-width: none;
            padding: 20px;
            width: 100%;
        }

        .container button,
        .container h1 {
            text-align: center;
        }
        
        .blue,
        .blue input[type='submit'] {
            color: #fafafa;
        }
        
        .blue {
            background: #3498db;
        }
        
        .blue input[type='submit'] {
            background: #e74c3c;
        }
        
        .blue input[type='text'] {
            color: #222;
        }
    </style>
</head>
<body>
<section class="container">
    <h1>UrlShort</h1>

    <div class="row">
        <div class="one-third column">
            <p>The following project was created for the purposes of the Java Programming Course in 2015, by
                way of
                demonstrating how much time the author had invested in understanding the use of Java with Servlets. It's
                a
                simple
                url shortner that consists of a relatively small API set as well as the ability to automatically
                re-direct to a
                site
                using the website url plus the short id e.g. www.example.com/ABCDE.</p>
        </div>
        <div class="two-thirds column">
            <p>The backend Java code includes a host of coding techniques and styles, which showcase just how much has
                been
                learnt
                about the latest version of Java 8. The database access is level 3, in that the servlets interact with
                the
                backend
                DAO and then the SQLite database.</p>
            <ol>
                <li>Regular expressions</li>
                <li>Lambda expressions</li>
                <li>Generic lists</li>
                <li>Object instantiation</li>
                <li>Date and time formatting</li>
                <li>Accessing a local SQLite and/or MariaDb database</li>
                <li>Use of static, final and primitive datatypes</li>
                <li>Base 36 encoding (encoding an integer into a unique id e.g. 4096 => FFYT0)</li>
                <li>Servlet design</li>
            </ol>
        </div>
    </div>
</section>

<section class="container blue">
    <h2>API</h2>

    <p>The API consists of GET and POST requests only, with an optional DELETE to clear the database (for demo
        purposes
        only).</p>

    <p><strong>Note:</strong> To access the API from outside the website, first create a connection to the
        address e.g.
        www.example.com, then send either a GET or POST request using the associated parameter(s) e.g. GET
        /expand?shortid=<strong>ABCDE</strong>. Details as to the return values have been provided underneath
        the
        relevant
        requests.</p>

    <div class="row">
        <div class="two-thirds column">
            <dl>
                <dt>
                    GET /expand?shortid=<strong>ABCDE</strong>
                </dt>
                <dd>
                    <strong>Param:</strong> shortid - A valid short id containing 5 characters from 0-9, A-Z.<br/>
                    <strong>Success:</strong> An associated url with the short id<br/>
                    <strong>Error:</strong> Short id is null or empty OR Short id is an invalid format OR Not found in
                    the
                    database
                </dd>
            </dl>
        </div>
        <div class="one-third column">
            <form name="example" method="GET" action="expand">
                <input type="text" name="shortid"/>
                <input type="submit" value="Retrieve"/>
            </form>
        </div>
    </div>

    <div class="row">
        <div class="two-thirds column">
            <dl>
                <dt>
                    POST /shorten?url=<strong>http://www.github.com/softwarespot</strong>
                </dt>
                <dd>
                    <strong>Param:</strong> url - A valid url which begins with http(s)<br/>
                    <strong>Success:</strong> A short id associated with the url<br/>
                    <strong>Error</strong>:
                    Url is null or empty OR Url is an invalid format OR Not inserted into
                    the database
                </dd>
            </dl>
        </div>
        <div class="one-third column">
            <form name="example" method="POST" action="shorten">
                <input type="text" name="url"/> <input type="submit" value="Submit"/>
            </form>
        </div>
    </div>
</section>

<section class="container">
    <p>As this is a demonstration and not a fully functioning site, a button to clear the database has been included as
        means of showing the SQLite/MariaDb is indeed working as one would expect</p>
    <button type="button" onclick="destroy()">Clear DB</button>
    <p><strong>Note:</strong> This will clear the contents of the database.</p>
</section>

<script>
    function destroy() {
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.open("DELETE", "clear", false);
        xmlHttp.send();
    }
</script>
</body>
</html>