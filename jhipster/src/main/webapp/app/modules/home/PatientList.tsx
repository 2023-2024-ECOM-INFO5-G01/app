// PatientList.tsx
import React from 'react';
import PatientCard from './PatientCard';

const PatientList = ({ patients, getCardColorClass }) => {
  return (
    <div className="d-flex flex-wrap">
      {patients.map((patient, i) => (
        <div
          key={`entity-${i}`}
          className={`patient-card ${getCardColorClass(patient.statut)}`} // Apply the card color class
        >
          <PatientCard patient={patient} />
        </div>
      ))}
    </div>
  );
};

export default PatientList;