import './home.scss';

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { Row, Col, Alert, Button, Table } from 'reactstrap';
import TestButton from './TestButton';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown, faStarAndCrescent } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities,getPatientSearch ,getPatientsByUserId} from 'app/entities/patient/patient.reducer';
import { set } from 'lodash';

export const Home = () => {

  const account = useAppSelector(state => state.authentication.account);

  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(() => {
    const storedSortState = JSON.parse(localStorage.getItem('sortState'));
  
    if (storedSortState) {
      // Use the stored sorting state if available
      return storedSortState;
    } else {
      // If no stored sorting state, set the initial state with a default sorting field and order
      return {
        ...overrideSortStateWithQueryParams(getSortState(location, 'id'), location.search),
        order: DESC, // Set the default order to DESC when there is no previous sorting state
      };
    }
  });
  

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
const [selectedEhpadFilter, setSelectedEhpadFilter] = useState(''); // Initialize with an empty Ehpad filter value


const [filteredStatuses, setFilteredStatuses] = useState([]);

const handleStatusFilterChange = (status, isChecked) => {
  if (isChecked) {
    setFilteredStatuses(prevStatuses => [...prevStatuses, status]);
  } else {
    setFilteredStatuses(prevStatuses => prevStatuses.filter(s => s !== status));
  }
};

const filterPatientsByStatus = () => {
  if (filteredStatuses.length === 0) {
    return patientList;
  } else {
    return patientList.filter((patient) => filteredStatuses.includes(patient.statut));
  }
};

const getCardColorClass = (status) => {
  switch (status) {
    case 'dénutrition avérée':
      return 'card-red';
    case 'surveillance':
      return 'card-orange';
    case 'normal':
      return 'card-blue';
    default:
      return '';
  }
};

const getStatusOrder = (status) => {
  switch (status) {
    case 'dénutrition avérée':
      return 1;
    case 'surveillance':
      return 2;
    case 'normal':
      return 3;
    default:
      return '';
  }
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

  const sort = (fieldName) => () => {
    setSortState((prevSortState) => {
      const order = prevSortState
  ? (prevSortState.sort === fieldName ? (prevSortState.order === ASC ? DESC : ASC) : prevSortState.order)
  : DESC;
      return {
        ...prevSortState,
        order,
        sort: fieldName,
      };
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  const filters = ['nom', 'prenom', 'statut', "datearrive"];

  const filterDisplayText = {
    nom: 'Trier par nom',
    prenom: 'Trier par prénom',
    statut: 'Trier par statut',
    datearrive: 'Trier par date d\'arrivée',
  };

  
  
  // État local pour stocker le filtre sélectionné
  const [selectedFilter, setSelectedFilter] = useState('nom');
  useEffect(() => {
    if (selectedFilter) {
      sort(selectedFilter)();
    }
  }, [selectedFilter]);
  sort(selectedFilter);



  return (
    <div>
        <div>
          <h2 id="patient-heading" data-cy="PatientHeading">
            <Translate contentKey="home.title">Patient</Translate>
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
          
    <div>
    <input
        type="text"
        placeholder="nom du patient"
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
<select
  value={selectedEhpadFilter}
  onChange={(e) => {
    setSelectedEhpadFilter(e.target.value);
  }}
>
  <option value="">All</option>
  {[...new Set(patientList.map((patient) => patient.ehpad && patient.ehpad.nom))].filter(Boolean).map((ehpadName: string, index) => (
    <option key={index} value={ehpadName}>
      {ehpadName}
    </option>
  ))}

</select>
    <select
    
      value={selectedFilter}
      onChange={(e) => setSelectedFilter(e.target.value)}
      
    >

      {Object.keys(filterDisplayText).map((filter) => (
        <option key={filter} value={filter}>
          {filterDisplayText[filter]}
        </option>
      ))}
    </select>
      <FontAwesomeIcon icon={getSortIconByFieldName(selectedFilter)} onClick={sort(selectedFilter)} />
      <div>
      <input type="checkbox" id="status1" name="status1" value="dénutrition avérée"
             onChange={(e) => handleStatusFilterChange('dénutrition avérée', e.target.checked)} />
      <label htmlFor="status1"> Dénutrition avérée</label><br/>
      <input type="checkbox" id="status2" name="status2" value="surveillance"
             onChange={(e) => handleStatusFilterChange('surveillance', e.target.checked)} />
      <label htmlFor="status2"> Surveillance</label><br/>
      <input type="checkbox" id="status3" name="status3" value="normal"
             onChange={(e) => handleStatusFilterChange('normal', e.target.checked)} />
      <label htmlFor="status3"> Normal</label><br/>
    </div>


          <div className="d-flex flex-wrap">
          {filterPatientsByStatus()
  .filter((patient) =>
    selectedEhpadFilter === '' || patient.ehpad.nom === selectedEhpadFilter
  )
  .map((patient, i) => (
    <div
    key={`entity-${i}`}
    className={`patient-card ${getCardColorClass(patient.statut)}`} // Apply the card color class
  >
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
                  <Button tag={Link} to={`/patients/${patient.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <Translate contentKey="entity.action.view">View</Translate>
                  </Button>
                  <Button tag={Link} to={`/patient/${patient.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <Translate contentKey="entity.action.edit">Visualiser données</Translate>
                  </Button>
                  <Button tag={Link} to={`/note/${patient.id}`} color="primary" size="sm" data-cy="entityEditButton">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <Translate contentKey="entity.action.note">Note</Translate>
                  </Button>
                </div>
              </div>
            ))}
          </div>
        </div>
    </div>
  );
};

export default Home;