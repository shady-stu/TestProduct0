import { test, expect } from '@playwright/test';
import { LoginPOM } from './loginPom.';


test.describe("Login Page Tests", () => {

  test("Valid login", async ({ page }) => {
    const loginPage = new LoginPOM(page);
    await loginPage.navigate();

    await loginPage.login("standard_user", "secret_sauce");

    await expect(page).toHaveURL(/inventory.html/);
  });

  test("Invalid login", async ({ page }) => {
    const loginPage = new LoginPOM(page);
    await loginPage.navigate();

    await loginPage.login("invalid_user", "wrong_pass");

    await expect(loginPage.errorMsg)
      .toContainText("Username and password do not match");
  });

  test("Empty username and password", async ({ page }) => {
    const loginPage = new LoginPOM(page);
    await loginPage.navigate();

    await loginPage.login("", "");

    await expect(loginPage.errorMsg)
      .toContainText("Username is required");
  });

  test("Locked out user", async ({ page }) => {
    const loginPage = new LoginPOM(page);
    await loginPage.navigate();

    await loginPage.login("locked_out_user", "secret_sauce");

    await expect(loginPage.errorMsg)
      .toContainText("locked out");
  });
});
