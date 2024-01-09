import {TextFormat, Translate} from "react-jhipster";
import {APP_DATE_FORMAT} from "app/config/constants";
import React from "react";


export const PatientInfoAdmin = (props) => {

  return <div className="info_patient_administratives">
    <div>
      <div>
        <span id="prenom">
          <Translate contentKey="ecomApp.patient.prenom"></Translate>{props.patientEntity.prenom}
        </span>
      </div>
      <div>
        <span id="nom">
          <Translate contentKey="ecomApp.patient.nom"></Translate>{props.patientEntity.nom}
        </span>
      </div>
      <div>
        <span id="dateNaissance">
          <Translate contentKey="ecomApp.patient.dateNaissance"></Translate>{props.patientEntity.dateNaissance ?
          <TextFormat value={props.patientEntity.dateNaissance} type="date" format={APP_DATE_FORMAT}/> : null}
        </span>
      </div>
    </div>
    <div>
      <div>
        <span id="datearrive">
          <Translate contentKey="ecomApp.patient.datearrive"></Translate>{props.patientEntity.datearrive ?
          <TextFormat value={props.patientEntity.datearrive} type="date" format={APP_DATE_FORMAT}/> : null}
        </span>
      </div>
      <div>
        <label>Statut : {props.patientEntity.statut}</label>
      </div>
    </div>
  </div>
}
