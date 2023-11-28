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
import PatientSearch from './PatientSearch';
import PatientCard from './PatientCard';
import EhpadFilter from './EhpadFilter';
import SortFilter from './SortFilter';
import StatusFilter from './StatusFilter';
import PatientList from './PatientList';
import PatientHeading from './PatientHeading';
import PatientSearchResults from './PatientSearchResults';
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

const clearSearch = () => {
  setPatientsearch('');
};

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
 
  const [patientsearch, setPatientsearch] = useState(''); // État local pour stocker l'ID du patient
  const [patientsuggestion, setpatientsuggestion] = useState([]);

  const handleRunPatient = () => {
    if (patientsearch !== '') {
      dispatch(getPatientSearch({ query: patientsearch, login: account.login })).then((response) => {
        console.log("response", response);
        if (response.payload) {
          setpatientsuggestion(response.payload as any[]);
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

  const filters = ['nom', 'prenom', "datearrive"];

  const filterDisplayText = {
    nom: 'Trier par nom',
    prenom: 'Trier par prénom',
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
          <PatientHeading loading={loading} handleSyncList={handleSyncList} />
          <PatientSearch patientsearch={patientsearch} setPatientsearch={setPatientsearch} handleRunPatient={handleRunPatient} />
          <PatientSearchResults patients={patientsuggestion} getCardColorClass={getCardColorClass} onClose={() => setpatientsuggestion([])} onClearSearch={clearSearch} />
          <EhpadFilter patientList={patientList} selectedEhpadFilter={selectedEhpadFilter} setSelectedEhpadFilter={setSelectedEhpadFilter} />
          <SortFilter selectedFilter={selectedFilter} setSelectedFilter={setSelectedFilter} filterDisplayText={filterDisplayText} />
          <FontAwesomeIcon icon={getSortIconByFieldName(selectedFilter)} onClick={sort(selectedFilter)} />
          <StatusFilter handleStatusFilterChange={handleStatusFilterChange} />

          <div className="d-flex flex-wrap">
          {filterPatientsByStatus().filter((patient) => selectedEhpadFilter === '' || patient.ehpad.nom === selectedEhpadFilter).map((patient, i) => (
            <div key={`entity-${i}`} className={`patient-card ${getCardColorClass(patient.statut)}`}>
              <PatientCard patient={patient} />
            </div>
          ))}
          </div>
        </div>
    </div>
  );
};

export default Home;