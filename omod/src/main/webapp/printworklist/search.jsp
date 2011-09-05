<%@ include file="/WEB-INF/template/include.jsp" %>
<script type="text/javascript">
	testNo = ${testNo};
</script>
<table id="myTable" class="tablesorter">
	<thead>
		<tr> 
			<th>Order Date</th>
			<th>Patient Identifier</th>
			<th>Sample No.</th>
			<th>Age</th>
			<th>Gender</th>
			<th>Name</th>
			<th>Test</th>
			<th>Test name</th>
			<th>Enter Result</th>		
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
				<td>
					${test.acceptedDate}
				</td>
				<td>
					${test.patientIdentifier}
				</td>
				<td>${test.sampleId}</td>
				<td>
					${test.age}
				</td>
				<td>
					${test.gender}
				</td>
				<td>
					${test.patientName}
				</td>
				<td>
					${test.testName}
				</td>
				<td>
					${test.investigation} 					
				</td>
				<td>
					 					
				</td>
			</tr>	
		</c:forEach>
	</tbody>
</table>

<div id="printArea" style="display: none;">
	<style>
		table.wltable {
			margin-left: auto;
			margin-right: auto;
			font-family: Verdana, 'Lucida Grande', 'Trebuchet MS', Arial, Sans-Serif;
			font-style: normal;
			font-size: 10px;
			border: 1px solid;
		}

		table.wltable th {
			border-bottom: 1px solid;
		}

		table.wltable td {
			padding: 5px 5px 0px 5px;
		}

		table.wltable .right {
			border-right: 1px solid;
		}
	</style>
	<table class="wltable" cellspacing="0">
		<tr> 
			<th class="right">Order Date</th>
			<th class="right">Patient ID</th>
			<th class="right">Sample No.</th>
			<th class="right">Age</th>
			<th class="right">Gender</th>
			<th class="right">Name</th>
			<th class="right">Test</th>
			<th class="right">Test name</th>
			<th style="width: 200px;">Enter Result</th>		
		</tr>
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
				<td class="right">
					${test.acceptedDate}
				</td>
				<td class="right">
					${test.patientIdentifier}
				</td>
				<td class="right">${test.sampleId}</td>
				<td class="right">
					${test.age}
				</td>
				<td class="right">
					${test.gender}
				</td>
				<td class="right">
					${test.patientName}
				</td>
				<td class="right">
					${test.testName}
				</td>
				<td class="right">
					${test.investigation} 					
				</td>
				<td>
					 					
				</td>
			</tr>	
		</c:forEach>
	</table>
</div>
