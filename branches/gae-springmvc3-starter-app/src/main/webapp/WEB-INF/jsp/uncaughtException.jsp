<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<%--Borrowed from Spring Pet Clinic --%>

<h2/>Error</h2>
<p/>

<% 
try {
	// The Servlet spec guarantees this attribute will be available
	Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception"); 

	if (exception != null) {
		if (exception instanceof ServletException) {
			// It's a ServletException: we should extract the root cause
			ServletException sex = (ServletException) exception;
			Throwable rootCause = sex.getRootCause();
			if (rootCause == null)
				rootCause = sex;
			out.println("** Root cause is: "+ rootCause.getMessage());
			rootCause.printStackTrace(new java.io.PrintWriter(out)); 
		}
		else if (exception instanceof org.springframework.web.multipart.MultipartException) {
			out.println("File upload is too large.  <a href=\"\" onclick=\"history.go(-1);return false;\">Try again</a>");
		}
		else {
			// It's not a ServletException
			Throwable rootCause = exception.getCause();
			if (rootCause == null)
				rootCause = exception;
			out.println("** Root cause is: "+ rootCause.getMessage());
			rootCause.printStackTrace(new java.io.PrintWriter(out)); 
		}
	} 
	else  {
    	out.println("No error information available");
	} 

	    
} catch (Exception ex) { 
	ex.printStackTrace(new java.io.PrintWriter(out));
}
%>

<p/>
<br/>


<%@ include file="/WEB-INF/jsp/footer.jsp" %>
