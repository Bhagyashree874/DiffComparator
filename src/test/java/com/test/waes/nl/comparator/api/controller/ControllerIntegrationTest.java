package com.test.waes.nl.comparator.api.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.test.waes.nl.comparator.api.entity.Document;
import com.test.waes.nl.comparator.api.repository.DocumentRepository;

@RunWith(SpringRunner.class)

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

@AutoConfigureMockMvc
public class ControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	public DocumentRepository repository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	public void addOneSideAndGotErrorWhenGetDiff() throws Exception {

		repository.save(new Document(1l, "dGVzdGluZyB0aGUgYmFzZTY0", null));

		mvc.perform(MockMvcRequestBuilders.put("/v1/diff/1/left").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"data\": " + "  \"dGVzdGluZyB0aGUgYmFzZTY0\"" + "}")).andExpect(status().isOk())
				.andReturn();

		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.message", is("Base64 data missing"))).andReturn();
	}

	@Test
	public void different() throws Exception {
		repository.save(new Document(1l, "dGVzdGluZyB0aGUgYmFzZTY0", "dGhlIDJuZCB0ZXN0IGZvciBjb21wYXJpbmc="));
		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/1").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"data\": " + "  \"DBVsbG8gd29ybGJK=\"" + "}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("Base64 data have not same size."))).andReturn();
	}

	@Test
	public void DiffSideOtherThanLeftRight() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/1/up").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		;
	}

	@Test
	public void equal() throws Exception {
		repository.save(new Document(1l, "dGVzdGluZyB0aGUgYmFzZTY0", "dGVzdGluZyB0aGUgYmFzZTY0"));
		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/1").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"data\": " + "  \"DBVsbG8gd29ybGJK=\"" + "}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("Base64 data are equal"))).andReturn();
	}

	@Test
	public void equalsizeDifferentContent() throws Exception {
		repository.save(new Document(1l, "dGVzdGluZyB0aGUgYmFzZTY0", "dGV12GluZyB0aGUgYmFzZTY0"));
		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/1").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"data\": " + "  \"DBVsbG8gd29ybGJK=\"" + "}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message",
						is("Base64 data got the same size, but their offsets are different: 3 4")))
				.andReturn();
	}

	@Test
	public void onlyleft() throws Exception {
		mvc.perform(MockMvcRequestBuilders.put("/v1/diff/1/left").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"data\": " + "  \"dGVzdGluZyB0aGUgYmFzZTY0\"" + "}")).andExpect(status().isOk())
				.andReturn();
		Document document1 = repository.findById(1L);
		Assert.assertThat(document1.getId(), Matchers.is(1L));
		Assert.assertThat(document1.getLeft(), Matchers.is("dGVzdGluZyB0aGUgYmFzZTY0"));
		Assert.assertThat(document1.getRight(), Matchers.isEmptyOrNullString());
	}

	@Test
	public void onlyright() throws Exception {

		mvc.perform(MockMvcRequestBuilders.put("/v1/diff/1/right").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"data\": " + "  \"dGVzdGluZyB0aGUgYmFzZTY0\"" + "}")).andExpect(status().isOk())
				.andReturn();
		Document document2 = repository.findById(1L);
		Assert.assertThat(document2.getId(), Matchers.is(1L));
		Assert.assertThat(document2.getRight(), Matchers.is("dGVzdGluZyB0aGUgYmFzZTY0"));
		Assert.assertThat(document2.getLeft(), Matchers.isEmptyOrNullString());
	}

	@Before
	public void setup() throws Exception {
		this.mvc = webAppContextSetup(webApplicationContext).build();
		this.repository.deleteAll();
	}

	@Test
	public void shouldGetNoDataFoundWhenGetDiffFromUnstoredentifier() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.message", is("No data found"))).andReturn();
		;
	}

	@Test
	public void shouldRejectBothSidesWithEmptyData() throws Exception {

		mvc.perform(MockMvcRequestBuilders.put("/v1/diff/1/left").content("").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("No data found")));
		mvc.perform(MockMvcRequestBuilders.put("/v1/diff/1/right").content("").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("No data found")));
	}

}
