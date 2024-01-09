import { IPatient } from 'app/shared/model/patient.model';

export interface IAlbumine {
  id?: number;
  albu?: number | null;
  date?: string | null;
  patient?: IPatient | null;
}

export const defaultValue: Readonly<IAlbumine> = {};
