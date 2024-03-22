export interface IFieldFormat {
  id?: string;
  name?: string;
  dataType?: string;
  isRequired?: boolean;
  isSearchable?: boolean;
  defaultValue?: string;
  referenceValue?: string;
  valueSet?: string;
  order?: number;
}

export const defaultValue: Readonly<IFieldFormat> = {};
