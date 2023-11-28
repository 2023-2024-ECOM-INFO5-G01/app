import React from 'react';
import PatientList from './PatientList';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

const PatientSearchResults = ({ patients, getCardColorClass, onClose, onClearSearch }) => {
  return (
    patients.length > 0 && (
      <div>
        <div className="d-flex justify-content-between align-items-center">
          <h3>Résultats de la recherche :</h3>
          <button className="btn btn-link" onClick={() => { onClose(); onClearSearch(); }}>
            <FontAwesomeIcon icon={faTimes} />
          </button>
        </div>
        <PatientList patients={patients} getCardColorClass={getCardColorClass} />
      </div>
    )
  );
};

export default PatientSearchResults;
