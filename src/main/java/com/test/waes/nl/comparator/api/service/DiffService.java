package com.test.waes.nl.comparator.api.service;

import java.util.Arrays;
import java.util.Base64;

import javax.xml.bind.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.test.waes.nl.comparator.api.domain.Side;
import com.test.waes.nl.comparator.api.entity.Document;
import com.test.waes.nl.comparator.api.exception.BlankDataException;
import com.test.waes.nl.comparator.api.exception.DiffSideIsNotDefinedException;
import com.test.waes.nl.comparator.api.exception.InvalidBase64contentException;
import com.test.waes.nl.comparator.api.exception.InvalidIdentifierException;
import com.test.waes.nl.comparator.api.repository.DocumentRepository;

@Service
public class DiffService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DiffService.class);

	@Autowired
	public DocumentRepository repository;

	/**
	 *
	 * Save the object
	 *
	 * @author Bhagya
	 * @param id   unique identifier of the document
	 * @param data
	 * @param side
	 * @return
	 * @throws Exception Document
	 */
	public Document save(Long id, String data, Side side) throws Exception {
		Document document = null;

		if (validate(id, data)) {
			document = repository.findById(id.longValue());
			if (document == null) {
				document = new Document();
				document.setId(id);
			}
			if (side == null) {
				throw new DiffSideIsNotDefinedException();
			}

			if (Side.LEFT.equals(side)) {
				document.setLeft(data);
			} else if (Side.RIGHT.equals(side)) {
				document.setRight(data);
			} else {
				LOGGER.warn("Invalid side sent.");
			}
			document = repository.save(document);
		}

		return document;
	}

	/**
	 *
	 * Checks the data for ensure the document will be created correctly.
	 *
	 * @author Bhagya
	 * @param id   of the document
	 * @param data to be saved.
	 * @throws InvalidBase64contentException
	 * @throws Exception                     void
	 */
	public boolean validate(Long id, String data)
			throws ValidationException, BlankDataException, InvalidBase64contentException {
		boolean isValid = true;
		if (StringUtils.isEmpty(data)) {
			isValid = false;
			LOGGER.warn("Json data is blank or null or invalid");
			throw new BlankDataException();

		}

		LOGGER.trace("Entering validate(id={}, data={})", id, data);
		try {
			LOGGER.info("Inside try");
			Base64.getDecoder().decode(data);

		} catch (IllegalArgumentException e) {
			LOGGER.debug("The content {} is not a valid bases64", data, e);

			isValid = false;
			throw new InvalidBase64contentException();
		}

		LOGGER.debug("Validating request for id '{}'", id);

		LOGGER.trace("Leaving validate(id, data)={}", isValid);
		return isValid;
	}

	/**
	 * Do the core validation in order to compare Jsons and return its results
	 *
	 * @param id is used by repository to find a object
	 * @return a string with comparison results
	 * @throws InvalidIdentifierException
	 * @throws Exception
	 * @throws TypeMisMatchException
	 */
	public String validateBase64Data(Long id)
			throws MethodArgumentTypeMismatchException, NumberFormatException, InvalidIdentifierException {
		LOGGER.trace("Entering validateBase64Data(id={})", id);
		LOGGER.debug("Will try to find the document by for id '{}'", id);

		if (id == null) {
			throw new InvalidIdentifierException();
		}

		Document document = repository.findById(id.longValue());

		if (document == null) {
			return "No data found";
		}

		LOGGER.debug("Document found. Will check the base64 data on both sides for id '{}'", id);
		if (!StringUtils.isNotBlank(document.getLeft()) || !StringUtils.isNotBlank(document.getRight())) {
			return "Base64 data missing";
		}

		byte[] bytesLeft = document.getLeft().getBytes();
		String leftstring = new String(bytesLeft);

		LOGGER.info("Left document is '{}'", leftstring);
		byte[] bytesRight = document.getRight().getBytes();
		String rightstring = new String(bytesRight);
		LOGGER.info("Right document is '{}'", rightstring);
		boolean blnResult = Arrays.equals(bytesLeft, bytesRight);

		String offsets = "";

		if (blnResult) {
			LOGGER.warn("Base64 data are equal");
			return "Base64 data are equal";
		} else if (bytesLeft.length != bytesRight.length) {
			return "Base64 data have not same size.";
		} else {
			byte different = 0;
			for (int index = 0; index < bytesLeft.length; index++) {
				different = (byte) (bytesLeft[index] ^ bytesRight[index]);
				if (different != 0) {
					offsets = offsets + " " + index;
				}
			}
		}
		return "Base64 data got the same size, but their offsets are different:" + offsets;
	}

}
