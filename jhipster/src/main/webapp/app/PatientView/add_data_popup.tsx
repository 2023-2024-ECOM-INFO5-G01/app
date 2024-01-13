import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IPatient } from 'app/shared/model/patient.model';
import { IPoids } from 'app/shared/model/poids.model';
import { createEntity as createPoids } from 'app/entities/poids/poids.reducer';
import { createEntity as createEPA } from 'app/entities/epa/epa.reducer';
import { createEntity as createAlbumine } from 'app/entities/albumine/albumine.reducer';
import { createEntity as createIMC } from 'app/entities/imc/imc.reducer';
import { denutrition_cases,alerte_epas } from 'app/entities/patient/patient.reducer';
import { IEPA } from 'app/shared/model/epa.model';
import { IAlbumine } from 'app/shared/model/albumine.model';
import { IIMC } from 'app/shared/model/imc.model';
import {IAlerte} from "app/shared/model/alerte.model";
import {createEntity} from "app/entities/alerte/alerte.reducer";
import {useForm} from "react-hook-form";
export const AddDataPopup = () => {

  const dispatch = useAppDispatch();

  const state = {};

  const patient: IPatient = useAppSelector(s => s.patient.entity);

  const handleButtonClick =  (e) => {
    (async () => {
    const value = state[e];
    switch (e) {
      case 'height': {
        // ajouter taille pour ce patient en bdd
        break;
      }
      case 'weight': {
        const poids: IPoids = {
          date: new Date().toISOString(),
          patient: { id: patient.id },
          poids: value
        };
        await dispatch(createPoids(poids));
        const imc : IIMC = {
          date: new Date().toISOString(),
          patient: { id: patient.id },
          imc: 10000 * poids.poids / (patient.taille * patient.taille)
        };
       await dispatch(createIMC(imc));
        dispatch(denutrition_cases(patient.id));
        break;
      }
      case 'albumin': {
        const albu: IAlbumine = {
          date: new Date().toISOString(),
          patient: { id: patient.id },
          albu: value
        };
        await dispatch(createAlbumine(albu));
        dispatch(denutrition_cases(patient.id));
        break;
      }
      case 'epa': {
        const poids: IEPA = {
          date: new Date().toISOString(),
          patient: { id: patient.id },
          epa: value
        };
     await dispatch(createEPA(poids));
        dispatch(alerte_epas(patient.id));
        break;
      }
      default:
        break;
    }}) ();
  };

  const handleInputChange = (e) => {
    state[e.target.id] = e.target.value;
  };


  const inputField = (name, id) => {
    return <div className="input-row">
      <label>{name} : </label>
      <input type="text" id={id} onChange={handleInputChange} />
      <button onClick={(e) => handleButtonClick(id)}>Ajouter</button>
    </div>;
  };

  return (
    <div className="datapopup" onClick={(e) => e.stopPropagation()}>
      <h1>Nouvelle Mesure</h1>
      {/* {inputField('Taille', 'height')} */}
      {inputField('Poids', 'weight')}
      {inputField('Albumine', 'albumin')}
      {inputField('EPA', 'epa')}

    </div>
  );
};
