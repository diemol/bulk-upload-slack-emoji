import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class BulkUploadSlackEmoji {
	private final static String SLACK_EMAIL = System.getenv("SLACK_EMAIL");
	private final static String SLACK_PASSWORD = System.getenv("SLACK_PASSWORD");
	private final static String SLACK_WORKSPACE = System.getenv("SLACK_WORKSPACE");
	private static final String SLACK_EMOJI_PATH = System.getenv("SLACK_EMOJI_PATH");

	public static void main(String[] args) {

		WebDriver webDriver = new ChromeDriver();

		try {
			webDriver.manage().window().maximize();

			// Going to the upload custom emoji page
			webDriver.get(String.format("https://%s.slack.com/customize/emoji", SLACK_WORKSPACE));

			// Logging in
			WebDriverWait wait = new WebDriverWait(webDriver, 20);
			WebElement email = wait.until(elementToBeClickable(By.cssSelector("input[data-qa='login_email']")));
			email.sendKeys(SLACK_EMAIL);
			WebElement password = webDriver.findElement(By.cssSelector("input[data-qa='login_password']"));
			password.sendKeys(SLACK_PASSWORD);
			WebElement signIn = webDriver.findElement(By.cssSelector("button[data-qa='signin_button']"));
			signIn.click();

			// Bulk upload!
			try (Stream<Path> pathStream = Files.walk(Paths.get(SLACK_EMOJI_PATH))) {
				pathStream
						.filter(Files::isRegularFile)
						.filter(path -> path.getFileName().toString().endsWith(".gif") || path.getFileName().toString().endsWith(".png"))
						.forEach(path -> {
							// Clicking on add custom emoji
							WebElement addCustomEmoji = wait.until(elementToBeClickable(By.cssSelector("button[data-qa='customize_emoji_add_button']")));
							addCustomEmoji.click();

							// Waiting for the form to be there and uploading the file
							String fileName = path.getFileName().toString();
							int lastDot = fileName.lastIndexOf(".");
							wait.until(elementToBeClickable(By.cssSelector("button[data-qa='customize_emoji_add_dialog_upload']")));
							WebElement uploadImage = webDriver.findElement(By.cssSelector("input[data-qa='customize_emoji_add_dialog_file_input']"));
							uploadImage.sendKeys(path.toAbsolutePath().toString());
							WebElement giveItAName = webDriver.findElement(By.cssSelector("input[data-qa='customize_emoji_add_dialog_input']"));
							giveItAName.sendKeys(fileName.substring(0, lastDot));
							WebElement save = webDriver.findElement(By.cssSelector("button[data-qa='customize_emoji_add_dialog_go']"));
							save.click();
						});
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			WebElement signOut = webDriver.findElement(By.cssSelector("a[data-qa='sign_out']"));
			signOut.click();
			webDriver.quit();
		}
		System.exit(0);
	}

}
