import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IEhpad } from 'app/shared/model/ehpad.model';
import { getEntity, updateEntity, createEntity, reset,deleteEntity } from './ehpad.reducer';
import { getEntities as getEhpads } from 'app/entities/ehpad/ehpad.reducer';
import {deletePatientsByEhpad} from 'app/entities/patient/patient.reducer';
export const EhpadDelete = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;
  const ehpads = useAppSelector(state => state.ehpad.entities);

  const users = useAppSelector(state => state.userManagement.users);
  const ehpadEntity = useAppSelector(state => state.ehpad.entity);
  const loading = useAppSelector(state => state.ehpad.loading);
  const updating = useAppSelector(state => state.ehpad.updating);
  const updateSuccess = useAppSelector(state => state.ehpad.updateSuccess);

  const handleClose = () => {
    navigate('/');
  };

  useEffect(() => {
    dispatch(getEhpads({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...ehpadEntity,
      ...values,
    };
    dispatch(deletePatientsByEhpad(entity.ehpad)).then(() => {
        dispatch(deleteEntity(entity.ehpad));
        });
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...ehpadEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecomApp.ehpad.home.createOrEditLabel" data-cy="EhpadCreateUpdateHeading">
            <Translate contentKey="ecomApp.ehpad.home.deletehpad">Supprimer un Ehpad</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
            <ValidatedField
              id="patient-ehpad"
              name="ehpad"
              data-cy="ehpad"
              label={translate('ecomApp.patient.ehpad')}
              type="select"
              validate={{
                required: 'Veuillez choisir une option',
              }}
              >
                <option value="" key="0" />
                {ehpads
                  ? ehpads.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nom}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
                            &nbsp;
              <Button color="danger" id="delete-entity" data-cy="entityDeleteButton" type="submit" disabled={updating}>
  <FontAwesomeIcon icon="trash" />
  &nbsp;
  <Translate contentKey="entity.action.delete">Delete</Translate>
</Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default EhpadDelete;
