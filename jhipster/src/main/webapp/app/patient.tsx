import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './entities/patient/patient.reducer';

export const Patient = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const patientEntity = useAppSelector(state => state.patient.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="patientDetailsHeading">
          <Translate contentKey="ecomApp.patient.detail.title">Patient</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{patientEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="ecomApp.patient.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{patientEntity.nom}</dd>
          <dt>
            <span id="prenom">
              <Translate contentKey="ecomApp.patient.prenom">Prenom</Translate>
            </span>
          </dt>
          <dd>{patientEntity.prenom}</dd>
          <dt>
            <span id="statut">
              <Translate contentKey="ecomApp.patient.statut">Statut</Translate>
            </span>
          </dt>
          <dd>{patientEntity.statut}</dd>
          <dt>
            <span id="dateNaissance">
              <Translate contentKey="ecomApp.patient.dateNaissance">Date Naissance</Translate>
            </span>
          </dt>
          <dd>
            {patientEntity.dateNaissance ? <TextFormat value={patientEntity.dateNaissance} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="taille">
              <Translate contentKey="ecomApp.patient.taille">Taille</Translate>
            </span>
          </dt>
          <dd>{patientEntity.taille}</dd>
          <dt>
            <span id="datearrive">
              <Translate contentKey="ecomApp.patient.datearrive">Datearrive</Translate>
            </span>
          </dt>
          <dd>{patientEntity.datearrive ? <TextFormat value={patientEntity.datearrive} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="ecomApp.patient.albumine">Albumine</Translate>
          </dt>
          <dd>{patientEntity.albumine ? patientEntity.albumine.id : ''}</dd>
          <dt>
            <Translate contentKey="ecomApp.patient.ehpad">Ehpad</Translate>
          </dt>
          <dd>{patientEntity.ehpad ? patientEntity.ehpad.id : ''}</dd>
          <dt>
            <Translate contentKey="ecomApp.patient.user">User</Translate>
          </dt>
          <dd>
            {patientEntity.users
              ? patientEntity.users.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {patientEntity.users && i === patientEntity.users.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/patient/${patientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Visualiser données</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default Patient;
