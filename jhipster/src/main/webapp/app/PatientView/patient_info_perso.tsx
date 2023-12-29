import {Translate} from "react-jhipster";
import React, {useEffect, useState} from "react";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {IAlerte} from "app/shared/model/alerte.model";
import {createEntity} from "app/entities/alerte/alerte.reducer";
import {useForm} from "react-hook-form";
import { getIMC } from "app/entities/imc/imc.reducer";
import { getPoids } from "app/entities/poids/poids.reducer";
import { getEpas } from "app/entities/epa/epa.reducer";
import {IPoids} from "app/shared/model/poids.model";
import {IIMC} from "app/shared/model/imc.model";
import {IEPA} from "app/shared/model/epa.model";
import { PayloadAction } from "@reduxjs/toolkit";

export const PatientInfoPerso = (props) => {
  const dispatch = useAppDispatch()

  const account = useAppSelector(state => state.authentication.account);

  const alerteEntity = useAppSelector(state => state.alerte.entity);

  const [lastImc, setLastImc] = useState<IIMC | null>(null);
  const [lastPoids, setLastPoids] = useState<IPoids | null>(null);
  const [lastEpa, setLastEpa] = useState<IEPA | null>(null);

  useEffect(() => {
    if (props.patientEntity.id) {
      dispatch(getIMC(props.patientEntity.id)).then((response: PayloadAction<any>) => {
        if (response.payload && response.payload.data && response.payload.data.length > 0) {
         setLastImc(response.payload.data[response.payload.data.length - 1]);
        }
      });

      dispatch(getPoids(props.patientEntity.id)).then((response: PayloadAction<any>) => {
        if (response.payload && response.payload.data && response.payload.data.length > 0) {
          setLastPoids(response.payload.data[response.payload.data.length - 1]);
        }
      });

      dispatch(getEpas(props.patientEntity.id)).then((response: PayloadAction<any>) => {
        if (response.payload && response.payload.data && response.payload.data.length > 0) {
          setLastEpa(response.payload.data[response.payload.data.length - 1]);
        }
      });
    }
  }, [dispatch, props.patientEntity.id]);

  return (
    <div className="info_patient_perso">
      <div>
        <div>
          <span id="taille">
          <Translate contentKey="ecomApp.patient.taille"></Translate>{props.patientEntity.taille} cm
        </span>
        </div>
        <div>
        <span id="iMC">
          <Translate contentKey="ecomApp.patient.iMC"></Translate>{lastImc ? lastImc.imc : ''}
        </span>
      </div>
      <div>
        <span id="poids">
          <Translate contentKey="ecomApp.patient.poids"></Translate>{lastPoids ? lastPoids.poids : ''} kg
        </span>
      </div>
      <div>
        <span id="ePA">
          <Translate contentKey="ecomApp.patient.ePA"></Translate>{lastEpa ? lastEpa.epa : ''}
        </span>
      </div>
        <div>
        <span id="albumine">
          <Translate
            contentKey="ecomApp.patient.albumine"></Translate>{props.patientEntity.albumine ? props.patientEntity.albumine.albu : ''}
        </span>
        </div>
      </div>
    </div>)
}
