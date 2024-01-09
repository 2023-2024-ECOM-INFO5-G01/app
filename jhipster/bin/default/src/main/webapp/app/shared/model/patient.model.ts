import { IAlbumine } from 'app/shared/model/albumine.model';
import { IEhpad } from 'app/shared/model/ehpad.model';
import { IUser } from 'app/shared/model/user.model';
import { IPoids } from 'app/shared/model/poids.model';
import { IEPA } from 'app/shared/model/epa.model';
import { IIMC } from 'app/shared/model/imc.model';
import { IRepas } from 'app/shared/model/repas.model';
import { IRappel } from 'app/shared/model/rappel.model';
import { INote } from 'app/shared/model/note.model';

export interface IPatient {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  statut?: string | null;
  dateNaissance?: string | null;
  taille?: number | null;
  datearrive?: string | null;
  albumine?: IAlbumine | null;
  ehpad?: IEhpad | null;
  users?: IUser[] | null;
  poids?: IPoids[] | null;
  ePAS?: IEPA[] | null;
  iMCS?: IIMC[] | null;
  repas?: IRepas[] | null;
  rappels?: IRappel[] | null;
  notes?: INote[] | null;
}

export const defaultValue: Readonly<IPatient> = {};
