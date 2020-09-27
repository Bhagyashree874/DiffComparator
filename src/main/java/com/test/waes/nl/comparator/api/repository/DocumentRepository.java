package com.test.waes.nl.comparator.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.test.waes.nl.comparator.api.entity.Document;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {

	/**
	 *
	 * findById Method will find the document by its ID.
	 *
	 * @author Bhagyashree
	 * @param id
	 * @return Document
	 */

	Document findById(long id);
}
