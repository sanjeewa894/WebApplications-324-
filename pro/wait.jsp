<!DOCTYPE html>
<html>
    <head>
        <title>Wait utill other connect</title>
    </head>
    <body onload="start()"> 
    <br><br>
           
    <br><br>        
        message: <span id="foo"></span>
        <br><br>
        
    <script type="text/javascript">
    function start() { 
        var eventSource = new EventSource("/WEB-INF/classes/CheckConnection");         
        	eventSource.onmessage = function(event) {         
          	document.getElementById('foo').innerHTML = event.data;         
        };         
    }
    </script>    
    </body>
</html>
