import { IUser } from 'app/shared/model/user.model';
import { IPatient } from 'app/shared/model/patient.model';

export interface IAlerte {
  id?: number;
  date?: string | null;
  action?: string | null;
  verif?: boolean | null;
  user?: IUser | null;
  patient?: IPatient | null;
}

export const defaultValue: Readonly<IAlerte> = {
  verif: false,
};
