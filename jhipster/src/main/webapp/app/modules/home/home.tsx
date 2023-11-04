import './home.scss';

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { Row, Col, Alert, Button, Table } from 'reactstrap';
import TestButton from './TestButton';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities,getPatientSearch ,getPatientsByUserId} from 'app/entities/patient/patient.reducer';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(location, 'id'), location.search));

  const patientList = useAppSelector(state => state.patient.entities);
  const loading = useAppSelector(state => state.patient.loading);
  const getAllEntities = () => {
    dispatch(
      getPatientsByUserId({
        sort: `${sortState.sort},${sortState.order}`,
        login: account?.login,
      }),
    );
  };

  const [patientsearch, setPatientsearch] = useState(''); // État local pour stocker l'ID du patient
  const [patientsuggestion, setpatientsuggestion] = useState([]);

  const handleRunPatient = () => {
    if (patientsearch != '') {
    // Appelez l'action getPoidsByPatientId avec l'ID du patient
    dispatch(getPatientSearch(patientsearch)).then((response) => {
      console.log(response);
      if (response.payload && (response.payload as any).data) {
        setpatientsuggestion((response.payload as any).data);
      }
    });
  }
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
      {account?.login ? (
        <div>
          <h2 id="patient-heading" data-cy="PatientHeading">
            <Translate contentKey="home.title">Accueil</Translate>
            <div className="d-flex justify-content-end">
              <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
                <FontAwesomeIcon icon="sync" spin={loading} />{' '}
                <Translate contentKey="ecomApp.patient.home.refreshListLabel">Refresh List</Translate>
              </Button>
              <Link to="/patient/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
                <FontAwesomeIcon icon="plus" />
                &nbsp;
                <Translate contentKey="ecomApp.patient.home.createLabel">Create new Patient</Translate>
              </Link>
            </div>
          </h2>
          <div className="d-flex justify-content-end">
          <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="ecomApp.patient.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nom')}>
                  <Translate contentKey="ecomApp.patient.nom">Nom</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('nom')} />
                </th>
                <th className="hand" onClick={sort('prenom')}>
                  <Translate contentKey="ecomApp.patient.prenom">Prenom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('prenom')} />
                </th>
                <th className="hand" onClick={sort('statut')}>
                  <Translate contentKey="ecomApp.patient.statut">Statut</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('statut')} />
                </th>
                <th className="hand" onClick={sort('dateNaissance')}>
                  <Translate contentKey="ecomApp.patient.dateNaissance">Date Naissance</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dateNaissance')} />
                </th>
                <th className="hand" onClick={sort('taille')}>
                  <Translate contentKey="ecomApp.patient.taille">Taille</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('taille')} />
                </th>
                <th className="hand" onClick={sort('datearrive')}>
                  <Translate contentKey="ecomApp.patient.datearrive">Datearrive</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('datearrive')} />
                </th>
      <input
        type="text"
        placeholder="ID du patient"
        value={patientsearch}
        onChange={(e) => setPatientsearch(e.target.value)}
      />
      <Button color="primary" onClick={handleRunPatient}>
        enter search
      </Button>
    </div>
    {patientsuggestion.length > 0 && (
  <div>
    <h3>Résultats de la recherche :</h3>
    <div className="d-flex flex-wrap">
      {patientsuggestion.map((patient, i) => (
        <div key={`suggested-entity-${i}`} className="patient-card">
          <div className="patient-info">
            <p>
              <strong>
                Nom : {patient.nom}
              </strong>
            </p>
            <p>
              <strong>
                Prénom : {patient.prenom}
              </strong>
            </p>
            <p> Ehpad : {patient.ehpad ? patient.ehpad.nom : ''}</p>
          </div>
        </div>
      ))}
    </div>
  </div>
)}
        
          <div className="d-flex flex-wrap">
            {patientList.map((patient, i) => (
              <div key={`entity-${i}`} className="patient-card">
                <div className="patient-info">
                  <p>
                    <strong>
                      Nom:{patient.nom}
                      </strong>
                  </p>
                  <p>
                  <strong>
                        Prenom: {patient.prenom}
                      </strong>
                  </p>
                  <p> Ehpad:{patient.ehpad ? patient.ehpad.nom : ''}</p>
                </div>
                <div className="patient-actions">
                  <Button tag={Link} to={`/patient/${patient.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <Translate contentKey="entity.action.view">View</Translate>
                  </Button>
                  <Button tag={Link} to={`/patient/${patient.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <Translate contentKey="entity.action.edit">Edit</Translate>
                  </Button>
                  <Button tag={Link} to={`/patient/${patient.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                    <FontAwesomeIcon icon="trash" />{' '}
                    <Translate contentKey="entity.action.delete">Delete</Translate>
                  </Button>
                </div>
              </div>
            ))}
          </div>
        </div>
      ) : (
        <div>
          <Alert color="warning">
            <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>

            <Link to="/login" className="alert-link">
              <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
            </Link>
            <Translate contentKey="global.messages.info.authenticated.suffix">
              , you can try the default accounts:
              <br />- Administrator (login="admin" and password="admin")
              <br />- User (login="user" and password="user").
            </Translate>
          </Alert>

          <Alert color="warning">
            <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>&nbsp;
            <Link to="/account/register" className="alert-link">
              <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
            </Link>
          </Alert>
        </div>
      )}
    </div>
  );
};

export default Home;
