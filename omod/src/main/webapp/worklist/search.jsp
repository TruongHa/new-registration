<%@ include file="/WEB-INF/template/include.jsp" %>
<script type="text/javascript">
	testNo = ${testNo};
</script>
<table id="myTable" class="tablesorter">
	<thead>
		<tr> 
			<th>Sr. No.</th>
			<th>Sample No.</th>
			<th>Results</th>
			<th>Reorder</th>	
			<th>Date</th>
			<th>Patient ID</th>
			<th>Name</th>
			<th>Gender</th>
			<th>Age</th>
			<th>Test</th>					
		</tr>
	</thead>
	<tbody>
		<c:forEach var="test" items="${tests}" varStatus="index">
			<c:choose>
				<c:when test="${index.count mod 2 == 0}">
					<c:set var="klass" value="odd"/>
				</c:when>					
				<c:otherwise>
					<c:set var="klass" value="even"/>
				</c:otherwise>
			</c:choose>
			<tr class="${klass}">
				<td>${index.count}</td>
				<td>${test.sampleId}</td>
				<td>
					<a href="javascript:enterResult(${test.testId});">
						Enter results
					</a>
				</td>
				<td>
					<a href="rescheduleTest.form?type=reorder&orderId=${test.orderId}&modal=true&height=200&width=800" class="thickbox" title="Reschedule test">Reorder</a> 					
				</td>
				<td>
					${test.startDate}
				</td>
				<td>
					${test.patientIdentifier}
				</td>
				<td>
					${test.patientName}
				</td>
				<td>
					${test.gender}
				</td>
				<td>
					${test.age}
				</td>
				<td>
					${test.testName}
				</td>
				
			</tr>	
			<tr class='resultParameter' id='row${test.testId}' style='display:none;'>
				<td colspan='10'>
					<form id="contentForm${test.testId}" method="post" action="showForm.form">
						
					</form>
					<div style='clear: both;'></div>
					<input type='button' value='Save' onClick='submit(${test.testId});'/>
					<input type='button' value='Cancel' onClick='jQuery("#row${test.testId}").hide();'/>						
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>