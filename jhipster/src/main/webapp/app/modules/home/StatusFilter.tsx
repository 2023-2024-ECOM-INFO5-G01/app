import React, { useState } from 'react';

const StatusFilter = ({ handleStatusFilterChange }) => {
  const [showStatusMenu, setShowStatusMenu] = useState(false);

  const toggleStatusMenu = () => {
    setShowStatusMenu(!showStatusMenu);
  };

  const statusOptions = [
    { value: 'dénutrition avérée', label: 'Dénutrition avérée' },
    { value: 'surveillance', label: 'Surveillance' },
    { value: 'normal', label: 'Normal' },
  ];

  return (
    <div className="status-filter-container">
      <button className="custom-search-button" type="button" onClick={toggleStatusMenu}>
        Choix des statuts
      </button>

      {showStatusMenu && (
        <div className="status-menu">
          {statusOptions.map((option) => (
            <div key={option.value}>
              <input
                type="checkbox"
                id={`status-${option.value}`}
                name={`status-${option.value}`}
                value={option.value}
                onChange={(e) => handleStatusFilterChange(option.value, e.target.checked)}
              />
              <label htmlFor={`status-${option.value}`} className="checkbox-label">
                {option.label}
              </label>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};


export default StatusFilter;
