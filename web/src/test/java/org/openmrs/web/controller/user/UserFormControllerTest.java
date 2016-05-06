/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.controller.user;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.PersonName;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.test.Verifies;
import org.openmrs.web.test.BaseWebContextSensitiveTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * Tests the {@link oldUserFormController} class.
 */
public class UserFormControllerTest extends BaseWebContextSensitiveTest {
	
	protected static final String TEST_DATA = "org/openmrs/web/controller/include/UserFormControllerTest.xml";
	
	/**
	 * @see {@link UserFormController#handleSubmission(WebRequest,HttpSession,String,String,String,null,User,BindingResult)}
	 *
	 */
	@Test
	@Verifies(value = "should work for an example", method = "handleSubmission(WebRequest,HttpSession,String,String,String,null,User,BindingResult)")
	public void handleSubmission_shouldWorkForAnExample() throws Exception {
		UserFormController controller = new UserFormController();
		WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
		User user = controller.formBackingObject(request, null);
		user.addName(new PersonName("This", "is", "Test"));
		user.getPerson().setGender("F");
		controller.handleSubmission(request, new MockHttpSession(), new ModelMap(), "Save User", "pass123", "pass123", null,
		    null, null, new String[0], "true", null, user, new BindException(user, "user"));
	}
	
	/**
	 * @see UserFormController#handleSubmission(WebRequest,HttpSession,String,String,String,null, String, User,BindingResult)
	 *
	 */
	@Test
	@Verifies(value = "Creates Provider Account when Provider Account Checkbox is selected", method = "handleSubmission(WebRequest,HttpSession,String,String,String,null, String, User,BindingResult)")
	public void handleSubmission_createUserProviderAccountWhenProviderAccountCheckboxIsSelected() throws Exception {
		executeDataSet(TEST_DATA);
		UserFormController controller = new UserFormController();
		WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
		//A user with userId=2 is preset in the Test DataSet and the relevant details are passed
		User user = Context.getUserService().getUser(2);
		Assert.assertTrue(Context.getProviderService().getProvidersByPerson(user.getPerson()).isEmpty());
		ModelMap model = new ModelMap();
		model.addAttribute("isProvider", false);
		controller.showForm(2, "true", user, model);
		controller.handleSubmission(request, new MockHttpSession(), new ModelMap(), "", "Test1234", "valid secret question",
		    "valid secret answer", "Test1234", false, new String[] { "Provider" }, "true", "addToProviderTable", user,
		    new BindException(user, "user"));
		Assert.assertFalse(Context.getProviderService().getProvidersByPerson(user.getPerson()).isEmpty());
	}
	
}
