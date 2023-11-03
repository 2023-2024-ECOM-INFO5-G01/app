import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './epa.reducer';

export const EPADetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ePAEntity = useAppSelector(state => state.ePA.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ePADetailsHeading">
          <Translate contentKey="ecomApp.ePA.detail.title">EPA</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ePAEntity.id}</dd>
          <dt>
            <span id="epa">
              <Translate contentKey="ecomApp.ePA.epa">Epa</Translate>
            </span>
          </dt>
          <dd>{ePAEntity.epa}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="ecomApp.ePA.date">Date</Translate>
            </span>
          </dt>
          <dd>{ePAEntity.date ? <TextFormat value={ePAEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="ecomApp.ePA.patient">Patient</Translate>
          </dt>
          <dd>{ePAEntity.patient ? ePAEntity.patient.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/epa" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/epa/${ePAEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EPADetail;
