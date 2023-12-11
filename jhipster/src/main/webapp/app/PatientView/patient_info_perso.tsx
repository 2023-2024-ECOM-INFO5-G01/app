import {Translate} from "react-jhipster";
import React from "react";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {IAlerte} from "app/shared/model/alerte.model";
import {createEntity} from "app/entities/alerte/alerte.reducer";
import {useForm} from "react-hook-form";


export const PatientInfoPerso = (props) => {
  const dispatch = useAppDispatch()

  const account = useAppSelector(state => state.authentication.account);

  const createAlarmEntity = (userId, patientId) => {
    const currentDate = new Date();
    const entity: IAlerte = {
      date: currentDate.toISOString(),
      user: {id: userId},
      patient: {id: patientId},
      verif: false,
      action: 'epa<7',
    };

    return entity;
  };

  const alerteEntity = useAppSelector(state => state.alerte.entity);


  const handlecreateAlerte = (userid, patientid) => {
    const entity = createAlarmEntity(userid, patientid);
    dispatch(createEntity(entity));
  }
// Inside your Alerte component
  const {register, handleSubmit, reset} = useForm();
//              <button onClick={() => handlecreateAlerte(account.id, patientEntity.id)}>Créer une alerte</button>
// <button onClick={() => handlecreateAlerte(account.id, props.patientEntity.id)}>Créer une alerte</button>
  return (
    <div className="info_patient_perso">
      <div>
        <div>
          <span id="taille">
          <Translate contentKey="ecomApp.patient.taille"></Translate>{props.patientEntity.taille} cm
        </span>
        </div>
        <div>
        <span id="poids">
          <Translate contentKey="ecomApp.patient.poids"></Translate>{props.patientEntity.poids} kg
        </span>
        </div>
      </div>
      <div>
        <div>
        <span id="ePA">
          <Translate contentKey="ecomApp.patient.ePA"></Translate>{props.patientEntity.ePA}
        </span>
        </div>
        <div>
        <span id="iMC">
          <Translate contentKey="ecomApp.patient.iMC"></Translate>{props.patientEntity.iMC}
        </span>
        </div>
        <div>
        <span id="albumine">
          <Translate
            contentKey="ecomApp.patient.albumine"></Translate>{props.patientEntity.albumine ? props.patientEntity.albumine.id : ''}
        </span>
        </div>
      </div>
    </div>)
}
