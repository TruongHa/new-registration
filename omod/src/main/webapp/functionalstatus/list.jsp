<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<br/>
<openmrs:require privilege="Manage Laboratory" otherwise="/login.htm" redirect="/module/laboratory/listForm.form" />
<%@ include file="../localHeader.jsp" %>
<script type="text/javascript">
	$(document).ready(function() {
		$("#myTable").tablesorter({sortList: [[0,0]]});
    });	
	
	// enable/disable a test
	function toggleTest(serviceId){
		serviceIds = $('#serviceIds').val();
		if(serviceIds.indexOf("<" + serviceId + ">")>=0){
			serviceIds = serviceIds.replace("<" + serviceId + ">", "");
		} else {
			serviceIds += "<" + serviceId + ">"
		}
		$('#serviceIds').val(serviceIds);
	}
</script> 

<table id='myTable' class='tablesorter' style='width: 500px;'>
	<thead>
		<tr> 
			<th>Test</th>
			<th>Disabled</th>
		</tr>
	</thead>
	<tbody>

<c:forEach var='bs' items='${billableServices}'>
		<tr>
			<td>${bs.name}</td>
			<td>
				<center>					
					<input id='cb${bs.serviceId}' type='checkbox' onClick='toggleTest(${bs.serviceId});' <c:if test='${bs.disable}'>checked='checked'</c:if>/>
				</center>				
			</td>
		</tr>	
</c:forEach>		
	</tbody>
</table>
<form method="POST">	
	<input type='hidden' id='serviceIds' name='serviceIds' value=''/>	
	<input type='submit' value='Save'/>	
</form>	



<%@ include file="/WEB-INF/template/footer.jsp" %>  