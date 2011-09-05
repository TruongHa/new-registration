/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.laboratory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.ModuleActivator;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class LaboratoryActivator implements ModuleActivator {
	
	private Log log = LogFactory.getLog(this.getClass());

	public void contextRefreshed() {
		// TODO Auto-generated method stub
		
	}

	public void started() {
		log.info("Started Laboratory module");
	}

	public void stopped() {
		log.info("Stopped Laboratory module");		
	}

	public void willRefreshContext() {
		// TODO Auto-generated method stub
		
	}

	public void willStart() {
		// TODO Auto-generated method stub
		
	}

	public void willStop() {
		// TODO Auto-generated method stub
		
	}
	
}
