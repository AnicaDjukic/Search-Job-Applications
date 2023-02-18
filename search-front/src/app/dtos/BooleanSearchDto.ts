export class BooleanSearchDto {
    field1: string
    value1: string
    field2: string
    value2: string
    operation: string

    constructor({ field1, value1, field2, value2, operation }: any) {
        this.field1 = field1;
        this.value1 = value1;
        this.field2 = field2;
        this.value2 = value2;
        this.operation = operation
      }
}