export class SearchResultDto {
    firstName: string
    lastName: string
    education: string
    highlight: Date

    constructor({ firstName, lastName, education, highlight }: any) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.education = education;
        this.highlight = highlight;
      }
}