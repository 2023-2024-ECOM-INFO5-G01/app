import { IPatient } from 'app/shared/model/patient.model';

export interface IPoids {
  id?: number;
  poids?: number | null;
  date?: string | null;
  patient?: IPatient | null;
}

export const defaultValue: Readonly<IPoids> = {};
