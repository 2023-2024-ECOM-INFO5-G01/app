import {TextFormat, Translate} from "react-jhipster";
import {APP_LOCAL_DATE_FORMAT} from "app/config/constants";
import React from "react";
import { useAppDispatch } from "app/config/store";
import { useParams } from "react-router-dom";
import { updateStatus } from "app/entities/patient/patient.reducer";



export const PatientInfoAdmin = (props) => {

  const dispatch = useAppDispatch();
  const {id} = useParams<'id'>();

  // Options de statut disponibles
  const optionsStatut = ['normal', 'surveillance', 'dénutrition avérée'];

  // Gestionnaire d'événements pour la modification du statut
  const handleStatutChange = (event) => {
    const nouveauStatutSelectionne = event.target.value;
    // changement de statut avec le nouveau statut
    dispatch(updateStatus({id, statut: nouveauStatutSelectionne}));
    props.setStatus(true);
    // Actualiser la page
    window.location.reload();
  };

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
          <TextFormat value={props.patientEntity.dateNaissance} type="date" format={APP_LOCAL_DATE_FORMAT}/> : null}
        </span>
      </div>
    </div>
    <div>
      <div>
        <span id="datearrive">
          <Translate contentKey="ecomApp.patient.datearrive"></Translate>{props.patientEntity.datearrive ?
          <TextFormat value={props.patientEntity.datearrive} type="date" format={APP_LOCAL_DATE_FORMAT}/> : null}
        </span>
      </div>
      <div>
        <label>Statut : {props.patientEntity.statut}</label>
      </div>
    </div>
  </div>
}
