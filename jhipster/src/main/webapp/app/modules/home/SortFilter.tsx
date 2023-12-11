// SortFilter.tsx
import React from 'react';

const SortFilter = ({ selectedFilter, setSelectedFilter, filterDisplayText }) => {
  return (
    <p className='sortFilter'>{Object.keys(filterDisplayText).map((filter) => (
      <option key={filter} value={filter}>
        {filterDisplayText[filter]}
      </option>
    ))}</p>

    /* Version avec plusieurs filtres
    <select
      value={selectedFilter}
      onChange={(e) => setSelectedFilter(e.target.value)}
    >
      {Object.keys(filterDisplayText).map((filter) => (
        <option key={filter} value={filter}>
          {filterDisplayText[filter]}
        </option>
      ))}
    </select>
    */
  );
};

export default SortFilter;