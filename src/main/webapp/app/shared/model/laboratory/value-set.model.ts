import { IConstant } from 'app/shared/model/laboratory/constant.model';
import { DataType } from 'app/shared/model/enumerations/data-type.model';

export interface IValueSet {
  id?: string;
  name?: string;
  description?: string;
  dataType?: DataType;
  values?: IConstant[];
}

export const defaultValue: Readonly<IValueSet> = {};
