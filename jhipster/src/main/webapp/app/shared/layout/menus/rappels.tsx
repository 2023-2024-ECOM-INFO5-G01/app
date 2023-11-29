import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getRappelsByUser,toggleVerif } from 'app/entities/rappel/rappel.reducer';
import { Link } from 'react-router-dom';
import './Alerte.css'; // Import your CSS file
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFileAlt } from '@fortawesome/free-solid-svg-icons';
export const Rappels = () => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();
  const [filter, setFilter] = useState('all'); 

  const [rappels, setRappels] = useState([]);
  useEffect(() => {
    if (account && account.login) {
      dispatch(getRappelsByUser(account.login))
      .then(response => {
        setRappels((response.payload as any).data);
        console.log(rappels);
      })
      .catch(error => {
        console.error('Une erreur s\'est produite :', error);
      });
    }
  }, [account.login, dispatch]);

  const handleToggleVerif = (id: string | number) => { // New function to handle click
    dispatch(toggleVerif(id))
    .then(() => {
      // Refresh the alertes after toggling verif
      if (account && account.login) {
        dispatch(getRappelsByUser(account.login))
        .then(response => {
          setRappels((response.payload as any).data);
        });
      }
    });
  };

  const filteredRappels = rappels.filter(rappel => {
    if (filter === 'all') return true;
    if (filter === 'verified' && rappel.verif) return true;
    if (filter === 'unverified' && !rappel.verif) return true;
    return false;
  });
  return (
    <div>
      <div>
      <select onChange={(e) => setFilter(e.target.value)}>
        <option value="all">Toutes les rappels</option>
        <option value="verified">Rappels vérifiées</option>
        <option value="unverified">Rappels non vérifiées</option>
      </select>
      </div>
      {filteredRappels.map(rappel => (
  <div key={rappel.id} className="alerte">
    <div className="alerte-content">
      <div className="alerte-icon">⚠️</div>
      <div>
        <h2>Rappel </h2>
        <p>Action: {rappel.action}</p>
        {rappel.patient && <p>Patient: {rappel.patient.nom} {rappel.patient.prenom}</p>}
        <p>Date: {rappel.date}</p>
      </div>
      <button className="alerte-check" onClick={() => handleToggleVerif(rappel.id)}>
      {rappel.verif ? '✅' : '⬜'}
      </button>
    </div>
    {rappel.patient && 
      <div className="button-container">
        <Link to={`/patients/${rappel.patient.id}`} className="alerte-button">
        <FontAwesomeIcon icon={faFileAlt} />
          Voir patient</Link>
      </div>
    }  
  </div>
))}
    </div>
  );
};

export default Rappels;