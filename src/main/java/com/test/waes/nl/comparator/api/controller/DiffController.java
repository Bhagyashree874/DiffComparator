package com.test.waes.nl.comparator.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.waes.nl.comparator.api.domain.RequestData;
import com.test.waes.nl.comparator.api.domain.Side;
import com.test.waes.nl.comparator.api.service.DiffService;

/**
 * Entry point.Application Rest controller. Declares the endpoints that are used
 * to add the content for both sides of a diff document and a method to check
 * the differences between the sides.It maps the api endpoints and bring the
 * results.
 *
 * @author Bhagyashree
 *
 */

@RestController
@RequestMapping("v1/diff/{id}")

public class DiffController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DiffController.class);

	@Autowired
	private DiffService service;

	/**
	 * Building message for the end user. This method takes message as a parameter
	 * and returns String in response
	 *
	 * @author Bhagyashree
	 */
	private String buildJsonResponse(String message) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"");
		sb.append("message");
		sb.append("\":");
		sb.append("\"");
		sb.append(message);
		sb.append("\"");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Provides an endpoint to add the right side of a diff document.
	 *
	 * @param id the diff document identifier.
	 *
	 * @return a simple message for be showed on the response.
	 * @throws Exception String
	 *
	 * @author Bhagyashree
	 *
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	private String diff(@PathVariable Long id) throws Exception {
		return buildJsonResponse(service.validateBase64Data(id));
	}

	/**
	 * Provides an endpoint to get the difference of the left side and right side of
	 * a diff document.
	 *
	 * @param id          the diff document identifier.
	 * @param requestData an object that encapsulates the JSON with the base64
	 *                    encoded content.
	 * @return a representative object of the diff document.
	 * @param data string based base64 value for being updated.
	 * @return a simple message for be showed on the response.
	 * @throws Exception String
	 *
	 * @author Bhagyashree
	 *
	 */

	@RequestMapping(value = "/left", method = RequestMethod.PUT, produces = "application/json")
	private String left(@PathVariable Long id, @RequestBody RequestData data) throws Exception {
		LOGGER.info("Entering left(id={}, data={})", id, data);

		LOGGER.info("Setting '{}' side of the document with the value: '{}'", Side.LEFT, data.getData());
		service.save(id, data.getData(), Side.LEFT);

		LOGGER.info("'{}' side of the document saved successfuly for data: '{}'", Side.LEFT, data.getData());
		String message = buildJsonResponse("Document left-side saved successfuly");

		LOGGER.trace("Leaving left(id, data)={}", message);
		return message;
	}

	/**
	 * Provides an endpoint to add the right side of a diff document.
	 *
	 * @param id          the diff document identifier.
	 * @param requestData an object that encapsulates the JSON with the base64
	 *                    encoded content.
	 * @return a representative object of the diff document.
	 * @param data string based base64 value for being updated.
	 * @return a simple message for be showed on the response.
	 * @throws Exception String
	 *
	 * @author Bhagyashree
	 *
	 */

	@RequestMapping(value = "/right", method = RequestMethod.PUT, produces = "application/json")
	private String right(@PathVariable Long id, @RequestBody RequestData data) throws Exception {
		LOGGER.trace("Entering right(id={}, data={})", id, data);

		LOGGER.debug("Setting '{}' side of the document with the value: '{}'", Side.RIGHT, data);
		service.save(id, data.getData(), Side.RIGHT);

		LOGGER.info("'{}' side of the document saved successfuly for data: '{}'", Side.RIGHT, data.getData());

		String message = buildJsonResponse("Document right-side saved successfuly");
		LOGGER.trace("Leaving right(id, data)={}", message);
		return message;
	}
}
