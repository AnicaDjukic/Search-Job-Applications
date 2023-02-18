import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { GeoSearchDto } from '../dtos/GeoSearchDto';
import { SearchResultDto } from '../dtos/SearchResultDto';
import { SearchService } from '../services/search.service';

@Component({
  selector: 'app-geo-search',
  templateUrl: './geo-search.component.html',
  styleUrls: ['./geo-search.component.scss']
})
export class GeoSearchComponent implements OnInit {

  constructor(private searchService: SearchService) { }

  ngOnInit(): void {
  }

  searchForm = new FormGroup({
    city: new FormControl(''),
    radius: new FormControl('')
  })

  searchResults!: SearchResultDto[]

  get city() { return this.searchForm.get('city') }
  get radius() { return this.searchForm.get('radius') }

  submit() {
    const city = this.city?.value;
    const radius = this.radius?.value;

    let geoSearchDto = new GeoSearchDto ({
      city,
      radius
    })

    this.searchService.geoSearch(geoSearchDto).subscribe((data: any) => {
      this.searchResults = data
    }, (err: any) => {
      alert(err.error.redirect)
    })
    
  }

}
