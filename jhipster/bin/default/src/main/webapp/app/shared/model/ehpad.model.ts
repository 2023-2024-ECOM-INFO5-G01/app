import { IUser } from 'app/shared/model/user.model';
import { IPatient } from 'app/shared/model/patient.model';

export interface IEhpad {
  id?: number;
  nom?: string | null;
  users?: IUser[] | null;
  patients?: IPatient[] | null;
}

export const defaultValue: Readonly<IEhpad> = {};
