import React, { useState } from 'react';

const StatusFilter = ({ handleStatusFilterChange}) => {

  const statusOptions = [
    { value: 'surveillance prioritaire', label: 'Surveillance prioritaire' },
    { value: 'surveillance particulière', label: 'Surveillance particulière' },
    { value: 'normal', label: 'Normal' },
  ];

  const [checkboxStates, setCheckboxStates] = useState(
    Object.fromEntries(statusOptions.map((option) => [option.value, true]))
  );

  const handleCheckboxChange = (e, optionValue) => {
    // Compte le nombre de cases cochées
    const checkedCheckboxes = Object.values(checkboxStates).reduce((count, isChecked) => (isChecked ? count + 1 : count), 0);

    // Si une seule case est cochée et qu'on la décocher, pas d'action
    if (checkedCheckboxes === 1 && checkboxStates[optionValue]) {
      return;
    }

    // Sinon on change les états des checkbox et on appel le tri des patient
    setCheckboxStates((prevStates) => ({
      ...prevStates,
      [optionValue]: !prevStates[optionValue],
    }));
    handleStatusFilterChange(optionValue, e.target.checked)
  };

  return (
    <div className="status-filter-container">
          {statusOptions.map((option) => (
            <div key={option.value}>
              <input
                className='input_statut'
                type="checkbox"
                id={`status-${option.value}`}
                name={`status-${option.value}`}
                value={option.value}
                checked={checkboxStates[option.value]}
                onChange={(e) => handleCheckboxChange(e, option.value)}
              />
              <label htmlFor={`status-${option.value}`} className="checkbox-label">
                {option.label}
              </label>
            </div>
          ))}
    </div>
  );
};


export default StatusFilter;
