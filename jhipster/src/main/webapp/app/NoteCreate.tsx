import { createEntity } from 'app/entities/note/note.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getAccount } from 'app/shared/reducers/authentication';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getPatients } from 'app/entities/patient/patient.reducer';

export const NoteCreate = ({ modal, toggle ,patientId}: { modal: boolean; toggle: () => void; patientId: string }) => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const updateSuccess = useAppSelector(state => state.note.updateSuccess);
  const navigate = useNavigate();
  const noteEntity = useAppSelector(state => state.note.entity);
  const patients = useAppSelector(state => state.patient.entities);
  const users = useAppSelector(state => state.userManagement.users);

  const saveEntity = values => {
    console.log("users", users);
    console.log("account", account.id);
    const date = new Date();
    const entity = {
      ...noteEntity,
      ...values,
      user: users.find(it => it.id.toString() === account.id.toString()),
      patient: patients.find(it => it.id.toString() === patientId),
      date: date,
    };
    console.log(entity);
    dispatch(createEntity(entity));
  };
  const handleClose = () => {
    navigate(`/patients/${patientId}`);
  };

  useEffect(() => {
    dispatch(getUsers({}));
    dispatch(getPatients({}));
  }
  , []);
  useEffect(() => {
    if (updateSuccess) {
      handleClose();
      toggle();
    }
  }, [updateSuccess]);

  return (
    <Modal isOpen={modal} toggle={toggle}>
    <ModalHeader toggle={toggle}>Create Rappel</ModalHeader>
    <ModalBody>
    <ValidatedForm onSubmit={saveEntity}>
      <ValidatedField label={translate('ecomApp.note.titre')} id="note-titre" name="titre" data-cy="titre" type="text" />
      <ValidatedField label={translate('ecomApp.note.note')} id="note-note" name="note" data-cy="note" type="text" />
      <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit">
        <FontAwesomeIcon icon="save" />
        &nbsp;
        <Translate contentKey="entity.action.save">Save</Translate>
      </Button>
    </ValidatedForm>
    </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={toggle}>Cancel</Button>
      </ModalFooter>
    </Modal>
  );
};