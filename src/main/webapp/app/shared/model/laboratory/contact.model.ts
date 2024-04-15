import { AdministrativeGender } from 'app/shared/model/enumerations/administrative-gender.model';
import { IAddress } from 'app/shared/model/laboratory/address.model';
import { IHumanName } from 'app/shared/model/laboratory/human-name.model';

export interface IContact {
  id?: string;
  gender?: AdministrativeGender;
  name?: IHumanName;
  address?: IAddress;
}

export const defaultValue: Readonly<IContact> = {};
