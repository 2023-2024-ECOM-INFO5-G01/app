import {Col} from "reactstrap";
import React from "react";
import {PatientInfoAdmin} from "app/PatientView/patient_info_admin";
import {PatientInfoPerso} from "app/PatientView/patient_info_perso";
import {AddDataPopup} from "app/PatientView/add_data_popup";


export const PatientThumbnail = (props) => {

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

  const handleAddAdministrativeInfo = () => {
    //place holder
  }

  const handleAddData = () => {
    props.setbackground(<AddDataPopup/>);
  }

  const handleAddTask = () => {
    //place holder
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
        <button className="bouton_modif_patient" onClick={handleAddAdministrativeInfo}>
          <img src="../../content/images/icons8-plus-50.png" alt="Icon svg plus" className="img_patient_plus"/>
          Données administratives
        </button>
        <button className="bouton_modif_patient" onClick={handleAddData}>
          <img src="../../content/images/icons8-plus-50.png" alt="Icon svg plus" className="img_patient_plus"/>
          Données patient
        </button>
        <button className="bouton_modif_patient" onClick={handleAddTask}>
          <img src="../../content/images/icons8-plus-50.png" alt="Icon svg plus" className="img_patient_plus"/>
          Tâche
        </button>
      </div>
    </Col>
  )
}
