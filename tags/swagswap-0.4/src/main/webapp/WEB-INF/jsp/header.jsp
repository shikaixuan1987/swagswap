<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <link rel="stylesheet" href="<spring:url value="/css/swagswap.css" htmlEscape="true" />" type="text/css"/>
  <link rel="stylesheet" href="<spring:url value="/css/maven-base.css" htmlEscape="true" />" type="text/css"/>
  <link rel="stylesheet" href="<spring:url value="/css/maven-theme.css" htmlEscape="true" />" type="text/css"/>
  <link rel="stylesheet" href="<spring:url value="/css/site.css" htmlEscape="true" />" type="text/css"/>
  <link rel="stylesheet" href="<spring:url value="/css/screen.css" htmlEscape="true" />" type="text/css"/>
  <link rel="stylesheet" href="<spring:url value="/css/teststyles.css" htmlEscape="true" />" type="text/css"/>
  <link rel="stylesheet" href="<spring:url value="/css/alternative.css" htmlEscape="true" />" type="text/css"/>
  <title>Swag Swap</title>	
  <link rel="shortcut icon" href="/images/favicon.ico" >
  <link rel="icon" type="image/gif" href="/images/animated_favicon1.gif" >
  <script type="text/javascript" src="/js/prototype.js"></script>
  <script type="text/javascript" src="/js/swagswap.js"></script>
  
  	<%-- This is to show cost and usage stats from liveHttpHeaders --%>
	<google-auth:isAdmin>
	   	<script>
	   	var costSummary;
	   	var totalCost;

	   	//function to remove duplicates from an array
	   	//see http://www.martienus.com/code/javascript-remove-duplicates-from-array.html
		function unique(a)
		{
		   var r = new Array();
		   o:for(var i = 0, n = a.length; i < n; i++)
		   {
		      for(var x = 0, y = r.length; x < y; x++)
		      {
		         if(r[x].src==a[i].src) continue o;
		      }
		      r[r.length] = a[i];
		   }
		   return r;
		}
	   	
	   	function submitRequest(url)
	   	{
		   	new Ajax.Request(url, {
		   		method: 'get',asynchronous:false, 
		   	  onSuccess: function(response) {
			   	  
		   	      costSummary=costSummary + '\r\n\r\nURL: ' + url + ' Cost: ' + response.getHeader('X-AppEngine-Estimated-CPM-US-Dollars') + ' Usage: ' + response.getHeader('X-AppEngine-Resource-Usage');
	 	      	  var cost = response.getHeader('X-AppEngine-Estimated-CPM-US-Dollars').substr(1);
	 	      	  totalCost=totalCost + parseFloat(cost);
		   	  }
		   	});
	   	}   
	
	   	function showTotalUsage() {
		 	totalCost = 0.0;
		 	costSummary='';
		 	submitRequest(location.href);
		 	var uniqueImages = unique(document.images); //browser only asks for an image once
		 	for(i=0;i<uniqueImages.length;i++){
		 	   	submitRequest(uniqueImages[i].src);
		 	}
		 	alert('Total Cost = ' + totalCost +' Usage Summary: ' + costSummary);
	   	}
	   	</script>
	</google-auth:isAdmin>
</head>


<%--Google Analytics (only for swagswap, you can put your own here if deploying yourself --%>
<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-7317033-11");
pageTracker._trackPageview();
} catch(err) {}</script>

<body>

<div id="mainContent">
<img src="<spring:url value="/images/trading-floor-banner.jpg" htmlEscape="true" />" alt="Swag Swap"/>

<table border="1">
<google-auth:isAdmin>
  <br/>
  <tr><td>Welcome Admin: <a href="/swag/admin/main">Go to Admin Page</a> | <a href="#"  onclick="showTotalUsage();">Show GAE Usage for this page</a> (doesn't work in dev env)</td></tr>
</google-auth:isAdmin>
<tr><td>
<h3>Swagswap Devoxx 2009 Demo application (work in progress)</h3>

<ul>
	<li>Read about it and run it yourself with these <a href="http://code.google.com/p/swagswap/wiki/Instructions">instructions</a></li> 
	<li>Visit our <a href="http://cloudmouthdevoxx.blogspot.com/">cloudMouth blog</a></li> 
	<li>Attend our <a href="http://www.devoxx.com/display/DV09/Google+App+Engine+for+Java+-+a+real+live+voyage+to+The+Cloud">Devoxx session</a> (November 17th at 9:30)</li> 
</ul>
--<a href="http://www.devoxx.com/display/DV09/Sam+Brodkin">Sam Brodkin</a> and <a href="http://www.devoxx.com/display/DV09/Scott+Stevenson">Scott Stevenson</a>
</td></tr>
</table>

