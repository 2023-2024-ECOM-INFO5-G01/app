// StatusFilter.tsx
import React from 'react';

const StatusFilter = ({ handleStatusFilterChange }) => {
  return (
    <div>
      <h6>Choix des statuts</h6>
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
  );
};

export default StatusFilter;