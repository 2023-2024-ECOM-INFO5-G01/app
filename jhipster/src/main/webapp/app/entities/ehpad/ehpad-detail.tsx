import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ehpad.reducer';

export const EhpadDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ehpadEntity = useAppSelector(state => state.ehpad.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ehpadDetailsHeading">
          <Translate contentKey="ecomApp.ehpad.detail.title">Ehpad</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ehpadEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="ecomApp.ehpad.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{ehpadEntity.nom}</dd>
          <dt>
            <Translate contentKey="ecomApp.ehpad.user">User</Translate>
          </dt>
          <dd>
            {ehpadEntity.users
              ? ehpadEntity.users.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {ehpadEntity.users && i === ehpadEntity.users.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/ehpad" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ehpad/${ehpadEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EhpadDetail;
