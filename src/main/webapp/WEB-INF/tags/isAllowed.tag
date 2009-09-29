<%-- JSP Tag File: See http://java.sun.com/j2ee/1.4/docs/tutorial/doc/JSPTags5.html --%>
<%-- or http://today.java.net/pub/a/today/2003/11/14/tagfiles.html --%>
<%@ tag import="com.google.appengine.api.users.*" %> 
<%@ attribute name="swagItemOwnerEmail" required="true" %>

<% UserService userService = UserServiceFactory.getUserService(); %>
<c:if test="<%= userService.isUserLoggedIn()%>">
	<c:set var="curentUserEmail" scope="page" value="<%= userService.getCurrentUser().getEmail()%>"/>
	<c:if test="${currentUserEmail == swagItemOwnerEmail}">
		<jsp:doBody/>
	</c:if>
</c:if>
