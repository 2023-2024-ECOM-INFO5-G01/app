import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAlbumine } from 'app/shared/model/albumine.model';
import { getEntities as getAlbumines } from 'app/entities/albumine/albumine.reducer';
import { IEhpad } from 'app/shared/model/ehpad.model';
import { getEntities as getEhpads } from 'app/entities/ehpad/ehpad.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getRoles, getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IPatient } from 'app/shared/model/patient.model';
import { getEntity, updateEntity, createEntity, reset } from './patient.reducer';

export const PatientUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const albumines = useAppSelector(state => state.albumine.entities);
  const ehpads = useAppSelector(state => state.ehpad.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const medecins = useAppSelector(state => state.userManagement.users.medecins)
  const patientEntity = useAppSelector(state => state.patient.entity);
  const loading = useAppSelector(state => state.patient.loading);
  const updating = useAppSelector(state => state.patient.updating);
  const updateSuccess = useAppSelector(state => state.patient.updateSuccess);

  const handleClose = () => {
    navigate('/');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAlbumines({}));
    dispatch(getEhpads({}));
    dispatch(getUsers({}));
    dispatch(getRoles());
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateNaissance = convertDateTimeToServer(values.dateNaissance);
    values.datearrive = convertDateTimeToServer(values.datearrive);

    const entity = {
      ...patientEntity,
      ...values,
      users: mapIdList(values.users),
      albumine: albumines.find(it => it.id.toString() === values.albumine.toString()),
      ehpad: ehpads.find(it => it.id.toString() === values.ehpad.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateNaissance: displayDefaultDateTime(),
          datearrive: displayDefaultDateTime(),
        }
      : {
          ...patientEntity,
          dateNaissance: convertDateTimeFromServer(patientEntity.dateNaissance),
          datearrive: convertDateTimeFromServer(patientEntity.datearrive),
          albumine: patientEntity?.albumine?.id,
          ehpad: patientEntity?.ehpad?.id,
          users: patientEntity?.users?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecomApp.patient.home.createOrEditLabel" data-cy="PatientCreateUpdateHeading">
            <Translate contentKey="ecomApp.patient.home.createOrEditLabel">Create or edit a Patient</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="patient-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('ecomApp.patient.nom')} id="patient-nom" name="nom" data-cy="nom" type="text"  validate={{required: 'Ce champs ne peut pas être vide',}} />
              <ValidatedField label={translate('ecomApp.patient.prenom')} id="patient-prenom" name="prenom" data-cy="prenom" type="text" validate={{required: 'Ce champs ne peut pas être vide',}} />
              <ValidatedField 
              label={translate('ecomApp.patient.statut')} 
              id="patient-statut" 
              name="statut" 
              type='select'
              data-cy="statut"
              validate={{
                required: 'Veuillez choisir une option',
              }}
              >
                  <option value="">Choisir une option</option>
                  <option value="normal">normal</option>
                  <option value="surveillance">surveillance</option>
                  <option value="dénutrition avérée">dénutrition avérée</option>
              </ValidatedField>
              <ValidatedField
                label={translate('ecomApp.patient.dateNaissance')}
                id="patient-dateNaissance"
                name="dateNaissance"
                data-cy="dateNaissance"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{required: 'Ce champs ne peut pas être vide',}}
              />
              <ValidatedField label={translate('ecomApp.patient.taille')} id="patient-taille" name="taille" data-cy="taille" type="text" 
              validate={{
                required: 'Ce champs ne peut pas être vide',
                min: { value: 50, message: 'Choisir une taille entre 50 et 300 cm' },
                max: { value: 300, message: 'Choisir une taille entre 50 et 300 cm' },
                }}
              
              />
              <ValidatedField
                label={translate('ecomApp.patient.datearrive')}
                id="patient-datearrive"
                name="datearrive"
                data-cy="datearrive"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: 'Ce champs ne peut pas être vide',
                }}
              />
              <ValidatedField
                id="patient-albumine"
                name="albumine"
                data-cy="albumine"
                label={translate('ecomApp.patient.albumine')}
                type="select"
                validate={{
                  required: 'Veuillez choisir une option',
                }}
              >
                <option value="" key="0" />
                {albumines
                  ? albumines.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
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
              <ValidatedField
                label= "Médecin"
                id="patient-user"
                data-cy="user"
                type="select"
                multiple
                name="users"
                validate={{
                  required: 'Veuillez choisir un médecin',
                }}
              >
                <option value="" key="0" />
                {users
                  ? users.map((user,i) => (
                      <option value={user.id} key={user.id}>
                        {user.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/patient" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PatientUpdate;
