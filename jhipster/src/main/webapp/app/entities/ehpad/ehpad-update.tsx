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
import { getEntity, updateEntity, createEntity, reset } from './ehpad.reducer';

export const EhpadUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const ehpadEntity = useAppSelector(state => state.ehpad.entity);
  const loading = useAppSelector(state => state.ehpad.loading);
  const updating = useAppSelector(state => state.ehpad.updating);
  const updateSuccess = useAppSelector(state => state.ehpad.updateSuccess);

  const handleClose = () => {
    navigate('/');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);
  const [selectedUsers, setSelectedUsers] = useState<IUser[]>([]);

  const saveEntity = values => {
    const entity = {
      ...ehpadEntity,
      ...values,
      users: mapIdList(values.users),
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
          ...ehpadEntity,
          users: ehpadEntity?.users?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecomApp.ehpad.home.createOrEditLabel" data-cy="EhpadCreateUpdateHeading">
            <Translate contentKey="ecomApp.ehpad.home.createOrEditLabel">Create or edit a Ehpad</Translate>
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
                  id="ehpad-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('ecomApp.ehpad.nom')} id="ehpad-nom" name="nom" data-cy="nom" type="text" />
              <ValidatedField
  label= "Users attribué"
  id="patient-user"
  data-cy="user"
  type="select"
  multiple
  name="users"
  onChange={event => {
    const target = event.target as unknown as HTMLSelectElement;
    const selectedOptions = Array.from(target.options)
      .filter(option => option.selected)
      .map(option => option.value);

    const newSelectedUsers = selectedOptions.map(userId =>
      users.find(user => user.id.toString() === userId)
    ).filter(Boolean);

    setSelectedUsers(newSelectedUsers);
  }}
  validate={{
    required: 'Veuillez choisir un médecin et/ou des soignants',
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ehpad" replace color="info">
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

export default EhpadUpdate;
