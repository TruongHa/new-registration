<%@ include file="/WEB-INF/template/include.jsp" %>
{
<c:forEach var="entry" items="${json}" varStatus="status">
	"${entry.key}":"${entry.value}"<c:if test="${not status.last}">,</c:if>
	
</c:forEach>
}