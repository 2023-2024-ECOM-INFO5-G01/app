// PatientCard.tsx
import React from 'react';
import { Row, Col, Alert, Button, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { faSort, faSortUp, faSortDown, faStarAndCrescent } from '@fortawesome/free-solid-svg-icons';
import { Translate, TextFormat, getSortState } from 'react-jhipster';

const PatientCard = ({ patient,alertes }) => {
  const patientAlertes = alertes.filter(alerte => alerte.patient.id === patient.id);

  return (
    <div >
      <div className="patient-info">
        <p>
          <strong>Nom : {patient.nom}</strong>
        </p>
        <p>
          <strong>Prénom : {patient.prenom}</strong>
        </p>
        <p>Ehpad : {patient.ehpad ? patient.ehpad.nom : ''}</p>
        {patientAlertes.map((alerte, index) => (
          <div key={index}>
            <p>Alertes : {alerte.action}</p>
          </div>
        ))}
      </div>
      <div className="patient-actions">
        <Button tag={Link} to={`/patients/${patient.id}`} color="primary" size="sm" data-cy="entityDetailsButton" className="view_data_button">
          <FontAwesomeIcon icon="eye" />{' '}
          &nbsp;
          <Translate contentKey="entity.action.view">View</Translate>
        </Button>
      </div>
    </div>
  );
};

export default PatientCard;