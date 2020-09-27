package com.test.waes.nl.comparator.api.service;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import javax.xml.bind.ValidationException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.test.waes.nl.comparator.api.domain.Side;
import com.test.waes.nl.comparator.api.entity.Document;
import com.test.waes.nl.comparator.api.exception.BlankDataException;
import com.test.waes.nl.comparator.api.exception.DiffSideIsNotDefinedException;
import com.test.waes.nl.comparator.api.exception.InvalidBase64contentException;
import com.test.waes.nl.comparator.api.exception.InvalidIdentifierException;
import com.test.waes.nl.comparator.api.repository.DocumentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ServiceTest {

	@InjectMocks
	private DiffService service;

	@Mock
	public DocumentRepository repository;

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = BlankDataException.class)
	public void shouldFailWhenBothSideIsMissing()
			throws BlankDataException, ValidationException, InvalidBase64contentException {
		Document document = new Document(1L, null, null);
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		Mockito.doAnswer(returnsFirstArg()).when(repository).save(Mockito.any(Document.class));
		service.validate(1L, document.getLeft());
		service.validate(1L, document.getRight());

	}

	@Test(expected = InvalidIdentifierException.class)
	public void shouldFailWhenGetDiffFromNulldentifier()
			throws MethodArgumentTypeMismatchException, NumberFormatException, InvalidIdentifierException {

		service.validateBase64Data(null);

	}

	@Test(expected = BlankDataException.class)
	public void shouldFailWhenLeftSideIsMissing()
			throws ValidationException, BlankDataException, InvalidBase64contentException {
		Document document = new Document(1L, null, "Bhagya");
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		Mockito.doAnswer(returnsFirstArg()).when(repository).save(Mockito.any(Document.class));
		service.validate(1L, document.getLeft());

	}

	@Test(expected = BlankDataException.class)
	public void shouldFailWhenRightSideIsMissing()
			throws BlankDataException, ValidationException, InvalidBase64contentException {
		Document document = new Document(1L, "Left", "");
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		Mockito.doAnswer(returnsFirstArg()).when(repository).save(Mockito.any(Document.class));
		service.validate(1L, document.getRight());

	}

	@Test
	public void shouldGiveProperMessagediffDifferentOffset() throws Exception {
		Document document = new Document(1L, "ABVsbG8gd29ybGJK=", "DBVsbG8gd29ybGJK=");
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		String result = service.validateBase64Data(1L);
		Assert.assertThat(result, Matchers.is("Base64 data got the same size, but their offsets are different: 0"));
	}

	@Test
	public void shouldGiveProperMessagediffDifferentSize() throws Exception {
		Document document = new Document(1L, "DBVsbG8gd29ybG=", "DBVsbG8gd29ybGJK=");
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		String result = service.validateBase64Data(1L);
		Assert.assertThat(result, Matchers.is("Base64 data have not same size."));
	}

	@Test
	public void shouldGiveProperMessagediffEqual() throws Exception {
		Document document = new Document(1L, "DBVsbG8gd29ybGJK=", "DBVsbG8gd29ybGJK=");
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		String result = service.validateBase64Data(1L);
		Assert.assertThat(result, Matchers.is("Base64 data are equal"));
	}

	@Test
	public void shouldGiveProperMessagediffMissingLeft() throws Exception {
		Document document = new Document(1L, null, "Right");
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		String result = service.validateBase64Data(1L);
		Assert.assertThat(result, Matchers.is("Base64 data missing"));
	}

	@Test
	public void shouldGiveProperMessagediffMissingRight() throws Exception {
		Document document = new Document(1L, "Left", null);
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		String result = service.validateBase64Data(1L);
		Assert.assertThat(result, Matchers.is("Base64 data missing"));
	}

	@Test
	public void ShouldGiveProperMessagediffNoDataFound() throws Exception {
		Mockito.doReturn(null).when(repository).findById(Mockito.eq(1L));
		String result = service.validateBase64Data(1L);
		Assert.assertThat(result, Matchers.is("No data found"));
	}

	@Test
	public void shouldGiveProperMessageleftFound() throws Exception {
		Document document = new Document(1L, null, "Right");
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		Mockito.doAnswer(returnsFirstArg()).when(repository).save(Mockito.any(Document.class));
		Document left = service.save(1L, "Left", Side.LEFT);
		Assert.assertThat(left.getId(), Matchers.is(1L));
		Assert.assertThat(left.getLeft(), Matchers.is("Left"));
		Assert.assertThat(left.getRight(), Matchers.is("Right"));
	}

	@Test
	public void shouldGiveProperMessageleftNotFound() throws Exception {
		Mockito.doReturn(null).when(repository).findById(Mockito.eq(1L));
		Mockito.doAnswer(returnsFirstArg()).when(repository).save(Mockito.any(Document.class));
		Document left = service.save(1L, "Bhagya", Side.RIGHT);
		Assert.assertThat(left.getId(), Matchers.is(1L));
		Assert.assertThat(left.getRight(), Matchers.is("Bhagya"));
		Assert.assertThat(left.getLeft(), Matchers.isEmptyOrNullString());
	}

	@Test
	public void shouldGiveProperMessagerightFound() throws Exception {
		Document document = new Document(1L, "Left", null);
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		Mockito.doAnswer(returnsFirstArg()).when(repository).save(Mockito.any(Document.class));
		Document left = service.save(1L, "Bhagya", Side.RIGHT);
		Assert.assertThat(left.getId(), Matchers.is(1L));
		Assert.assertThat(left.getLeft(), Matchers.is("Left"));
		Assert.assertThat(left.getRight(), Matchers.is("Bhagya"));
	}

	@Test
	public void shouldGiveProperMessagerightnotFound() throws Exception {
		Mockito.doReturn(null).when(repository).findById(Mockito.eq(1L));
		Mockito.doAnswer(returnsFirstArg()).when(repository).save(Mockito.any(Document.class));
		Document left = service.save(1L, "Left", Side.LEFT);
		Assert.assertThat(left.getId(), Matchers.is(1L));
		Assert.assertThat(left.getLeft(), Matchers.is("Left"));
		Assert.assertThat(left.getRight(), Matchers.isEmptyOrNullString());
	}

	@Test(expected = InvalidBase64contentException.class)
	public void shouldNotAcceptContentWithInvalidBase64Scheme()
			throws ValidationException, BlankDataException, InvalidBase64contentException {

		service.validate(1L, "4rdHFh%2BHYoS8oLdVvbUzEVqB8Lvm7kSPnuwF0AAABYQ%3D");
	}

	@Test(expected = DiffSideIsNotDefinedException.class)
	public void shouldRejectWhenDiffSideIsNotSpecified() throws Exception {
		service.save(1L, "Left", null);
	}

	@Test
	public void shouldUpdateLeftSideWhenFileIdExistsAndTheSideIsAlreadyDefined() throws Exception {
		Document document = new Document(1L, "Left", "asdf");
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		Mockito.doAnswer(returnsFirstArg()).when(repository).save(Mockito.any(Document.class));

		service.save(1L, "fdsa", Side.LEFT);

		verify(repository).findById(eq(1L));
		Assert.assertThat(document.getId(), Matchers.is(1L));
		Assert.assertThat(document.getLeft(), Matchers.is("fdsa"));
		Assert.assertThat(document.getRight(), Matchers.is("asdf"));

	}

	@Test
	public void shouldUpdateRightSideWhenFileIdExistsAndTheSideIsAlreadyDefined() throws Exception {
		Document document = new Document(1L, "Left", "asdf");
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		Mockito.doAnswer(returnsFirstArg()).when(repository).save(Mockito.any(Document.class));

		service.save(1L, "fdsa", Side.RIGHT);

		verify(repository).findById(eq(1L));
		Assert.assertThat(document.getId(), Matchers.is(1L));
		Assert.assertThat(document.getLeft(), Matchers.is("Left"));
		Assert.assertThat(document.getRight(), Matchers.is("fdsa"));

	}
}