import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRepas } from 'app/shared/model/repas.model';
import { getEntities as getRepas } from 'app/entities/repas/repas.reducer';
import { IAliment } from 'app/shared/model/aliment.model';
import { getEntity, updateEntity, createEntity, reset } from './aliment.reducer';

export const AlimentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const repas = useAppSelector(state => state.repas.entities);
  const alimentEntity = useAppSelector(state => state.aliment.entity);
  const loading = useAppSelector(state => state.aliment.loading);
  const updating = useAppSelector(state => state.aliment.updating);
  const updateSuccess = useAppSelector(state => state.aliment.updateSuccess);

  const handleClose = () => {
    navigate('/aliment');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getRepas({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...alimentEntity,
      ...values,
      repas: repas.find(it => it.id.toString() === values.repas.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...alimentEntity,
          repas: alimentEntity?.repas?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecomApp.aliment.home.createOrEditLabel" data-cy="AlimentCreateUpdateHeading">
            <Translate contentKey="ecomApp.aliment.home.createOrEditLabel">Create or edit a Aliment</Translate>
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
                  id="aliment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('ecomApp.aliment.nom')} id="aliment-nom" name="nom" data-cy="nom" type="text" />
              <ValidatedField
                label={translate('ecomApp.aliment.calories')}
                id="aliment-calories"
                name="calories"
                data-cy="calories"
                type="text"
              />
              <ValidatedField id="aliment-repas" name="repas" data-cy="repas" label={translate('ecomApp.aliment.repas')} type="select">
                <option value="" key="0" />
                {repas
                  ? repas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/aliment" replace color="info">
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

export default AlimentUpdate;
