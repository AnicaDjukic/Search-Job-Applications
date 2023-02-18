export class GeoSearchDto {
    city: string
    radius: number

    constructor({ city, radius }: any) {
        this.city = city;
        this.radius = radius;
      }
}