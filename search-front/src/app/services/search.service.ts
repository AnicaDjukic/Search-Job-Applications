import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BooleanSearchDto } from '../dtos/BooleanSearchDto';
import { GeoSearchDto } from '../dtos/GeoSearchDto';
import { AdvancedSearchDto } from '../dtos/AdvancedSearchDto';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private url = "http://localhost:8080/api/v1/applications/search"

  constructor(private http: HttpClient) { }

  simpleSearch(field: string, text: string ) {
    return this.http.get(`${this.url}?field=${field}&text=${text}`)
  }

  booleanSearch(booleanSearchDto: BooleanSearchDto) {
    return this.http.post(`${this.url}/boolean`, booleanSearchDto)
  }

  geoSearch(geoSearchDto: GeoSearchDto) {
    return this.http.post(`${this.url}/geo`, geoSearchDto)
  }

  advancedSearch(advancedSearchDto: AdvancedSearchDto) {
    return this.http.post(`${this.url}/advanced`, advancedSearchDto)
  }
}
