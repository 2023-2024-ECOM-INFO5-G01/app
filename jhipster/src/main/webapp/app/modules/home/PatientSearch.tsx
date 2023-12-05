// PatientSearch.tsx
import React from 'react';
import { Button } from 'reactstrap';

const PatientSearch = ({ patientsearch, setPatientsearch, handleRunPatient }) => {
  return (
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
  );
};

export default PatientSearch;