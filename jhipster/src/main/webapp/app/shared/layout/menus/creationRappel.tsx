import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from '../../util/date-utils'
import { mapIdList } from '../../util/entity-utils';
import { useAppDispatch, useAppSelector } from '../../../config/store';

import { IUser } from '../../model/user.model';
import { getUsers } from '../../../modules/administration/user-management/user-management.reducer';
import { IPatient } from '../../model/patient.model';
import { getEntities as getPatients } from '../../../entities/patient/patient.reducer';
import { IRappel } from '../../model/rappel.model';
import { getEntity, updateEntity, createEntity, reset } from '../../../entities/rappel/rappel.reducer';
export const CreationRappel = ({ modal, toggle ,idprops}: { modal: boolean; toggle: () => void; idprops: string }) => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const patients = useAppSelector(state => state.patient.entities);
  const rappelEntity = useAppSelector(state => state.rappel.entity);
  const loading = useAppSelector(state => state.rappel.loading);
  const updating = useAppSelector(state => state.rappel.updating);
  const updateSuccess = useAppSelector(state => state.rappel.updateSuccess);
  const handleClose = () => {
    navigate('/rappel');
  };

  useEffect(() => {
      dispatch(reset());
    dispatch(getUsers({}));
    dispatch(getPatients({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...rappelEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      patient: patients.find(it => it.id.toString() === idprops),
    };
      dispatch(createEntity(entity));

  };

  const defaultValues = () =>
    isNew
      ? {
          date: displayDefaultDateTime(),
        }
      : {
          ...rappelEntity,
          date: convertDateTimeFromServer(rappelEntity.date),
          user: rappelEntity?.user?.id,
          patient: rappelEntity?.patient?.id,
        };

  return (
    <Modal isOpen={modal} toggle={toggle}>
    <ModalHeader toggle={toggle}>Create Rappel</ModalHeader>
    <ModalBody>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              <ValidatedField
                label={translate('ecomApp.rappel.date')}
                id="rappel-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
<ValidatedField id="rappel-action" name="action" data-cy="action" label={translate('ecomApp.rappel.action')} type="select">
  <option value="surveillance">Surveillance</option>
  <option value="prise de poids">Prise de poids</option>
</ValidatedField>
              <ValidatedField
                label={translate('ecomApp.rappel.verif')}
                id="rappel-verif"
                name="verif"
                data-cy="verif"
                check
                type="checkbox"
              />
              <ValidatedField id="rappel-user" name="user" data-cy="user" label={translate('ecomApp.rappel.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/rappel" replace color="info">
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
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={toggle}>Cancel</Button>
      </ModalFooter>
    </Modal>
  );
};

export default CreationRappel;
