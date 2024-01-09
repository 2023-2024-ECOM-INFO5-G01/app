// PatientCard.tsx
import React from 'react';
import { Row, Col, Alert, Button, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { faSort, faSortUp, faSortDown, faStarAndCrescent, faBell } from '@fortawesome/free-solid-svg-icons';
import { Translate, TextFormat, getSortState } from 'react-jhipster';

const PatientCard = ({ patient,alertes }) => {
  const patientAlertes = alertes.filter(alerte => alerte.patient.id === patient.id);

  return (
    <div style={{ display: 'flex', flexDirection: 'column' }}>
      <div className="patient-info">
        <p>
          <strong> Patient  </strong> : {patient.nom} {patient.prenom}
          <strong> EPA </strong> : {patient.ePA}
        <Button tag={Link} to={`/patients/${patient.id}`} color="primary" size="sm" data-cy="entityDetailsButton" className="view_data_button">
            <FontAwesomeIcon icon="eye" />
        </Button>
        </p>
        {patientAlertes.length > 0 && (
                      <FontAwesomeIcon icon={faBell} className="blinking-icon" />
)}
        {patientAlertes.map((alerte, index) => (
          <div key={index}>
            <p>Alertes : {alerte.action}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default PatientCard;