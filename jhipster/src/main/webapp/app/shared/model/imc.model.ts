import { IPatient } from 'app/shared/model/patient.model';

export interface IIMC {
  id?: number;
  imc?: number | null;
  date?: string | null;
  patient?: IPatient | null;
}

export const defaultValue: Readonly<IIMC> = {};
