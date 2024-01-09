import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities,getPoids } from './poids.reducer';
import { IPoids } from 'app/shared/model/poids.model';
export const Poids = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(location, 'id'), location.search));

  const poidsList = useAppSelector(state => state.poids.entities);
  const loading = useAppSelector(state => state.poids.loading);


  const [patientId, setPatientId] = useState(''); // État local pour stocker l'ID du patient
  const [poidsData, setPoidsData] = useState([]);

  const handleRunPatient = () => {
    // Appelez l'action getPoidsByPatientId avec l'ID du patient
    dispatch(getPoids(patientId)).then((response) => {
      if (response.payload && (response.payload as any).data) {
        setPoidsData((response.payload as any).data);
      }
    });
  };
  


  
  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="poids-heading" data-cy="PoidsHeading">
        <Translate contentKey="ecomApp.poids.home.title">Poids</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ecomApp.poids.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/poids/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="ecomApp.poids.home.createLabel">Create new Poids</Translate>
          </Link>
        </div>
      </h2>
      <div className="d-flex justify-content-end">
      <input
        type="text"
        placeholder="ID du patient"
        value={patientId}
        onChange={(e) => setPatientId(e.target.value)}
      />
      <Button color="primary" onClick={handleRunPatient}>
        Run Patient
      </Button>
      <h2>Data from getPoids:</h2>
    <ul>
      {poidsData.map((poids, i) => (
        <li key={`poids-${i}`}>{`Poids: ${poids.poids}, Date: ${poids.date}`}</li>
      ))}
    </ul>
    </div>
      <div className="table-responsive">
        {poidsList && poidsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="ecomApp.poids.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('poids')}>
                  <Translate contentKey="ecomApp.poids.poids">Poids</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('poids')} />
                </th>
                <th className="hand" onClick={sort('date')}>
                  <Translate contentKey="ecomApp.poids.date">Date</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('date')} />
                </th>
                <th>
                  <Translate contentKey="ecomApp.poids.patient">Patient</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {poidsList.map((poids, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/poids/${poids.id}`} color="link" size="sm">
                      {poids.id}
                    </Button>
                  </td>
                  <td>{poids.poids}</td>
                  <td>{poids.date ? <TextFormat type="date" value={poids.date} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{poids.patient ? <Link to={`/patient/${poids.patient.id}`}>{poids.patient.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/poids/${poids.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/poids/${poids.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/poids/${poids.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="ecomApp.poids.home.notFound">No Poids found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Poids;
