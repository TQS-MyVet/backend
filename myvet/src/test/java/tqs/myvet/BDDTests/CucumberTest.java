package tqs.myvet.BDDTests;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("tqs/myvet/BDDTests")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "tqs.myvet.BDDTests")
public class CucumberTest {

}