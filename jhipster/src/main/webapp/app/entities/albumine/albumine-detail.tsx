import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './albumine.reducer';

export const AlbumineDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const albumineEntity = useAppSelector(state => state.albumine.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="albumineDetailsHeading">
          <Translate contentKey="ecomApp.albumine.detail.title">Albumine</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{albumineEntity.id}</dd>
          <dt>
            <span id="albu">
              <Translate contentKey="ecomApp.albumine.albu">Albu</Translate>
            </span>
          </dt>
          <dd>{albumineEntity.albu}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="ecomApp.albumine.date">Date</Translate>
            </span>
          </dt>
          <dd>{albumineEntity.date ? <TextFormat value={albumineEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/albumine" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/albumine/${albumineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AlbumineDetail;
