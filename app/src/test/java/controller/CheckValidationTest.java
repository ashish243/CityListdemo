package controller;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class) // use so Android classes like TextUtils will be mocked
@Config(manifest=Config.NONE) //To avoid WARNING: No manifest file found at ./AndroidManifest.xml.

/**
 * Created by Aashish on 8/6/18.
 */
public class CheckValidationTest {

    /**
     * single CredentialsValidator instance to use for all tests
     */
    private CheckValidation mValidator;

    public CheckValidationTest() {}

    /*********************************
     * setup methods
     *********************************/

    @Before
    public void setUp() throws
            Exception {

        mValidator = new CheckValidation();
    }

    /*********************************
     * cityname validation methods
     *********************************/

    @Test
    public void validate_cityname_empty_returnsFalse() throws
            Exception {

        Assert.assertFalse(mValidator.isCityNameValid(""));
    }

    @Test
    public void validate_cityname_null_returnsFalse() throws
            Exception {

        Assert.assertFalse(mValidator.isCityNameValid(null));
    }

    @Test
    public void validate_cityname_valid_returnsTrue() throws
            Exception {

        Assert.assertTrue(mValidator.isCityNameValid("cityName"));
    }

    /*********************************
     * city population validation methods
     *********************************/

    @Test
    public void validate_citypopulation_empty_returnsFalse() throws
            Exception {

        Assert.assertFalse(mValidator.isCityPopulationValid(""));
    }

    @Test
    public void validate_citypopulation_null_returnsFalse() throws
            Exception {

        Assert.assertFalse(mValidator.isCityPopulationValid(null));
    }

    @Test
    public void validate_citypopulation_valid_returnsTrue() throws
            Exception {

        Assert.assertTrue(mValidator.isCityPopulationValid("3245435243"));
    }

    /*********************************
     * city state validation methods
     *********************************/

    @Test
    public void validate_citystate_empty_returnsFalse() throws
            Exception {

        Assert.assertFalse(mValidator.isCityStateValid(""));
    }

    @Test
    public void validate_citystate_null_returnsFalse() throws
            Exception {

        Assert.assertFalse(mValidator.isCityStateValid(null));
    }

    @Test
    public void validate_citystate_valid_returnsTrue() throws
            Exception {

        Assert.assertTrue(mValidator.isCityStateValid("MP"));
    }

    // @After => JUnit 4 annotation that specifies this method should be run after each test
    @After
    public void tearDown() {
        // Destroy activity after every test
           mValidator = null;

    }
}