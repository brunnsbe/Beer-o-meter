<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en" ng-app="beerOMeter">
    <head>
		<!--
		Programmed by André Brunnsberg, Finland with a lot of blood, sweat and beers. :-)				
		-->	
        <title>Beer-o-Meter</title>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<link href="favicon.ico" rel="shortcut icon" type="image/x-icon">
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" />
		<style>
			.panel-body {
				overflow: auto;
			}
			.add-hover {
				cursor: pointer;
				border-top: 1px solid #eee;
			}
			.add-hover:hover {
				background: #efe;
			}
		</style>
    </head>
    <body>  
        <div class="container-fluid" ng-view resizable></div>
        <script>
            /* Needed for IE8-support */
            if(typeof String.prototype.trim !== 'function') {
              String.prototype.trim = function() {
                return this.replace(/^\s+|\s+$/g, '');
              }
            }               
        </script>
        <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.min.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular-resource.min.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular-route.min.js"></script>
		<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular-sanitize.min.js"></script>
        <script src="scripts/vendor/angular-translate.min.js"></script>
        <script src="scripts/vendor/angular-translate-loader-url.min.js"></script>
        <script src="scripts/vendor/ui-bootstrap-tpls-0.10.0.min.js"></script>
        <script src="scripts/services/services.js"></script>
		<script src="scripts/directives/directives.js"></script>
        <script src="scripts/controllers/controllers.js"></script>		

		<script>
		  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

		  ga('create', 'UA-50916660-1', 'ebcu.org');
		  ga('send', 'pageview');

		</script>		
    </body>
</html>