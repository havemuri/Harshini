package com.collabera.runners;

import org.junit.runner.RunWith;
import org.testng.annotations.DataProvider;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import io.cucumber.junit.Cucumber;



			
@RunWith(Cucumber.class)
@CucumberOptions(strict = true,
	features = "src/test/java/resources/ChromeTest.feature" ,format = {"pretty"} )
	
public class TestRunner {
}
