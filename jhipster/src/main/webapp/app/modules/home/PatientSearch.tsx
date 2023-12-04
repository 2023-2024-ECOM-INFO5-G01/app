// PatientSearch.tsx
import React from 'react';
import { Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';

const PatientSearch = ({ patientsearch, setPatientsearch, handleRunPatient }) => {
  return (
    <div>
      <input
        type="text"
        placeholder="Nom ou prénom du patient"
        value={patientsearch}
        onChange={(e) => setPatientsearch(e.target.value)}
        style={{ width: '30%'}}
      />
      <Button
        color="primary"
        className="custom-search-button"
        onClick={handleRunPatient}
        style={{ transition: 'background-color 0.3s' }}
      >
        <FontAwesomeIcon icon={faSearch} style={{ marginRight: '5px' }} />
        &nbsp;
        enter search
      </Button>
    </div>
  );
};

export default PatientSearch;
