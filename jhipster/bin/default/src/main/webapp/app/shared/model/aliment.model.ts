import { IRepas } from 'app/shared/model/repas.model';

export interface IAliment {
  id?: number;
  nom?: string | null;
  calories?: number | null;
  repas?: IRepas | null;
}

export const defaultValue: Readonly<IAliment> = {};
