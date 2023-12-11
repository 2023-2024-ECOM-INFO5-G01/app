import React, { useState } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Col } from 'reactstrap';
import { PatientInfoAdmin } from 'app/PatientView/patient_info_admin';
import { PatientInfoPerso } from 'app/PatientView/patient_info_perso';
import CreationRappel from '../shared/layout/menus/creationRappel';
import { Link, useNavigate, useParams } from 'react-router-dom';
import PatientEdit from './patient_edit';
import {AddDataPopup} from "app/PatientView/add_data_popup";
export const PatientThumbnail = (props) => {
  const account = useAppSelector(state => state.authentication.account);
  const userHasRequiredRole = account.authorities.some(role => ['ROLE_MEDECIN', 'ROLE_ADMIN'].includes(role));

  const getCardColorClass = (status) => {
    switch (status) {
      case 'dénutrition avérée':
        return 'info-card red';
      case 'surveillance':
        return 'info-card orange';
      case 'normal':
        return 'info-card blue';
      default:
        return '';
    }
  };
  const [modal, setModal] = useState(false);
  const toggle = () => setModal(!modal);

  const [modal2, setModal2] = useState(false);
  const toggle2 = () => setModal2(!modal2);
  const navigate = useNavigate();

  const editpatient = () => {
    navigate(`/patient/edit/${props.idprops}`);
  }

  const handleAddData = () => {
    props.setbackground(<AddDataPopup/>);
  }

  return (
    <Col className="fixed-flex-container">
      <div className={`patient-card ${getCardColorClass(props.patientEntity.statut)}`}>
        <div className="flex_div">
          <img src="../../content/images/logo.jpeg" alt="Photo du patient" className="photo_patient"/>
          <button onClick={props.togglePosition} className={`button_${props.isFixed ? 'release' : 'clicked'}`}>
            <img src="../../content/images/pin.svg" alt="Punaise" className="img_patient_pin"/>
          </button>
        </div>
        <PatientInfoAdmin patientEntity={props.patientEntity}/>
      </div>
      <PatientInfoPerso patientEntity={props.patientEntity}/>
      <div className="info_buttons">
        {userHasRequiredRole && (
          <>
            <button className="bouton_modif_patient" onClick={toggle2}>
              <img src="../../content/images/icons8-plus-50.png" alt="Icon svg plus" className="img_patient_plus"/>
              Données administratives
            </button>
            <PatientEdit modal={modal2} toggle={toggle2} idprops={props.idprops}/>
            </>
        )}
        <button className="bouton_modif_patient" onClick={handleAddData}>
          <img src="../../content/images/icons8-plus-50.png" alt="Icon svg plus" className="img_patient_plus"/>
          Données patient
        </button>
        {userHasRequiredRole && (
          <>
            <button className="bouton_modif_patient" onClick={toggle}>
              <img src="../../content/images/icons8-plus-50.png" alt="Icon svg plus" className="img_patient_plus"/>
              Tâche
            </button>
            <CreationRappel modal={modal} toggle={toggle} idprops={props.idprops}/>
            </>
        )}

      </div>
    </Col>
  )
}
