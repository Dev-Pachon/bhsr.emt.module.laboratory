export interface IConstant {
  id?: string;
  name?: string;
  value?: string | number | boolean;
  description?: string;
}

export const defaultValue: Readonly<IConstant> = {};
