import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './aliment.reducer';

export const AlimentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const alimentEntity = useAppSelector(state => state.aliment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="alimentDetailsHeading">
          <Translate contentKey="ecomApp.aliment.detail.title">Aliment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{alimentEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="ecomApp.aliment.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{alimentEntity.nom}</dd>
          <dt>
            <span id="calories">
              <Translate contentKey="ecomApp.aliment.calories">Calories</Translate>
            </span>
          </dt>
          <dd>{alimentEntity.calories}</dd>
          <dt>
            <Translate contentKey="ecomApp.aliment.repas">Repas</Translate>
          </dt>
          <dd>{alimentEntity.repas ? alimentEntity.repas.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/aliment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/aliment/${alimentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AlimentDetail;
