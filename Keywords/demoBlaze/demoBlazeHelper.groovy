package demoBlaze
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import java.util.Random


// REALISTICALLY THERE SHOULD BE MULTIPLE FILES HERE WITH EACH CORRESPONDING TO A PAGE/EPIC or helper

class demoBlazeHelper {
	@Keyword
	def demoBlazeLogin(String username,String password, boolean success = true) {
		TestObject loginHeaderBtn = findTestObject('Object Repository/Header/header - login')
		WebUiBuiltInKeywords.click(loginHeaderBtn)

		TestObject usernameInput = findTestObject('Object Repository/userInputModal/sign in/input_Username_loginusername')
		WebUiBuiltInKeywords.setText(usernameInput, username)

		TestObject passwordInput = findTestObject('Object Repository/userInputModal/sign in/input_Password_loginpassword')
		WebUiBuiltInKeywords.setEncryptedText(passwordInput, password)

		TestObject loginModalBtn = findTestObject('Object Repository/userInputModal/sign in/button_Log_in')
		WebUiBuiltInKeywords.click(loginModalBtn)

		TestObject loginHeaderSuccess = findTestObject('Header/Auth_Welcome_Message')
		WebUiBuiltInKeywords.waitForElementVisible(loginHeaderSuccess, 1)

		if (success == true) {
			String welcomeMessage = WebUiBuiltInKeywords.getText(loginHeaderSuccess)
			WebUiBuiltInKeywords.verifyEqual(welcomeMessage, "Welcome " + username )
		}
	}

	@Keyword
	def demoBlazeSignUp(String username,String password) {
		TestObject signUpHeaderBtn = findTestObject('Object Repository/Header/a_Sign up')
		WebUiBuiltInKeywords.click(signUpHeaderBtn)

		TestObject usernameInput = findTestObject('Object Repository/userInputModal/sign up/input_Username_sign-username')
		WebUiBuiltInKeywords.setText(usernameInput, username)

		TestObject passwordInput = findTestObject('Object Repository/userInputModal/sign up/input_Password_sign-password')
		WebUiBuiltInKeywords.setEncryptedText(passwordInput, password)

		TestObject signUpModalBtn = findTestObject('Object Repository/userInputModal/sign up/button_Sign up')
		WebUiBuiltInKeywords.click(signUpModalBtn)
	}

	@Keyword
	def randUsername(){
		String name = "username_"
		Random random = new Random();
		int randomNumber = 10000000 + random.nextInt(90000000); // Generates a number between 10000000 and 99999999
		System.out.println(randomNumber);
		name = name + randomNumber.toString()
		return name
	}

	@Keyword
	def randProdTileNum(){
		Random random = new Random();
		int digit = random.nextInt(9) + 1; // Generates a number between 10000000 and 99999999
		System.out.println(digit);
		return digit
	}

	@Keyword
	def homePageOpenRandProd() {
		int randProdTileNumber = randProdTileNum()

		String productName = WebUiBuiltInKeywords.getText(findTestObject('Homepage/product tile by number', [('prodNum') : randProdTileNumber]))

		WebUiBuiltInKeywords.click(findTestObject('Homepage/product tile by number', [('prodNum') : randProdTileNumber]))

		WebUiBuiltInKeywords.verifyElementText(findTestObject('Object Repository/Product page/product page prod name'), productName)

		return randProdTileNumber
	}

	/**
	 * used to check error or success of checkout parameters
	 * @param isError boolean True if you expect error 
	 * @param message - optional is the text of the message
	 * */
	@Keyword
	def ddCheckoutValidation(String isError, String message = "") {
		if (isError == "true") {
			// expect an error to occur
			WebUiBuiltInKeywords.verifyAlertPresent(0)
			String actualAlertText = WebUiBuiltInKeywords.getAlertText()
			println "the actual message: \n" +  actualAlertText
			println "the expected message: \n" +  message
			WebUiBuiltInKeywords.verifyEqual(actualAlertText,message)
		} else {
			// expect success
			boolean tanksMessage = WebUiBuiltInKeywords.verifyElementVisible(findTestObject('Object Repository/Checkout Modals/h2_Thank you for your purchase'))
			WebUiBuiltInKeywords.verifyEqual(tanksMessage, true)
			boolean confIcon = WebUiBuiltInKeywords.verifyElementVisible(findTestObject('Object Repository/Checkout Modals/div__sa-placeholder'))
			WebUiBuiltInKeywords.verifyEqual(confIcon, true)
		}
	}
}