import dayjs from 'dayjs';
import { AdministrativeGender } from 'app/shared/model/enumerations/administrative-gender.model';

export interface IPatient {
  id?: string;
  active?: boolean;
  gender?: AdministrativeGender;
  birthDate?: string;
}

export const defaultValue: Readonly<IPatient> = {
  active: false,
};
