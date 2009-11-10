<%@ include file="/WEB-INF/jsp/header.jsp"%>

<h2>View Swag Stats</h2>
<table>
  <tr>
    <td>
        Top Rated:
        <display:table style="width:1px;" name="swagStats.topRated" uid="swagItemsList" id="topRated_current" 
                       requestURI="/springmvc/viewSwagStats" keepStatus="true">
        	<display:column sortable="true" property="name"/>
        		<display:column title="Action" >
        			<a href="<c:url value='/springmvc/view/${topRated_current.key}'/>"> 
        				<img border="0" alt="View/Comment/Rate" src="<%=request.getContextPath()%>/images/view.gif"/>
        			</a>
        			<%-- Users can only edit their own items --%>
        			<google-auth:isAllowed swagItemOwnerGoogleID="${topRated_current.ownerGoogleID}">
        				
        				<a href="<c:url value='/springmvc/edit/${topRated_current.key}'/>"> 
        					<img border="0" alt="Edit" src="<%=request.getContextPath()%>/images/edit.gif"/>
        				</a>
        			  
        		        <a href="<c:url value='/springmvc/delete/${topRated_current.key}'/>" onclick="return confirmSubmit()"> 
        		            <img border="0" alt="Delete" src="<%=request.getContextPath()%>/images/delete.gif"/>
        				</a>
        			</google-auth:isAllowed>
        		</display:column>
        
        	<%-- Ratings --%>
        	<display:column title="My Rating" >
        		<google-auth:isLoggedIn>
        		<a name="${topRated_current.key}"></a>
        		<form:form action="/springmvc/rate#${topRated_current.key}" commandName="newRating-${topRated_current.key}" name="rateForm${topRated_current.key}" method="get">
        			<form:hidden path="swagItemKey" />
        			<!-- can't use spring form:hidden tag here cause if userRating is empty, GAE blows up. 
        		     Spring seems to be doing something not-kosher for GAE when it populates a default value -->
             		<input type="hidden" name="userRating" /> <!-- this is used for javascript trick below -->
             		<center>
        			<swagItemRating:showRatingStars 
        				rateFormName="rateForm${topRated_current.key}" 
        				swagSwapUser="${swagSwapUser}" 
        				swagItemKey="${topRated_current.key}"/>
        			</center>
        		</form:form>
        		</google-auth:isLoggedIn>
        		<google-auth:isNotLoggedIn>
        			<a href="${loginUrl}" border="0">
        			<img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/>
        			</a>
        		</google-auth:isNotLoggedIn>
        	</display:column>
        	
        	<%-- 
        	<display:column property="tags" decorator="com.swagswap.web.springmvc.displaytag.TagsDecorator"/>
        	--%>
        	<c:if test="${not empty topRated_current.imageKey}">
        		<display:column title="Image">
                    <a href="<c:url value='/springmvc/view/${topRated_current.key}'/>">
        			<img border="0" alt="Image" height="50" width="66" src="<c:url value='/springmvc/showThumbnail/${topRated_current.imageKey}'/>"/>
        		</a>
                </display:column>
        	</c:if>
        	<display:column property="averageRating" title="Avg Rating" sortable="true"/>
        </display:table>

    </td>
    <td>
        Most Rated:
        <display:table style="width:1px;" name="swagStats.topRated" uid="swagItemsList" id="mostRated_current" 
                       requestURI="/springmvc/viewSwagStats" keepStatus="true">
        	<display:column sortable="true" property="name"/>
        		<display:column title="Action" >
        			<a href="<c:url value='/springmvc/view/${mostRated_current.key}'/>"> 
        				<img border="0" alt="View/Comment/Rate" src="<%=request.getContextPath()%>/images/view.gif"/>
        			</a>
        			<%-- Users can only edit their own items --%>
        			<google-auth:isAllowed swagItemOwnerGoogleID="${mostRated_current.ownerGoogleID}">
        				
        				<a href="<c:url value='/springmvc/edit/${mostRated_current.key}'/>"> 
        					<img border="0" alt="Edit" src="<%=request.getContextPath()%>/images/edit.gif"/>
        				</a>
        			  
        		        <a href="<c:url value='/springmvc/delete/${mostRated_current.key}'/>" onclick="return confirmSubmit()"> 
        		            <img border="0" alt="Delete" src="<%=request.getContextPath()%>/images/delete.gif"/>
        				</a>
        			</google-auth:isAllowed>
        		</display:column>
        
        	<%-- Ratings --%>
        	<display:column title="My Rating" >
        		<google-auth:isLoggedIn>
        		<a name="${mostRated_current.key}"></a>
        		<form:form action="/springmvc/rate#${mostRated_current.key}" commandName="newRating-${mostRated_current.key}" name="rateForm${mostRated_current.key}" method="get">
        			<form:hidden path="swagItemKey" />
        			<!-- can't use spring form:hidden tag here cause if userRating is empty, GAE blows up. 
        		     Spring seems to be doing something not-kosher for GAE when it populates a default value -->
             		<input type="hidden" name="userRating" /> <!-- this is used for javascript trick below -->
             		<center>
        			<swagItemRating:showRatingStars 
        				rateFormName="rateForm${mostRated_current.key}" 
        				swagSwapUser="${swagSwapUser}" 
        				swagItemKey="${mostRated_current.key}"/>
        			</center>
        		</form:form>
        		</google-auth:isLoggedIn>
        		<google-auth:isNotLoggedIn>
        			<a href="${loginUrl}" border="0">
        			<img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/>
        			</a>
        		</google-auth:isNotLoggedIn>
        	</display:column>
        	
        	<%-- 
        	<display:column property="tags" decorator="com.swagswap.web.springmvc.displaytag.TagsDecorator"/>
        	--%>
        	<c:if test="${not empty mostRated_current.imageKey}">
        		<display:column title="Image">
                    <a href="<c:url value='/springmvc/view/${mostRated_current.key}'/>">
        			<img border="0" alt="Image" height="50" width="66" src="<c:url value='/springmvc/showThumbnail/${mostRated_current.imageKey}'/>"/>
        		</a>
                </display:column>
        	</c:if>
        	<display:column property="numberOfRatings" title="No. Ratings" sortable="true"/>
        </display:table>

    </td>
    <td>
        Most Commented:
        <display:table style="width:1px;" name="swagStats.topRated" uid="swagItemsList" id="mostCommented_current" 
                       requestURI="/springmvc/viewSwagStats" keepStatus="true">
        	<display:column sortable="true" property="name"/>
        		<display:column title="Action" >
        			<a href="<c:url value='/springmvc/view/${mostCommented_current.key}'/>"> 
        				<img border="0" alt="View/Comment/Rate" src="<%=request.getContextPath()%>/images/view.gif"/>
        			</a>
        			<%-- Users can only edit their own items --%>
        			<google-auth:isAllowed swagItemOwnerGoogleID="${mostCommented_current.ownerGoogleID}">
        				
        				<a href="<c:url value='/springmvc/edit/${mostCommented_current.key}'/>"> 
        					<img border="0" alt="Edit" src="<%=request.getContextPath()%>/images/edit.gif"/>
        				</a>
        			  
        		        <a href="<c:url value='/springmvc/delete/${mostCommented_current.key}'/>" onclick="return confirmSubmit()"> 
        		            <img border="0" alt="Delete" src="<%=request.getContextPath()%>/images/delete.gif"/>
        				</a>
        			</google-auth:isAllowed>
        		</display:column>
        
        	<%-- Ratings --%>
        	<display:column title="My Rating" >
        		<google-auth:isLoggedIn>
        		<a name="${mostCommented_current.key}"></a>
        		<form:form action="/springmvc/rate#${mostCommented_current.key}" commandName="newRating-${mostCommented_current.key}" name="rateForm${mostCommented_current.key}" method="get">
        			<form:hidden path="swagItemKey" />
        			<!-- can't use spring form:hidden tag here cause if userRating is empty, GAE blows up. 
        		     Spring seems to be doing something not-kosher for GAE when it populates a default value -->
             		<input type="hidden" name="userRating" /> <!-- this is used for javascript trick below -->
             		<center>
        			<swagItemRating:showRatingStars 
        				rateFormName="rateForm${mostCommented_current.key}" 
        				swagSwapUser="${swagSwapUser}" 
        				swagItemKey="${mostCommented_current.key}"/>
        			</center>
        		</form:form>
        		</google-auth:isLoggedIn>
        		<google-auth:isNotLoggedIn>
        			<a href="${loginUrl}" border="0">
        			<img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/><img border="0" src="/images/starOff.gif"/>
        			</a>
        		</google-auth:isNotLoggedIn>
        	</display:column>
        	
        	<%-- 
        	<display:column property="tags" decorator="com.swagswap.web.springmvc.displaytag.TagsDecorator"/>
        	--%>
        	<c:if test="${not empty mostCommented_current.imageKey}">
        		<display:column title="Image">
                    <a href="<c:url value='/springmvc/view/${mostCommented_current.key}'/>">
        			<img border="0" alt="Image" height="50" width="66" src="<c:url value='/springmvc/showThumbnail/${mostCommented_current.imageKey}'/>"/>
        		</a>
                </display:column>
        	</c:if>
        	<display:column property="numberOfComments" title="No. Comments" sortable="true"/>
        </display:table>

    </td>
  </tr>

  <tr>
    <td>
      <img src="http://chart.apis.google.com/chart?chs=400x100&${swagStats.googleChartsUrlItemsCreated}"/>
     </td>
    <td>
      <img src="http://chart.apis.google.com/chart?chs=400x100&${swagStats.googleChartsUrlItemsRated}"/>
     </td>
    <td>
      <img src="http://chart.apis.google.com/chart?chs=400x100&${swagStats.googleChartsUrlItemsCommented}"/>
     </td>
  </tr>
</table>


<%@ include file="/WEB-INF/jsp/footer.jsp"%>