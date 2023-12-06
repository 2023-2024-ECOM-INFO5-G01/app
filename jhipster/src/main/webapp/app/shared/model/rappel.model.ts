import { IPatient } from 'app/shared/model/patient.model';
import { IUser } from 'app/shared/model/user.model';

export interface IRappel {
  id?: number;
  date?: string | null;
  action?: string | null;
  verif?: boolean | null;
  patient?: IPatient | null;
  users?: IUser[] | null;
}

export const defaultValue: Readonly<IRappel> = {
  verif: false,
};
