import { element, by, ElementFinder } from 'protractor';

export class FileTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('gha-file-type div table .btn-danger'));
  title = element.all(by.css('gha-file-type div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class FileTypeUpdatePage {
  pageTitle = element(by.id('gha-file-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  fileTypeNameInput = element(by.id('field_fileTypeName'));
  fileMediumTypeSelect = element(by.id('field_fileMediumType'));
  descriptionInput = element(by.id('field_description'));
  fileTemplateInput = element(by.id('file_fileTemplate'));
  fileTypeSelect = element(by.id('field_fileType'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setFileTypeNameInput(fileTypeName: string): Promise<void> {
    await this.fileTypeNameInput.sendKeys(fileTypeName);
  }

  async getFileTypeNameInput(): Promise<string> {
    return await this.fileTypeNameInput.getAttribute('value');
  }

  async setFileMediumTypeSelect(fileMediumType: string): Promise<void> {
    await this.fileMediumTypeSelect.sendKeys(fileMediumType);
  }

  async getFileMediumTypeSelect(): Promise<string> {
    return await this.fileMediumTypeSelect.element(by.css('option:checked')).getText();
  }

  async fileMediumTypeSelectLastOption(): Promise<void> {
    await this.fileMediumTypeSelect.all(by.tagName('option')).last().click();
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setFileTemplateInput(fileTemplate: string): Promise<void> {
    await this.fileTemplateInput.sendKeys(fileTemplate);
  }

  async getFileTemplateInput(): Promise<string> {
    return await this.fileTemplateInput.getAttribute('value');
  }

  async setFileTypeSelect(fileType: string): Promise<void> {
    await this.fileTypeSelect.sendKeys(fileType);
  }

  async getFileTypeSelect(): Promise<string> {
    return await this.fileTypeSelect.element(by.css('option:checked')).getText();
  }

  async fileTypeSelectLastOption(): Promise<void> {
    await this.fileTypeSelect.all(by.tagName('option')).last().click();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class FileTypeDeleteDialog {
  private dialogTitle = element(by.id('gha-delete-fileType-heading'));
  private confirmButton = element(by.id('gha-confirm-delete-fileType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
