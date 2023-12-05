// PatientHeading.tsx
import React from 'react';
import { Translate } from 'react-jhipster';
import { Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';

const PatientHeading = ({ loading, handleSyncList }) => {
  return (
<div className="d-flex justify-content-end">
  <Link
    to="/patient/new"
    className="btn jh-create-entity custom-create-button"
    id="jh-create-entity"
    data-cy="entityCreateButton"
    color='primary'
  >
    <FontAwesomeIcon icon="plus" />
    &nbsp;
    <Translate contentKey="ecomApp.patient.home.createLabel">Create new Patient</Translate>
  </Link>
</div>
  );
};

export default PatientHeading;