<%@ include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery('#rescheduledDate').datepicker({yearRange:'c-30:c+30', dateFormat: 'dd/mm/yy', changeMonth: true, changeYear: true});
    });
</script>

<style>
	.info {
		font-weight: bold;
		text-align:right
	}
</style>

<center>
	<c:choose>
		<c:when test="${not empty test}">
			<table class="testInfo" cellspacing="15">
				<tr>
					<td class='info'>Patient Name</td>
					<td>${test.patientName}</td>
					<td></td>
					<td class='info'>Patient Identifier</td>
					<td>${test.patientIdentifier}</td>
				</tr>
				<tr>
					<td class='info'>Date</td>
					<td>${test.startDate}</td>
					<td width="30px"></td>
					<td class='info'>Test name</td>
					<td>${test.testName}</td>
				</tr>
				<tr>
					<td class='info'>Gender</td>
					<td>${test.gender}</td>
					<td></td>
					<td class='info'>Age</td>
					<td>${test.age}</td>
				</tr>
			</table>
			<b>Reorder Date</b>: <input id="rescheduledDate" value="${currentDate}" style="text-align:right;"/>
			<input type="button" onClick="javascript:window.parent.rescheduleTest(${test.orderId}, $('#rescheduledDate').val()); tb_remove();" value="Reorder" />
			<input type="button" onClick="tb_remove();" value="Cancel" />
		</c:when>
		<c:otherwise>
			Not found<br />
			<input type="button" onClick="tb_remove();" value="Cancel" />
		</c:otherwise>
	</c:choose>
</center>