import { IUser } from 'app/shared/model/user.model';
import { IPatient } from 'app/shared/model/patient.model';

export interface INote {
  id?: number;
  date?: string | null;
  note?: string | null;
  titre?: string | null;
  user?: IUser | null;
  patient?: IPatient | null;
}

export const defaultValue: Readonly<INote> = {};
