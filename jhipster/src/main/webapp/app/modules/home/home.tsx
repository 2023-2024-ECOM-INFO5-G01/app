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
import Modal from 'react-modal';
import { getAlertesByUser } from 'app/entities/alerte/alerte.reducer';
import 'react-toastify/dist/ReactToastify.css';
import { toast } from 'react-toastify';
import { ToastContainer } from 'react-toastify';

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

  const [currentAlerte, setCurrentAlerte] = useState([]);

  const [alertes, setAlertes] = useState([]);

 
  useEffect(() => {
    if (account && account.login) {
      dispatch(getAlertesByUser(account.login))
      .then(response => {
        setAlertes((response.payload as any).data);
        console.log(alertes);
      })
      .catch(error => {
        console.error('Une erreur s\'est produite :', error);
      });
    }
  }, [ account.login, dispatch]);

  useEffect(() => {
    alertes.forEach(alerte => {
      if (!alerte.verif) {
        setCurrentAlerte(alerte);
        notify(alerte); 
        console.log("alerte non verif: ", alerte);
      }
    });
  }, [alertes]);
  
  const notify = (alerte) => {
    toast(`Alerte non vérifiée: ${alerte.action} pour le patient: ${alerte.patient.nom}`, {
      position: "bottom-right",
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
  }
  return (
    <div>
        <div>
          <PatientHeading loading={loading} handleSyncList={handleSyncList} />
          
    <PatientSearch patientsearch={patientsearch} setPatientsearch={setPatientsearch} handleRunPatient={handleRunPatient} />
    <PatientSearchResults patients={patientsuggestion} getCardColorClass={getCardColorClass} />
    <EhpadFilter patientList={patientList} selectedEhpadFilter={selectedEhpadFilter} setSelectedEhpadFilter={setSelectedEhpadFilter} />
    <SortFilter selectedFilter={selectedFilter} setSelectedFilter={setSelectedFilter} filterDisplayText={filterDisplayText} />
      <FontAwesomeIcon icon={getSortIconByFieldName(selectedFilter)} onClick={sort(selectedFilter)} />
      <StatusFilter handleStatusFilterChange={handleStatusFilterChange} />


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
    <PatientCard patient={patient} />
              </div>
            ))}
          </div>
        </div>
        <div>
        <ToastContainer />
      </div>
    </div>
  );
};

export default Home;