<%@ include file="/WEB-INF/jsp/header.jsp"%>

<h2>Admin Page</h2>

<c:if test="${not empty message}">
  <font color="blue"><c:out value="${message}" /></font><br/>
</c:if>
<pre>
<form action="populateTestSwagItems">
Populate Test Swag Items:
How many? <input type="text" name="numberOfSwagItems" size="5" /> <input type="submit" value="populate" />
</form>
<hr/>
<form action="deleteTestSwagItems">
Delete Test Swag Items: <input type="submit" value="delete" />
</form>
</pre>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>