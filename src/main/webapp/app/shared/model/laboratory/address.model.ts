export interface IAddress {
  id?: string;
  text?: string;
  line?: string;
  city?: string;
  state?: string;
  district?: string;
  country?: string;
}

export const defaultValue: Readonly<IAddress> = {};
