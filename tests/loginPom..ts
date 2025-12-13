import { Page, Locator, expect } from "@playwright/test";

export class LoginPOM {
  readonly usernameInput: Locator;
  readonly passwordInput: Locator;
  readonly loginButton: Locator;
  readonly errorMsg: Locator;

  constructor(private readonly page: Page) {
    this.usernameInput = page.locator('[data-test="username"]');
    this.passwordInput = page.locator('[data-test="password"]');
    this.loginButton = page.locator('[data-test="login-button"]');
   this.errorMsg = page.locator('[data-test="error"]');
  }

  async login(username: string, password: string) {
    await this.usernameInput.fill(username);
    await this.passwordInput.fill(password);
    await this.loginButton.click();
    
  }

   async navigate() {
    await this.page.goto("https://www.saucedemo.com/");
  }

async verifyLogin(){
await expect(this.page.locator('div.app_logo')).toBeVisible()
await expect(this.page.url()).toContain('inventory')}

}
