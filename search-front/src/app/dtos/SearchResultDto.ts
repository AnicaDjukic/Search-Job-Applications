export class SearchResultDto {
    firstName: string
    lastName: string
    education: string
    highlight: Date
    cvPath: string
    coverLetterPath: string

    constructor({ firstName, lastName, education, highlight, cvPath, coverLetterPath }: any) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.education = education;
        this.highlight = highlight;
        this.cvPath = cvPath;
        this.coverLetterPath = coverLetterPath
      }
}