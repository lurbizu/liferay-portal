/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.dynamicdatamapping.util;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.dynamicdatamapping.BaseDDMTestCase;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Marcellus Tavares
 */
public class DDMFormValuesTransformerTest extends BaseDDMTestCase {

	@Before
	public void setUp() {
		setUpJSONFactoryUtil();
	}

	@Test
	public void testTransformNestedRepeatableTextFormFieldValue()
		throws Exception {

		DDMForm ddmForm = createDDMForm();

		DDMFormField nameDDMFormField = new DDMFormField("Name", "textarea");

		DDMFormField phoneDDMFormField = new DDMFormField("Phone", "text");

		phoneDDMFormField.setRepeatable(true);

		nameDDMFormField.addNestedDDMFormField(phoneDDMFormField);

		ddmForm.addDDMFormField(nameDDMFormField);

		DDMFormValues ddmFormValues = createDDMFormValues(ddmForm);

		DDMFormFieldValue nameDDMFormFieldValue = createDDMFormFieldValue(
			"Name", new UnlocalizedValue("Joe Smith"));

		nameDDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue("Phone", new UnlocalizedValue("123")));
		nameDDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue("Phone", new UnlocalizedValue("456")));
		nameDDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue("Phone", new UnlocalizedValue("789")));

		ddmFormValues.addDDMFormFieldValue(nameDDMFormFieldValue);

		DDMFormValuesTransformer ddmFormValuesTransformer =
			new DDMFormValuesTransformer(ddmFormValues);

		String prefix = "+1";

		ddmFormValuesTransformer.addTransformer(
			"text", new DDMFormFieldValuePrefixAppender(prefix));

		ddmFormValuesTransformer.transform();

		List<DDMFormFieldValue> ddmFormFieldValues =
			ddmFormValues.getDDMFormFieldValues();

		nameDDMFormFieldValue = ddmFormFieldValues.get(0);

		testDDMFormFieldValue(ddmFormFieldValues.get(0), "Joe Smith");

		List<DDMFormFieldValue> phoneDDMFormFieldValues =
			nameDDMFormFieldValue.getNestedDDMFormFieldValues();

		testDDMFormFieldValue(
			phoneDDMFormFieldValues.get(0), prefix.concat("123"));
		testDDMFormFieldValue(
			phoneDDMFormFieldValues.get(1), prefix.concat("456"));
		testDDMFormFieldValue(
			phoneDDMFormFieldValues.get(2), prefix.concat("789"));
	}

	@Test
	public void testTransformTextFormFieldValue() throws Exception {
		DDMForm ddmForm = createDDMForm();

		addTextDDMFormFields(ddmForm, "FirstName", "LastName");

		DDMFormField ddmFormField = new DDMFormField("Description", "textarea");

		ddmForm.addDDMFormField(ddmFormField);

		DDMFormValues ddmFormValues = createDDMFormValues(ddmForm);

		ddmFormValues.addDDMFormFieldValue(
			createDDMFormFieldValue("FirstName", new UnlocalizedValue("Joe")));
		ddmFormValues.addDDMFormFieldValue(
			createDDMFormFieldValue("LastName", new UnlocalizedValue("Smith")));
		ddmFormValues.addDDMFormFieldValue(
			createDDMFormFieldValue(
				"Description", new UnlocalizedValue("Description Value")));

		DDMFormValuesTransformer ddmFormValuesTransformer =
			new DDMFormValuesTransformer(ddmFormValues);

		String prefix = StringPool.UNDERLINE;

		ddmFormValuesTransformer.addTransformer(
			"text", new DDMFormFieldValuePrefixAppender(prefix));

		ddmFormValuesTransformer.transform();

		List<DDMFormFieldValue> ddmFormFieldValues =
			ddmFormValues.getDDMFormFieldValues();

		testDDMFormFieldValue(ddmFormFieldValues.get(0), prefix.concat("Joe"));
		testDDMFormFieldValue(
			ddmFormFieldValues.get(1), prefix.concat("Smith"));
		testDDMFormFieldValue(ddmFormFieldValues.get(2), "Description Value");
	}

	@Override
	protected DDMFormField createTextDDMFormField(String name) {
		return createTextDDMFormField(name, name, false, false, false);
	}

	protected void testDDMFormFieldValue(
		DDMFormFieldValue ddmFormFieldValue, String expectedValue) {

		Value value = ddmFormFieldValue.getValue();

		Locale defaultLocale = value.getDefaultLocale();

		Assert.assertEquals(expectedValue, value.getString(defaultLocale));
	}

	private class DDMFormFieldValuePrefixAppender
		implements DDMFormFieldValueTransformer {

		@Override
		public void transform(Value value) {
			for (Locale locale : value.getAvailableLocales()) {
				value.addString(
					locale, _prefix.concat(value.getString(locale)));
			}
		}

		private DDMFormFieldValuePrefixAppender(String prefix) {
			_prefix = prefix;
		}

		private final String _prefix;

	}

}