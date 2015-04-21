<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type">

    <title>Url Short Demo</title><!--Mobile Specific Metas-->
    <meta content="width=device-width, initial-scale=1" name="viewport">
    <!--Font-->
    <link href="//fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
    <!--Stylesheets-->
    <link href="//cdnjs.cloudflare.com/ajax/libs/normalize/3.0.2/normalize.min.css" rel="stylesheet">
    <link href="//cdnjs.cloudflare.com/ajax/libs/skeleton/2.0.4/skeleton.min.css" rel="stylesheet">
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

        .error {
            color: #c0392b;
        }

        .success {
            color: #2cc36b;
        }

        #clear-message {
            margin: 0 0 0 5px;
        }
    </style>
</head>

<body>
<section class="container">
    <h1>UrlShort</h1>

    <div class="row">
        <div class="one-third column">
            <p>The following project was created for the purposes of the
                Java Programming Course in 2015, by way of demonstrating how
                much time the author had invested in understanding the use of
                Java with Servlets. It's a simple url shortner that consists of
                a relatively small API set as well as the ability to
                automatically re-direct to a site using the website url plus
                the short id e.g. www.example.com/ABCDE.</p>
        </div>

        <div class="two-thirds column">
            <p>The backend Java code includes a host of coding techniques
                and styles, which showcase just how much has been learnt about
                the latest version of Java 8. The database access is level 3,
                in that the servlets interact with the backend DAO and then the
                SQLite database.</p>

            <ol>
                <li>Regular expressions</li>

                <li>Lambda expressions</li>

                <li>Generic lists</li>

                <li>Object instantiation</li>

                <li>Date and time formatting</li>

                <li>Accessing a local SQLite and/or MariaDb database</li>

                <li>Use of static, final and primitive datatypes</li>

                <li>Base 36 encoding (encoding an integer into a unique id
                    e.g. 4096 =&gt; FFYT0)
                </li>

                <li>Servlet design</li>

                <li>JavaScript Object Notation (JSON)</li>
            </ol>
        </div>
    </div>
</section>

<section class="container blue">
    <h2>API</h2>

    <p>The API consists of GET and POST requests only, with an optional
        DELETE to clear the database (for demonstration purposes only).</p>

    <p><strong>Note:</strong> To access the API from outside the website,
        first create a connection to the address e.g. www.example.com, then
        send either a GET or POST request using the associated parameter(s)
        e.g. GET /expand?shortid=<strong>ABCDE</strong>. Details as to the
        return values have been provided underneath the relevant requests.</p>

    <div class="row">
        <div class="two-thirds column">
            <dl>
                <dt>GET /expand?shortid=<strong>ABCDE</strong></dt>

                <dd><strong>Param:</strong> shortid - A valid short id
                    containing 5 characters from 0-9, A-Z.<br>
                    <strong>Return:</strong> { "success": true/false, "id":
                    shortId/null, "message": null/error message, "url":
                    url/null }
                </dd>
            </dl>
        </div>

        <div class="one-third column">
            <form id="expand-id" action="expand" method="get">
                <input name="shortid" type="text">
                <input type="submit" value="Retrieve">
            </form>
            <div id="expand-message"></div>
        </div>
    </div>

    <div class="row">
        <div class="two-thirds column">
            <dl>
                <dt>POST
                    /shorten?url=<strong>http://www.github.com/softwarespot</strong></dt>

                <dd><strong>Param:</strong> url - A valid url which begins
                    with http(s)<br>
                    <strong>Return:</strong> { "success": true/false, "id":
                    shortId/null, "message": null/error message, "url":
                    url/null }
                </dd>
            </dl>
        </div>

        <div class="one-third column">
            <form id="update-id" action="shorten" method="post">
                <input name="url" type="text">
                <input type="submit" value="Submit">
            </form>
            <div id="update-message"></div>
        </div>
    </div>
</section>

<section class="container">
    <p>As this is a demonstration and not a fully functioning site, a
        button to clear the database has been included as means of showing the
        SQLite/MariaDb is indeed working as one would expect. Check the console
        output for additional messages.</p>
    <button onclick="destroy()" type=
            "button">Clear DB
    </button>
    <span id="clear-message"></span>

    <p><strong>Note:</strong> This will clear the contents of the
        database.</p>
</section>

<script>
    var formExpand = document.getElementById("expand-id-error");
    formExpand.onsubmit = function () {
        // Get the clear message id element
        var messageId = document.getElementById("expand-message");

        // Get the form data
        var formData = new FormData(formExpand);

        // AJAX object reference
        var request = new XMLHttpRequest();
        request.open("GET", formExpand.getAttribute("action"), true);

        // On load callback
        request.onload = function () {
            var data = JSON.parse(request.responseText);
            alert(data.success);
            if (request.status === 200 && data.success === true) {
                // Success
                messageId.className = "success";
                messageId.innerHTML =
                        "The short id " + data.id + "navigates to " +
                        data.url;
            } else {
                // We reached our target server, but it returned an error
                messageId.className = "error";
                messageId.innerHTML = "Error: " + data.message;
            }
        };

        // On error callback
        request.onerror = function () {
            // A serious error occurred
            messageId.className = "error";
            messageId.innerHTML =
                    "A serious error occurred that the target server never replied";
        };

        // Send form data using the request
        request.send(formData);

        return false;
    };

    var formUpdate = document.getElementById("update-id-error");
    formUpdate.onsubmit = function () {
        // Get the clear message id element
        var messageId = document.getElementById("update-message");

        // Get the form data
        var formData = new FormData(formUpdate);

        // AJAX object reference
        var request = new XMLHttpRequest();
        request.open("POST", formUpdate.getAttribute("action"), true);

        // On load callback
        request.onload = function () {
            var data = JSON.parse(request.responseText);
            if (request.status === 200 && data.success === true) {
                // Success
                messageId.className = "success";
                messageId.innerHTML =
                        "Added to the database with the short id of " +
                        data.id;
            } else {
                // We reached our target server, but it returned an error
                messageId.className = "error";
                messageId.innerHTML = "Error: " + data.message;
            }
        };

        // On error callback
        request.onerror = function () {
            // A serious error occurred
            messageId.className = "error";
            messageId.innerHTML =
                    "A serious error occurred that the target server never replied";
        };

        // Send form data using the request
        request.send(formData);

        return false;
    };

    function destroy() {
        // Get the clear message id element
        var messageId = document.getElementById("clear-message");

        // AJAX object reference
        var request = new XMLHttpRequest();
        request.open("DELETE", "clear", true);

        // On load callback
        request.onload = function () {
            var data = JSON.parse(request.responseText);
            if (request.status === 200 && data.success === true) {
                // Success
                messageId.className = "success";
                messageId.innerHTML = "The database was successfully cleared";
            } else {
                // We reached our target server, but it returned an error
                messageId.className = "error";
                messageId.innerHTML = "Error: " + data.message;
            }
        };

        // On error callback
        request.onerror = function () {
            // A serious error occurred
            messageId.className = "error";
            messageId.innerHTML = "A serious error occurred that the target server never replied";
        };

        // Send using the request
        request.send();
    }
</script>
</body>
</html>
