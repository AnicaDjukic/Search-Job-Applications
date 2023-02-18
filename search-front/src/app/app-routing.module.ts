import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SimpleSearchComponent } from './simple-search/simple-search.component';
import { BooleanSearchComponent } from './boolean-search/boolean-search.component';
import { AdvancedSearchComponent } from './advanced-search/advanced-search.component';
import { GeoSearchComponent } from './geo-search/geo-search.component';

const routes: Routes = [
  {path: 'simple-search', component: SimpleSearchComponent},
  {path: 'boolean-search', component: BooleanSearchComponent},
  {path: 'advanced-search', component: AdvancedSearchComponent},
  {path: 'geo-search', component: GeoSearchComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
