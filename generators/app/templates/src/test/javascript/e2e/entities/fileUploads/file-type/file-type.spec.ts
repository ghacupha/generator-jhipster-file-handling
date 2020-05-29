import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { FileTypeComponentsPage, FileTypeDeleteDialog, FileTypeUpdatePage } from './file-type.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('FileType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fileTypeComponentsPage: FileTypeComponentsPage;
  let fileTypeUpdatePage: FileTypeUpdatePage;
  let fileTypeDeleteDialog: FileTypeDeleteDialog;
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

  it('should load FileTypes', async () => {
    await navBarPage.goToEntity('file-type');
    fileTypeComponentsPage = new FileTypeComponentsPage();
    await browser.wait(ec.visibilityOf(fileTypeComponentsPage.title), 5000);
    expect(await fileTypeComponentsPage.getTitle()).to.eq('prepsGateApp.fileUploadsFileType.home.title');
    await browser.wait(ec.or(ec.visibilityOf(fileTypeComponentsPage.entities), ec.visibilityOf(fileTypeComponentsPage.noResult)), 1000);
  });

  it('should load create FileType page', async () => {
    await fileTypeComponentsPage.clickOnCreateButton();
    fileTypeUpdatePage = new FileTypeUpdatePage();
    expect(await fileTypeUpdatePage.getPageTitle()).to.eq('prepsGateApp.fileUploadsFileType.home.createOrEditLabel');
    await fileTypeUpdatePage.cancel();
  });

  it('should create and save FileTypes', async () => {
    const nbButtonsBeforeCreate = await fileTypeComponentsPage.countDeleteButtons();

    await fileTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      fileTypeUpdatePage.setFileTypeNameInput('fileTypeName'),
      fileTypeUpdatePage.fileMediumTypeSelectLastOption(),
      fileTypeUpdatePage.setDescriptionInput('description'),
      fileTypeUpdatePage.setFileTemplateInput(absolutePath),
      fileTypeUpdatePage.fileTypeSelectLastOption(),
    ]);

    expect(await fileTypeUpdatePage.getFileTypeNameInput()).to.eq(
      'fileTypeName',
      'Expected FileTypeName value to be equals to fileTypeName'
    );
    expect(await fileTypeUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
    expect(await fileTypeUpdatePage.getFileTemplateInput()).to.endsWith(
      fileNameToUpload,
      'Expected FileTemplate value to be end with ' + fileNameToUpload
    );

    await fileTypeUpdatePage.save();
    expect(await fileTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fileTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last FileType', async () => {
    const nbButtonsBeforeDelete = await fileTypeComponentsPage.countDeleteButtons();
    await fileTypeComponentsPage.clickOnLastDeleteButton();

    fileTypeDeleteDialog = new FileTypeDeleteDialog();
    expect(await fileTypeDeleteDialog.getDialogTitle()).to.eq('prepsGateApp.fileUploadsFileType.delete.question');
    await fileTypeDeleteDialog.clickOnConfirmButton();

    expect(await fileTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
