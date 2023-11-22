import dayjs from 'dayjs';
import { AdministrativeGender } from 'app/shared/model/enumerations/administrative-gender.model';
import { IAddress } from 'app/shared/model/laboratory/address.model';
import { IContact } from 'app/shared/model/laboratory/contact.model';
import { IHumanName } from 'app/shared/model/laboratory/human-name.model';
import { IIdentifierType } from 'app/shared/model/laboratory/identifier-type.model';

export interface IPatient {
  id?: string;
  identifierType?: IIdentifierType;
  identifier?: string;
  active?: boolean;
  gender?: AdministrativeGender;
  birthDate?: string;
  address?: IAddress;
  contact?: IContact;
  name?: IHumanName;
}

export const defaultValue: Readonly<IPatient> = {
  active: false,
};
