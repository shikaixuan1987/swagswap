<%@ include file="/WEB-INF/jsp/header.jsp" %>
<%--Shows a search box and a list of SwagItems using the old but good displaytag library --%>

<form:form action="/swag/search" commandName="searchCriteria" name="searchForm" method="GET">

	<c:if test="${not empty searchCriteria.searchString}">
		<h2>Swag Item Search: ${searchCriteria.searchString}</h2>
	</c:if>
	<c:if test="${empty searchCriteria.searchString}">
		<h2>All Swag Items</h2>
	</c:if>
	
	Note: Search is case sensitive, full word, and only on Name and Tags in this implementation
	<table>
		<tr>
			<td><form:input path="searchString" />
				<a href="#" onclick="document.searchForm.submit()">
				<img src="/images/icon_flashlight.gif" border="0"/>Search Swag</a>
				&nbsp; 
				<a href="/swag/add"><img src="/images/newAdd.png" 
				   title="Add SwagItem" border="0"/>Add Swag</a>
				&nbsp; 
				<google-auth:loginLogoutTag requestURL="/swag/search"/>
			</td>
		</tr>
	</table>
</form:form>

<display:table name="swagItems" uid="swagItemsList" id="currentObject" 
               requestURI="/swag/search" keepStatus="true">
	<display:column sortable="true" property="name" />
		<display:column title="Action" >
			<a href="<c:url value='/swag/view/${currentObject.key}'/>"> 
				<img border="0" alt="View/Comment/Rate" src="<%=request.getContextPath()%>/images/view.gif"/>
			</a>
			<%-- Users can only edit their own items --%>
			<google-auth:isAllowed swagItemOwnerEmail="${currentObject.ownerEmail}">
				
				<a href="<c:url value='/swag/edit/${currentObject.key}'/>"> 
					<img border="0" alt="Edit" src="<%=request.getContextPath()%>/images/edit.gif"/>
				</a>
			  
		        <a href="<c:url value='/swag/delete/${currentObject.key}'/>" onclick="return confirmSubmit()"> 
		            <img border="0" alt="Delete" src="<%=request.getContextPath()%>/images/delete.gif"/>
				</a>
			</google-auth:isAllowed>
		</display:column>

	<%-- Ratings --%>
	<display:column title="Your Rating" >
		<google-auth:isLoggedIn>
		<%-- HTML anchor to compensate for web 1.0 implementation --%>
		<a name="${currentObject.key}"></a>
		<form:form action="/swag/rate#${currentObject.key}" commandName="newRating-${currentObject.key}" name="rateForm${currentObject.key}" method="get">
			<form:hidden path="swagItemKey" />
			<!-- can't use spring form:hidden tag here cause if userRating is empty, GAE blows up. 
		     Spring seems to be doing something not-kosher for GAE when it populates a default value -->
     		<input type="hidden" name="userRating" /> <!-- this is used for javascript trick below -->
     		<center>
			<swagItemRating:showRatingStars 
				rateFormName="rateForm${currentObject.key}" 
				swagSwapUser="${swagSwapUser}" 
				swagItemKey="${currentObject.key}"/>
			</center>
		</form:form>
		</google-auth:isLoggedIn>
		<google-auth:isNotLoggedIn><%--show them a teaser with a link to the login page --%>
			<a href="${loginUrl}" border="0">
			<img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/>
			</a>
		</google-auth:isNotLoggedIn>
	</display:column>
	
	<display:column property="tags" decorator="com.swagswap.web.springmvc.displaytag.TagsDecorator"/>

	<c:if test="${not empty currentObject.imageKey}">
		<display:column title="Image">
			<img border="0" alt="Image" height="100" width="100" src="<c:url value='/swag/showImage/${currentObject.imageKey}'/>"/>
		</display:column>
	</c:if>
	<display:column sortable="true" title="Owner" property="ownerNickName" />
	<display:column sortable="true" property="company" />
	<display:column property="lastUpdated" title="Last Updated" format="{0,date,dd-MM-yyyy HH:mm:ss}" sortable="true"/>
	<display:column property="numberOfRatings" title="Number of Rating" sortable="true"/>
	<display:column property="averageRating" title="Average Rating" sortable="true"/>
	
</display:table>

<script language="JavaScript">
    document.forms[0].searchString.focus();
    document.forms[0].searchString.select();

	function confirmSubmit(name)
	{
	var agree=confirm("Are you sure you want to delete this item?");
	if (agree)
		return true ;
	else
		return false ;
	}

</script>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>