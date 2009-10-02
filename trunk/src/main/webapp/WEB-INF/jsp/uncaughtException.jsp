<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<%--Borrowed from Spring Pet Clinic --%>

<h2/>Error</h2>
<p/>

<% 
try {
	String pleaseLogBugText = "<b>Please <a href=\"http://code.google.com/p/swagswap/issues/entry?template=Defect%20report%20from%20user\">report this issue</a> (requires a google account)</b><br/><br/>";
	// The Servlet spec guarantees this attribute will be available
	Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception"); 

	if (exception != null) {
		//Custom exception handling
		if (exception instanceof org.springframework.web.multipart.MultipartException) {
			out.println("File upload is too large.  <a href=\"\" onclick=\"history.go(-1);return false;\">Try again</a>");
		}
		else if (exception instanceof javax.jdo.JDOObjectNotFoundException) {
			out.println("Can't help you buddy.  You're barking up the wrong tree. "+
					"<a href=\"/\">Return to Swagswap</a>");
		}
		//Standard exception handling
		else if (exception instanceof java.security.AccessControlException) {
			java.security.AccessControlException ace = (java.security.AccessControlException) exception;
			Throwable cause = ace.getCause();
			out.println(pleaseLogBugText);
			out.println("** Root cause is: "+ cause.getMessage());
			cause.printStackTrace(new java.io.PrintWriter(out)); 
		}
		else if (exception instanceof ServletException) {
			// It's a ServletException: we should extract the root cause
			ServletException sex = (ServletException) exception;
			Throwable rootCause = sex.getRootCause();
			if (rootCause == null)
				rootCause = sex;
			out.println(pleaseLogBugText);
			out.println("** Root cause is: "+ rootCause.getMessage());
			rootCause.printStackTrace(new java.io.PrintWriter(out)); 
			
		}
		else {
			// It's not a ServletException
			Throwable rootCause = exception.getCause();
			if (rootCause == null)
				rootCause = exception;
			out.println(pleaseLogBugText);
			out.println("** Root cause is: "+ rootCause.getMessage());
			rootCause.printStackTrace(new java.io.PrintWriter(out)); 
		}
	} 
	else  {
    	out.println("No error information available");
    	out.println(pleaseLogBugText);
	} 

	    
} catch (Exception ex) { 
	ex.printStackTrace(new java.io.PrintWriter(out));
}
%>

<p/>
<br/>


<%@ include file="/WEB-INF/jsp/footer.jsp" %>
