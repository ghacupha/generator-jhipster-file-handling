import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { FileUploadComponentsPage, FileUploadDeleteDialog, FileUploadUpdatePage } from './file-upload.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('FileUpload e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fileUploadComponentsPage: FileUploadComponentsPage;
  let fileUploadUpdatePage: FileUploadUpdatePage;
  let fileUploadDeleteDialog: FileUploadDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FileUploads', async () => {
    await navBarPage.goToEntity('file-upload');
    fileUploadComponentsPage = new FileUploadComponentsPage();
    await browser.wait(ec.visibilityOf(fileUploadComponentsPage.title), 5000);
    expect(await fileUploadComponentsPage.getTitle()).to.eq('prepsGateApp.fileUploadsFileUpload.home.title');
    await browser.wait(ec.or(ec.visibilityOf(fileUploadComponentsPage.entities), ec.visibilityOf(fileUploadComponentsPage.noResult)), 1000);
  });

  it('should load create FileUpload page', async () => {
    await fileUploadComponentsPage.clickOnCreateButton();
    fileUploadUpdatePage = new FileUploadUpdatePage();
    expect(await fileUploadUpdatePage.getPageTitle()).to.eq('prepsGateApp.fileUploadsFileUpload.home.createOrEditLabel');
    await fileUploadUpdatePage.cancel();
  });

  it('should create and save FileUploads', async () => {
    const nbButtonsBeforeCreate = await fileUploadComponentsPage.countDeleteButtons();

    await fileUploadComponentsPage.clickOnCreateButton();

    await promise.all([
      fileUploadUpdatePage.setDescriptionInput('description'),
      fileUploadUpdatePage.setFileNameInput('fileName'),
      fileUploadUpdatePage.setPeriodFromInput('2000-12-31'),
      fileUploadUpdatePage.setPeriodToInput('2000-12-31'),
      fileUploadUpdatePage.setFileTypeIdInput('5'),
      fileUploadUpdatePage.setDataFileInput(absolutePath),
      fileUploadUpdatePage.setUploadTokenInput('uploadToken'),
    ]);

    expect(await fileUploadUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
    expect(await fileUploadUpdatePage.getFileNameInput()).to.eq('fileName', 'Expected FileName value to be equals to fileName');
    expect(await fileUploadUpdatePage.getPeriodFromInput()).to.eq('2000-12-31', 'Expected periodFrom value to be equals to 2000-12-31');
    expect(await fileUploadUpdatePage.getPeriodToInput()).to.eq('2000-12-31', 'Expected periodTo value to be equals to 2000-12-31');
    expect(await fileUploadUpdatePage.getFileTypeIdInput()).to.eq('5', 'Expected fileTypeId value to be equals to 5');
    expect(await fileUploadUpdatePage.getDataFileInput()).to.endsWith(
      fileNameToUpload,
      'Expected DataFile value to be end with ' + fileNameToUpload
    );
    const selectedUploadSuccessful = fileUploadUpdatePage.getUploadSuccessfulInput();
    if (await selectedUploadSuccessful.isSelected()) {
      await fileUploadUpdatePage.getUploadSuccessfulInput().click();
      expect(await fileUploadUpdatePage.getUploadSuccessfulInput().isSelected(), 'Expected uploadSuccessful not to be selected').to.be
        .false;
    } else {
      await fileUploadUpdatePage.getUploadSuccessfulInput().click();
      expect(await fileUploadUpdatePage.getUploadSuccessfulInput().isSelected(), 'Expected uploadSuccessful to be selected').to.be.true;
    }
    const selectedUploadProcessed = fileUploadUpdatePage.getUploadProcessedInput();
    if (await selectedUploadProcessed.isSelected()) {
      await fileUploadUpdatePage.getUploadProcessedInput().click();
      expect(await fileUploadUpdatePage.getUploadProcessedInput().isSelected(), 'Expected uploadProcessed not to be selected').to.be.false;
    } else {
      await fileUploadUpdatePage.getUploadProcessedInput().click();
      expect(await fileUploadUpdatePage.getUploadProcessedInput().isSelected(), 'Expected uploadProcessed to be selected').to.be.true;
    }
    expect(await fileUploadUpdatePage.getUploadTokenInput()).to.eq('uploadToken', 'Expected UploadToken value to be equals to uploadToken');

    await fileUploadUpdatePage.save();
    expect(await fileUploadUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fileUploadComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last FileUpload', async () => {
    const nbButtonsBeforeDelete = await fileUploadComponentsPage.countDeleteButtons();
    await fileUploadComponentsPage.clickOnLastDeleteButton();

    fileUploadDeleteDialog = new FileUploadDeleteDialog();
    expect(await fileUploadDeleteDialog.getDialogTitle()).to.eq('prepsGateApp.fileUploadsFileUpload.delete.question');
    await fileUploadDeleteDialog.clickOnConfirmButton();

    expect(await fileUploadComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
