import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { SearchResultDto } from '../dtos/SearchResultDto';
import { SearchService } from '../services/search.service';

@Component({
  selector: 'app-simple-search',
  templateUrl: './simple-search.component.html',
  styleUrls: ['./simple-search.component.scss']
})
export class SimpleSearchComponent implements OnInit {

  constructor(private searchService: SearchService) { }

  ngOnInit(): void {
  }

  searchForm = new FormGroup({
    field: new FormControl(''),
    text: new FormControl('')
  })

  searchResults!: SearchResultDto[]

  get field() { return this.searchForm.get('field') }
  get text() { return this.searchForm.get('text') }

  submit() {
    const field = this.field?.value;
    const text = this.text?.value;

    this.searchService.simpleSearch(field!, text!).subscribe((data: any) => {
      this.searchResults = data
    }, (err: any) => {
      alert(err.error.redirect)
    })
    
  }

}
