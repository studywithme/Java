import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ContentsSharedLibsModule, ContentsSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [ContentsSharedLibsModule, ContentsSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [ContentsSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ContentsSharedModule {
  static forRoot() {
    return {
      ngModule: ContentsSharedModule
    };
  }
}
