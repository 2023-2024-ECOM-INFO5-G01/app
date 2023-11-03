import { IPatient } from 'app/shared/model/patient.model';
import { IAliment } from 'app/shared/model/aliment.model';

export interface IRepas {
  id?: number;
  date?: string | null;
  calories?: number | null;
  patient?: IPatient | null;
  aliments?: IAliment[] | null;
}

export const defaultValue: Readonly<IRepas> = {};
