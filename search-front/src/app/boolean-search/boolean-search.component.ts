import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { BooleanSearchDto } from '../dtos/BooleanSearchDto';
import { SearchResultDto } from '../dtos/SearchResultDto';
import { SearchService } from '../services/search.service';

@Component({
  selector: 'app-boolean-search',
  templateUrl: './boolean-search.component.html',
  styleUrls: ['./boolean-search.component.scss']
})
export class BooleanSearchComponent implements OnInit {

  constructor(private searchService: SearchService) { }

  ngOnInit(): void {
  }

  searchForm = new FormGroup({
    field1: new FormControl(''),
    value1: new FormControl(''),
    operation: new FormControl(''),
    field2: new FormControl(''),
    value2: new FormControl('')
  })

  searchResults!: SearchResultDto[]

  get field1() { return this.searchForm.get('field1') }
  get value1() { return this.searchForm.get('value1') }
  get operation() { return this.searchForm.get('operation')}
  get field2() { return this.searchForm.get('field2') }
  get value2() { return this.searchForm.get('value2') }

  submit() {
    
    const field1 = this.field1?.value;
    const value1 = this.value1?.value;
    const operation = this.operation?.value;
    const field2 = this.field2?.value;
    const value2 = this.value2?.value;

    let searchResultDto = new BooleanSearchDto ({
      field1,
      value1,
      field2,
      value2,
      operation
    }) 

    this.searchService.booleanSearch(searchResultDto).subscribe((data: any) => {
      this.searchResults = data
    }, (err: any) => {
      alert(err.error.redirect)
    })
    
  }

}
