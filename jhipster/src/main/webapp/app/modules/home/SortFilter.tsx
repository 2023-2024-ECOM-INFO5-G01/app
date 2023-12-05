// SortFilter.tsx
import React from 'react';

const SortFilter = ({ selectedFilter, setSelectedFilter, filterDisplayText }) => {
  return (
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
  );
};

export default SortFilter;