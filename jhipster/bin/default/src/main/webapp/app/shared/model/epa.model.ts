import { IPatient } from 'app/shared/model/patient.model';

export interface IEPA {
  id?: number;
  epa?: number | null;
  date?: string | null;
  patient?: IPatient | null;
}

export const defaultValue: Readonly<IEPA> = {};
