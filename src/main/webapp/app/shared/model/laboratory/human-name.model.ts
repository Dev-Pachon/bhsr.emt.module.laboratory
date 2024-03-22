export interface IHumanName {
  text?: string;
  given?: string;
  family?: string;
}

export const defaultValue: Readonly<IHumanName> = {};
