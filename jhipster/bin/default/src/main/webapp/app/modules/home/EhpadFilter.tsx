// EhpadFilter.tsx
import React from 'react';

const EhpadFilter = ({ patientList, selectedEhpadFilter, setSelectedEhpadFilter }) => {
  return (
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
  );
};

export default EhpadFilter;