import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './alerte.reducer';

export const AlerteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const alerteEntity = useAppSelector(state => state.alerte.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="alerteDetailsHeading">
          <Translate contentKey="ecomApp.alerte.detail.title">Alerte</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{alerteEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="ecomApp.alerte.date">Date</Translate>
            </span>
          </dt>
          <dd>{alerteEntity.date ? <TextFormat value={alerteEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="action">
              <Translate contentKey="ecomApp.alerte.action">Action</Translate>
            </span>
          </dt>
          <dd>{alerteEntity.action}</dd>
          <dt>
            <span id="verif">
              <Translate contentKey="ecomApp.alerte.verif">Verif</Translate>
            </span>
          </dt>
          <dd>{alerteEntity.verif ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="ecomApp.alerte.user">User</Translate>
          </dt>
          <dd>{alerteEntity.user ? alerteEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="ecomApp.alerte.patient">Patient</Translate>
          </dt>
          <dd>{alerteEntity.patient ? alerteEntity.patient.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/alerte" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/alerte/${alerteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AlerteDetail;
