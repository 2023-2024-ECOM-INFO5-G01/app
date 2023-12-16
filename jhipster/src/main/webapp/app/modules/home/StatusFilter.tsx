import React from 'react';

const StatusFilter = ({ handleStatusFilterChange }) => {

  const statusOptions = [
    { value: 'surveillance prioritaire', label: 'Surveillance prioritaire' },
    { value: 'surveillance particulière', label: 'Surveillance particulière' },
    { value: 'normal', label: 'Normal' },
  ];

  return (
    <div className="status-filter-container">
          {statusOptions.map((option) => (
            <div key={option.value}>
              <input
                type="checkbox"
                id={`status-${option.value}`}
                name={`status-${option.value}`}
                value={option.value}
                // ajouter le fait que les cases soient cochées de base puis qu'elles puissent être décochées checked={}
                onChange={(e) => handleStatusFilterChange(option.value, e.target.checked)}
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
