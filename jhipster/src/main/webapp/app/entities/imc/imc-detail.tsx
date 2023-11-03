import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './imc.reducer';

export const IMCDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const iMCEntity = useAppSelector(state => state.iMC.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="iMCDetailsHeading">
          <Translate contentKey="ecomApp.iMC.detail.title">IMC</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{iMCEntity.id}</dd>
          <dt>
            <span id="imc">
              <Translate contentKey="ecomApp.iMC.imc">Imc</Translate>
            </span>
          </dt>
          <dd>{iMCEntity.imc}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="ecomApp.iMC.date">Date</Translate>
            </span>
          </dt>
          <dd>{iMCEntity.date ? <TextFormat value={iMCEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="ecomApp.iMC.patient">Patient</Translate>
          </dt>
          <dd>{iMCEntity.patient ? iMCEntity.patient.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/imc" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/imc/${iMCEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IMCDetail;
