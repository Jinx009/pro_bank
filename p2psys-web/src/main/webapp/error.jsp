<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Error Page</title></head>
<body>
    <h3>Exception:</h3>
   ${exception}

    <h3>Stack trace:</h3>
    <pre>
        ${exceptionStack}
    </pre>
</body>
</html>