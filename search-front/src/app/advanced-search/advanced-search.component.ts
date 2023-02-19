import { Component, OnInit } from '@angular/core';
import { SearchResultDto } from '../dtos/SearchResultDto';
import { SearchService } from '../services/search.service';
import { FormBuilder, FormGroup, FormArray } from '@angular/forms';
import { AdvancedSearchDto } from '../dtos/AdvancedSearchDto';

@Component({
  selector: 'app-advanced-search',
  templateUrl: './advanced-search.component.html',
  styleUrls: ['./advanced-search.component.scss']
})
export class AdvancedSearchComponent implements OnInit {

  ngOnInit(): void {
  }

  searchResults!: SearchResultDto[]

  public searchForm: FormGroup;
  constructor(private _fb: FormBuilder, private searchService: SearchService) {
    this.searchForm = this._fb.group({
      params: this._fb.array([this.addFormGroup()])
    });
  }
  //Append Fields Set
  private addFormGroup(): FormGroup {
    return this._fb.group({
      field: [],
      value: [],
      operation: []
    });
  }
  //Add Fields
  add(): void {
    this.array.push(this.addFormGroup());
  }

  remove(index: number): void {
    this.array.removeAt(index);
  }

  //Fields Array
  get array(): FormArray {
    return <FormArray>this.searchForm.get('params');
  }

  submit() {
    const advancedSearchDto = new AdvancedSearchDto (this.array.value)
    console.log(advancedSearchDto)
    this.searchService.advancedSearch(advancedSearchDto).subscribe((data: any) => {
      this.searchResults = data
    }, (err: any) => {
      alert(err.error.redirect)
    })
    
  }

}
