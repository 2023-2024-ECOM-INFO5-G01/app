// PatientHeading.tsx
import React from 'react';
import { Translate } from 'react-jhipster';
import { Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import { useAppSelector } from 'app/config/store';
const PatientHeading = ({ loading, handleSyncList }) => {
  const account = useAppSelector(state => state.authentication.account);
  const userHasRequiredRole = account.authorities.some(role => ['ROLE_MEDECIN', 'ROLE_ADMIN'].includes(role));
  const userHasRequiredRoleEhpad = account.authorities.some(role => ['ROLE_ADMIN'].includes(role));

  return (
    <div className="d-flex justify-content-end">
      {userHasRequiredRole && (
        <Link
          to="/patient/new"
          className="btn jh-create-entity custom-create-button"
          id="jh-create-entity"
          data-cy="entityCreateButton"
          color='primary'
        >
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="ecomApp.patient.home.createLabel">Create Patient</Translate>
        </Link>
      )}
      {userHasRequiredRoleEhpad && (
        <>
        <Link to="/ehpad/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
        <FontAwesomeIcon icon="plus" />
        &nbsp;
        <Translate contentKey="ecomApp.ehpad.home.createLabel">Create Ehpad</Translate>
      </Link>
      <Link to="/ehpad/supprimer" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
      <FontAwesomeIcon icon="plus" />
      &nbsp;
      <span>Surpprimer un Ehpad</span>
    </Link>
    </>
      )}
    </div>
  );
};

export default PatientHeading;