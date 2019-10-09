package com.bpm.utility.JBPMUtilities;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JBPMUtilityProvider {

	private static Logger logger = Logger.getLogger(JBPMUtilityProvider.class.getName());
	private static volatile JBPMUtilityProvider singleInstance;
	private static Object lock = new Object();

	private JBPMUtilityProvider() {

	}

	public static JBPMUtilityProvider instance() {
		JBPMUtilityProvider jbpmUtilityProvider = singleInstance;
		if (jbpmUtilityProvider == null) {
			synchronized (lock) {
				jbpmUtilityProvider = singleInstance;
				if (jbpmUtilityProvider == null) {
					jbpmUtilityProvider = singleInstance = new JBPMUtilityProvider();
					logger.log(Level.INFO, "JBPM Utility Provider instnace is created");
				}
			}
		}
		return jbpmUtilityProvider;
	}

	public boolean isJSONValid(String jsonString) {

		final ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.readTree(jsonString);
			return true;
		} catch (JsonParseException e) {

		} catch (IOException e) {

		}
		return false;
	}

	/**
	 * A method to convert a JSON content from a file path into a required
	 * object
	 *
	 * @param <T>
	 * @return
	 */
	public <T> T toObjectf(String filePath, Class<T> objectType) {

		ObjectMapper mapper = new ObjectMapper();
		T obj = null;
		try {

			byte[] jsonData = Files.readAllBytes(Paths.get(filePath));
			obj = mapper.readValue(jsonData, objectType);

			logger.info("constructMyInstance: Instance of type " + objectType.getName()
					+ " is created, content found at " + filePath);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (SecurityException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return obj;
	}

	/**
	 * A method to convert a data object (in java) to a JSON string which is
	 * indented and doesn't fail on empty beans.
	 *
	 * @return
	 */
	public String toSerializedString(Object dataObject) {

		ObjectMapper objMapper = new ObjectMapper();
		objMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		StringWriter sw = new StringWriter();
		try {
			objMapper.writeValue(sw, dataObject);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return sw.toString();
	}

	/**
	 * A method to convert a data object (in java) to a JSON string. The indentation
	 * of the output is a choice to user
	 *
	 * @return
	 */
	public String toSerializedString(Object dataObject, boolean outputIntend) {

		ObjectMapper objMapper = new ObjectMapper();
		objMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		StringWriter sw = new StringWriter();
		try {
			objMapper.writeValue(sw, dataObject);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return sw.toString();
	}

	/**
	 * A method to convert a JSON string to an object of choice
	 *
	 * @param jsonString
	 * @param objectType
	 * @return
	 */
	public static <T> T toObject(String jsonString, Class<T> objectType) {

		ObjectMapper objMapper = new ObjectMapper();
		T obj = null;

		try {
			obj = objMapper.readValue(jsonString, objectType);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return obj;
	}

	/**
	 * A method to convert a JSON string to a List of objects of choice
	 *
	 * @param jsonString
	 * @return
	 */
	public static <T> List<T> toObjectsListg(String jsonString, Class<T> objectType) {
		List<T> obj = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			obj = mapper.readValue(jsonString, new TypeReference<List<T>>() {
			});
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return obj;
	}

}
