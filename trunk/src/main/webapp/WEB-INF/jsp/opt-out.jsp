<%@ include file="/WEB-INF/jsp/header.jsp"%>

<h2>Opt-out</h2>

<c:if test="${not empty message}">
  <font color="blue"><c:out value="${message}" /></font><br/>
</c:if>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>