package net.lelyak.tests.impl.bank;

import net.lelyak.pages.bank.BankLoginPage;
import net.lelyak.tests.TestBase;
import org.testng.annotations.Test;

public class LoginTestITCase extends TestBase {

    @Test
    public void testLoginPageElements() throws Exception {
        BankLoginPage loginPage = new BankLoginPage();
        loginPage.waitPageLoad();
        asserter.assertPass(loginPage.exist(),
                "page isn't open",
                "page is open");
    }
}
