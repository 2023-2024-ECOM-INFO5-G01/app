// PatientCard.tsx
import React from 'react';
import { Row, Col, Alert, Button, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { faSort, faSortUp, faSortDown, faStarAndCrescent } from '@fortawesome/free-solid-svg-icons';
import { Translate, TextFormat, getSortState } from 'react-jhipster';

const PatientCard = ({ patient }) => {
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
      </div>
      <div className="patient-actions">
                  <Button tag={Link} to={`/patients/${patient.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <Translate contentKey="entity.action.view">View</Translate>
                  </Button>
                  <Button tag={Link} to={`/patient/${patient.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <Translate contentKey="entity.action.edit">Visualiser données</Translate>
                  </Button>
                  <Button tag={Link} to={`/note/${patient.id}`} color="primary" size="sm" data-cy="entityEditButton">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <Translate contentKey="entity.action.note">Note</Translate>
                  </Button>
                </div>
    </div>
  );
};

export default PatientCard;