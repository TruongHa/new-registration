<%@ include file="/WEB-INF/template/include.jsp" %>
<hr/>
<div id="patientReportTestInfo"></div>
<table class="tablesorter">
	<thead>
		<tr>		
			<th>Test</th>
			<th>Result</th>
			<th>Unit</th>
			<th>Reference range</th> 
		</tr>
	</thead>	
	<tbody>
<c:forEach var="test" items="${tests}">
	<tr>		
		<td>
			<c:if test="${test.level eq 'LEVEL_INVESTIGATION'}"><b>${test.investigation}</b></c:if>
			<c:if test="${test.level eq 'LEVEL_SET'}">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				${test.set}
			</c:if>
			<c:if test="${test.level eq 'LEVEL_TEST'}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${test.test}</c:if>  
		</td>
		<td>${test.value}</td>
		<td>${test.unit}</td>
		<td>
			${test.lowNormal}
			<c:if test="${not empty test.lowNormal and not empty test.hiNormal}">||</c:if>
			${test.hiNormal}
		</td>
	</tr>	
</c:forEach>
	</tbody>
</table>

<div id="patientReportPrintArea" style="display: none;">
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
	<div id="printAreaTestInfo"></div>
	<table class="wltable" cellspacing="0">
		<thead>
			<tr>		
				<th class="right">Test</th>
				<th class="right">Result</th>
				<th class="right">Unit</th>
				<th>Reference range</th>
			</tr>
		</thead>	
		<tbody>
	<c:forEach var="test" items="${tests}">
		<tr>		
			<td class="right">&nbsp;
				<c:if test="${test.level eq 'LEVEL_INVESTIGATION'}"><b>${test.investigation}</b></c:if>
				<c:if test="${test.level eq 'LEVEL_SET'}">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${test.set}
				</c:if>
				<c:if test="${test.level eq 'LEVEL_TEST'}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${test.test}</c:if>  
			</td>
			<td class="right">${test.value}&nbsp;</td>
			<td class="right">${test.unit}&nbsp;</td>
			<td>&nbsp;
				${test.lowNormal}
				<c:if test="${not empty test.lowNormal and not empty test.hiNormal}">||</c:if>
				${test.hiNormal}
			</td>
		</tr>	
	</c:forEach>
		</tbody>
	</table>
</div>