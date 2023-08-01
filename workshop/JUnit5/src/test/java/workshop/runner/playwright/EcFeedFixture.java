package workshop.runner.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EcFeedFixture {

// To simplify the code and speed up tests, we only use one 'page' object.
// This might not be a good idea to use in production.

    static private Playwright playwright;           // Session.
    static private Browser browser;

    static private BrowserContext context;          // Test (here, it is extended to the whose session).
    static private Page page;

    static protected EcFeedPageObject ecfeed;       // Page object.

    @BeforeAll
    static void setUp() {
        var optionsBrowser = new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setTimeout(5000);

        var optionsContext = new Browser.NewContextOptions()
                .setViewportSize(1536, 900)
                .setDeviceScaleFactor(2);

        playwright = Playwright.create();
        browser = playwright.firefox().launch(optionsBrowser);

        context = browser.newContext(optionsContext);

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page = context.newPage();

        ecfeed = new EcFeedPageObject(page);

        ecfeed.visit();
    }

    @AfterAll
    static void tearDown() {

        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("playwright-trace.zip")));

        context.close();
        playwright.close();
    }
}
