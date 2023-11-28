// PatientHeading.tsx
import React from 'react';
import { Translate } from 'react-jhipster';
import { Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';

const PatientHeading = ({ loading, handleSyncList }) => {
  return (
      <div className="d-flex justify-content-end">
        <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
          <FontAwesomeIcon icon="sync" spin={loading} />{' '}
          <Translate contentKey="ecomApp.patient.home.refreshListLabel">Refresh List</Translate>
        </Button>
        <Link to="/patient/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="ecomApp.patient.home.createLabel">Create new Patient</Translate>
        </Link>
      </div>
  );
};

export default PatientHeading;