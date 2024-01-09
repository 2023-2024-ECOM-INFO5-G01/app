import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './poids.reducer';

export const PoidsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const poidsEntity = useAppSelector(state => state.poids.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="poidsDetailsHeading">
          <Translate contentKey="ecomApp.poids.detail.title">Poids</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{poidsEntity.id}</dd>
          <dt>
            <span id="poids">
              <Translate contentKey="ecomApp.poids.poids">Poids</Translate>
            </span>
          </dt>
          <dd>{poidsEntity.poids}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="ecomApp.poids.date">Date</Translate>
            </span>
          </dt>
          <dd>{poidsEntity.date ? <TextFormat value={poidsEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="ecomApp.poids.patient">Patient</Translate>
          </dt>
          <dd>{poidsEntity.patient ? poidsEntity.patient.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/poids" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/poids/${poidsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PoidsDetail;
