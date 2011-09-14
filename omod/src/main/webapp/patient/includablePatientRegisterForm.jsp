<style>
	.cell {
		border-top: 1px solid lightgrey;
		padding: 20px;
	}
</style>
<script type="text/javascript">

	jQuery(document).ready(function(){
		jQuery("#patientRegistrationForm").fillForm("patient.identifier==" + MODEL.patientIdentifier);			
		jQuery('#birthdate').datepicker({yearRange:'c-100:c+100', dateFormat: 'dd/mm/yy', changeMonth: true, changeYear: true});	
		jQuery('#birthdate').change(PAGE.checkBirthDate);
		PAGE.fillOptions("#districts", {
			data:MODEL.districts
		});		
		PAGE.fillOptions("#tehsils", {
			data:MODEL.tehsils[0].split(',')
		});				
		PAGE.fillOptions("#opdWard", {
			data:MODEL.OPDs,
			delimiter: ",",
			optionDelimiter: "|"
		});						
		PAGE.fillOptions("#referralHospitals", {
			data:MODEL.referralHospitals,
			delimiter: ",",
			optionDelimiter: "|"
		});	
		PAGE.fillOptions("#referralReasons	", {
			data:MODEL.referralReasons	,
			delimiter: ",",
			optionDelimiter: "|"
		});	
		
		jQuery("#searchbox").showPatientSearchBox({			
			resultView: "/module/registration/patientsearch/registration",
			success: function(data){
				PAGE.searchPatientSuccess(data);
			},
			beforeNewSearch: PAGE.searchPatientBefore
		});
	});
	
	/**
	 ** FORM
	 **/
	PAGE = {
		/** SUBMIT */
		submit: function(){						
			
			jQuery("#patientName", jQuery("#patientRegistrationForm")).val(jQuery("#nameOrIdentifier", jQuery("#patientSearchForm")).val());
			
			jQuery("#patientRegistrationForm").submit();				
		},
		
		/** VALIDATE BIRTHDATE */
		checkBirthDate: function() {
			jQuery.ajax({
				type : "GET",
				url : getContextPath() + "/module/registration/ajax/processPatientBirthDate.htm",
				data : ({
					birthdate: $("#birthdate").val()
				}),
				dataType: "json",
				success : function(json) {
					
					if(json.estimated == "true"){
						jQuery("#birthdateEstimated").val("true")
					} else {						
						jQuery("#birthdateEstimated").val("false");
					}
					
					jQuery("#estimatedAge").html(json.age);
					jQuery("#birthdate").val(json.birthdate);
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert(thrownError);
				}
			});
		},
		
		/** FILL OPTIONS INTO SELECT 
		 * option = {
		 * 		data: list of values or string
		 *		index: list of corresponding indexes
		 *		delimiter: seperator for value and label
		 *		optionDelimiter: seperator for options
		 * }
		 */
		fillOptions: function(divId, option) {
			jQuery(divId).empty();
			if(option.delimiter == undefined){
				if(option.index == undefined){
					jQuery.each(option.data, function(index, value){	
						if(value.length>0){
							jQuery(divId).append("<option value='" + value + "'>" + value + "</option>");
						}
					});				
				} else {
					jQuery.each(option.data, function(index, value){	
						if(value.length>0){
							jQuery(divId).append("<option value='" + option.index[index] + "'>" + value + "</option>");
						}
					});
				}
			} else {
				options = option.data.split(option.optionDelimiter);
				jQuery.each(options, function(index, value){
					values = value.split(option.delimiter);
					optionValue = values[0];
					optionLabel = values[1];
					if(optionLabel != undefined){
						if(optionLabel.length>0){
							jQuery(divId).append("<option value='" + optionValue + "'>" + optionLabel + "</option>");
						}
					}
					
				});
			}
		},
		
		/** CHANGE DISTRICT */
		changeDistrict: function() {		

			// get the list of tehsils
			tehsilList = "";
			selectedDistrict = jQuery("#districts option:checked").val();
			jQuery.each(MODEL.districts, function(index, value){
				if(value == selectedDistrict){
					tehsilList = MODEL.tehsils[index];					
				}
			});
			
			// fill tehsils into tehsil dropdown
			this.fillOptions("#tehsils", {
				data: tehsilList.split(",")
			});
		},
		
		/** SHOW OR HIDE REFERRAL INFO */
		toogleReferralInfo: function(obj){
			checkbox = jQuery(obj);
			if(checkbox.is(":checked")){
				jQuery("#referralDiv").show();
			} else {
				jQuery("#referralDiv").hide();
			}
		},
		
		/** CALLBACK WHEN SEARCH PATIENT SUCCESSFULLY */
		searchPatientSuccess: function(data){			
			jQuery("#numberOfFoundPatients").html("Similar patients: " + data.totalRow + "(<a href='javascript:PAGE.togglePatientResult();'>show/hide</a>)");
		},
		
		/** CALLBACK WHEN BEFORE SEARCHING PATIENT */
		searchPatientBefore: function(data){
			jQuery("#patientSearchResult").hide();
		},
		
		/** TOGGLE PATIENT RESULT */
		togglePatientResult: function(){
			jQuery("#patientSearchResult").toggle();
		}
	};
</script>

<h2>Patient Registration</h2>
<div id="patientSearchResult"></div>
<form id="patientRegistrationForm" method="POST">
	<table cellspacing="0">
		<tr>
			<td valign="top" class="cell"><b>Name *</b></td>
			<td class="cell">
				<input id="patientName" type="hidden" name="patient.name"/>
				<div id="searchbox"></div>
				<div id="numberOfFoundPatients"></div>
			</td>
		</tr>		
		<tr>
			<td class="cell"><b>Demographics *</b></td>
			<td class="cell">
				dd/mm/yyyy<br/>
				<table>
					<tr>
						<td>Age</td>
						<td>Birthdate</td>
						<td>Gender</td>
					</tr>
					<tr>
						<td>
							<span id="estimatedAge"/>
						</td>
						<td>
							<input id="birthdate" name="patient.birthdate"/>
							<input id="birthdateEstimated" type="hidden" name="patient.birthdateEstimate" value="true"/>
						</td>
						<td>
							<select name="patient.gender">
								<option value=""></option>
								<option value="M">Male</option>
								<option value="F">Female</option>
							</select>
						</td>
					</tr>
				</table>
			</td>
		</tr>		
		<tr>
			<td class="cell"><b>ID Number *</b></td>
			<td class="cell"><input name="patient.identifier" style="border: none;"/></td>
		</tr>
		<tr>
			<td class="cell"><b>Address</b></td>
			<td class="cell">
				<table>
					<tr>
						<td>District:</td>
						<td>
							<select id="districts" name="patient.address.district" onChange="PAGE.changeDistrict();">
							</select>
						</td>
					</tr>
					<tr>
						<td>Tehsil:</td>
						<td>
							<select id="tehsils" name="patient.address.tehsil">
							</select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="cell"><b>Relative Name *</b></td>
			<td class="cell">
				SO/DO/WO: <br/>
				<input name="person.attribute.8"/>
			</td>
		</tr>
		<tr>
			<td class="cell"><b>Visit Information</b></td>
			<td class="cell">				
				<b>Referral Information</b><br/>
				<input type="checkbox" id="referred" onClick="PAGE.toogleReferralInfo(this);" name="patient.referred" value="referred"/> Referred<br/>				
				<div id="referralDiv" style="display:none;">
					<table>
						<tr>
							<td>Referred From</td>
							<td>
								<select id="referralHospitals" name="patient.referred.from">
								</select>
							</td>
						</tr>
						<tr>
							<td>Referral Type</td>
							<td>
								<select id="referralReasons" name="patient.referred.reason">
								</select>
							</td>
						</tr>
					</table>
				</div>
				<b>OPD Room to Visit: *</b>
				<select id="opdWard" name="patient.opdWard">
				</select>
			</td>
		</tr>
		<tr>
			<td valign="top" class="cell"><b>Patient information</b></td>
			<td class="cell">
				<b>Patient category</b><br/>
				<table cellspacing="10">
					<tr>
						<td>
							<input id="category.general" type="checkbox" name="person.attribute.14" value="General"/> General 
						</td>
						<td>
							<input type="checkbox" name="person.attribute.14" value="Poor"/> Poor 
						</td>
					</tr>
					<tr>
						<td>
							<input id="category.general" type="checkbox" name="person.attribute.14" value="Staff"/> Staff 
						</td>
						<td>
							<input type="checkbox" name="person.attribute.14" value="Government Employee"/> Government Employee 
						</td>
					</tr>
					<tr>
						<td>
							<input id="category.general" type="checkbox" name="person.attribute.14" value="RSBY"/> RSBY 
						</td>
						<td>
							RSBY Number <input name="person.attribute.11"/>
						</td>
					</tr>
					<tr>
						<td>
							<input id="category.general" type="checkbox" name="person.attribute.14" value="RSBY"/> BPL 
						</td>
						<td>
							BPL Number <input name="person.attribute.10"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form>

<input type="button" value="Save" onclick="PAGE.submit();"/>
<input type="button" value="Reset" onclick="window.location.href=window.location.href"/>