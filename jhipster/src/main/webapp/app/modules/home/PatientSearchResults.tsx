// PatientSearchResults.tsx
import React from 'react';
import PatientList from './PatientList';

const PatientSearchResults = ({ patients, getCardColorClass }) => {
  return (
    patients.length > 0 && (
      <div>
        <h3>Résultats de la recherche :</h3>
        <PatientList patients={patients} getCardColorClass={getCardColorClass} />
      </div>
    )
  );
};

export default PatientSearchResults;