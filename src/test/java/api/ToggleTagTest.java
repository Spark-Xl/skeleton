package api;

import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import javax.validation.Validator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class ToggleTagTest {
    private final Validator validator = Validators.newValidator();

    @Test
    public void testValid() {
        ToggleTagRequest tag = new ToggleTagRequest();
        tag.id = new Integer(1);
        assertThat(validator.validate(tag), empty());
    }


    @Test
    public void testMissingId() {
        ToggleTagRequest tag = new ToggleTagRequest();
        assertThat(validator.validate(tag), hasSize(1));
    }
}
