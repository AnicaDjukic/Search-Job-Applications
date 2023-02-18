import { Component, Input, OnInit } from '@angular/core';
import { SearchResultDto } from '../dtos/SearchResultDto';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.scss']
})
export class SearchResultComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  @Input() searchResult!: SearchResultDto

}
